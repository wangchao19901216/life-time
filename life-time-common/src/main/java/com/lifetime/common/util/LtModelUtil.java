package com.lifetime.common.util;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.google.common.base.CaseFormat;
import com.lifetime.common.annotation.RelationConstDict;
import com.lifetime.common.annotation.RelationDict;
import com.lifetime.common.annotation.RelationOneToMany;
import com.lifetime.common.annotation.RelationOneToOne;
import com.lifetime.common.exception.InvalidDataFieldException;
import com.lifetime.common.object.Tuple2;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.assertj.core.util.Lists;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author:wangchao
 * @date: 2023/5/9-10:55
 * @description: com.lifetime.common.util
 * @Version:1.0
 */
public class LtModelUtil {
    /**
     * 数值型字段。
     */
    public static final Integer NUMERIC_FIELD_TYPE = 0;
    /**
     * 字符型字段。
     */
    public static final Integer STRING_FIELD_TYPE = 1;
    /**
     * 日期型字段。
     */
    public static final Integer DATE_FIELD_TYPE = 2;
    /**
     * 整个工程的实体对象中，创建者Id字段的Java对象名。
     */
    public static final String CREATE_USER_ID_FIELD_NAME = "createUser";
    /**
     * 整个工程的实体对象中，创建时间字段的Java对象名。
     */
    public static final String CREATE_TIME_FIELD_NAME = "createTime";
    /**
     * 整个工程的实体对象中，更新者Id字段的Java对象名。
     */
    public static final String UPDATE_USER_ID_FIELD_NAME = "modifierUser";
    /**
     * 整个工程的实体对象中，更新时间字段的Java对象名。
     */
    public static final String UPDATE_TIME_FIELD_NAME = "modifierTime";
    /**
     * mapToColumnName和mapToColumnInfo使用的缓存。
     */
    private static final Map<String, Tuple2<String, Integer>> CACHED_COLUMNINFO_MAP = new ConcurrentHashMap<>();

    /**
     * 将bean的数据列表转换为Map列表。
     *
     * @param dataList bean数据列表。
     * @param <T>      bean对象类型。
     * @return 转换后的Map列表。
     */
    public static <T> List<Map<String, Object>> beanToMapList(List<T> dataList) {
        if (CollectionUtils.isEmpty(dataList)) {
            return null;
        }
        List<Map<String, Object>> resultList = new LinkedList<>();
        for (T data : dataList) {
            resultList.add(BeanUtil.beanToMap(data));
        }
        return resultList;
    }

    /**
     * 拷贝源类型的集合数据到目标类型的集合中，其中源类型和目标类型中的对象字段类型完全相同。
     * NOTE: 该函数主要应用于框架中，Dto和Model之间的copy，特别针对一对一关联的深度copy。
     * 在Dto中，一对一对象可以使用Map来表示，而不需要使用从表对象的Dto。
     *
     * @param sourceCollection 源类型集合。
     * @param targetClazz      目标类型的Class对象。
     * @param <S>              源类型。
     * @param <T>              目标类型。
     * @return copy后的目标类型对象集合。
     */
    public static <S, T> List<T> copyCollectionTo(Collection<S> sourceCollection, Class<T> targetClazz) {
        if (sourceCollection == null) {
            return null;
        }
        List<T> targetList = new LinkedList<>();
        if (CollectionUtils.isNotEmpty(sourceCollection)) {
            for (S source : sourceCollection) {
                try {
                    T target = targetClazz.newInstance();
                    BeanUtil.copyProperties(source, target);
                    targetList.add(target);
                } catch (Exception e) {
                    return Collections.emptyList();
                }
            }
        }
        return targetList;
    }

    /**
     * 拷贝源类型的对象数据到目标类型的对象中，其中源类型和目标类型中的对象字段类型完全相同。
     * NOTE: 该函数主要应用于框架中，Dto和Model之间的copy，特别针对一对一关联的深度copy。
     * 在Dto中，一对一对象可以使用Map来表示，而不需要使用从表对象的Dto。
     *
     * @param source      源类型对象。
     * @param targetClazz 目标类型的Class对象。
     * @param <S>         源类型。
     * @param <T>         目标类型。
     * @return copy后的目标类型对象。
     */
    public static <S, T> T copyTo(S source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        try {
            T target = targetClazz.newInstance();
            BeanUtil.copyProperties(source, target);
            return target;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 映射Model对象的字段反射对象，获取与该字段对应的数据库列名称。
     *
     * @param field      字段反射对象。
     * @param modelClazz Model对象的Class类。
     * @return 该字段所对应的数据表列名称。
     */
    public static String mapToColumnName(Field field, Class<?> modelClazz) {
        return mapToColumnName(field.getName(), modelClazz);
    }

    /**
     * 映射Model对象的字段名称，获取与该字段对应的数据库列名称。
     *
     * @param fieldName  字段名称。
     * @param modelClazz Model对象的Class类。
     * @return 该字段所对应的数据表列名称。
     */
    public static String mapToColumnName(String fieldName, Class<?> modelClazz) {
        Tuple2<String, Integer> columnInfo = mapToColumnInfo(fieldName, modelClazz);
        return columnInfo == null ? null : columnInfo.getFirst();
    }

    /**
     * 映射Model对象的字段反射对象，获取与该字段对应的数据库列名称。
     * 如果没有匹配到ColumnName，则立刻抛出异常。
     *
     * @param field      字段反射对象。
     * @param modelClazz Model对象的Class类。
     * @return 该字段所对应的数据表列名称。
     */
    public static String safeMapToColumnName(Field field, Class<?> modelClazz) {
        return safeMapToColumnName(field.getName(), modelClazz);
    }

    /**
     * 映射Model对象的字段名称，获取与该字段对应的数据库列名称。
     * 如果没有匹配到ColumnName，则立刻抛出异常。
     *
     * @param fieldName  字段名称。
     * @param modelClazz Model对象的Class类。
     * @return 该字段所对应的数据表列名称。
     */
    public static String safeMapToColumnName(String fieldName, Class<?> modelClazz) {
        String columnName = mapToColumnName(fieldName, modelClazz);
        if (columnName == null) {
            throw new InvalidDataFieldException(modelClazz.getSimpleName(), fieldName);
        }
        return columnName;
    }

    /**
     * 映射Model对象的字段名称，获取与该字段对应的数据库列名称和字段类型。
     *
     * @param fieldName  字段名称。
     * @param modelClazz Model对象的Class类。
     * @return 该字段所对应的数据表列名称和Java字段类型。
     */
    public static Tuple2<String, Integer> mapToColumnInfo(String fieldName, Class<?> modelClazz) {
        if (StringUtils.isBlank(fieldName)) {
            return null;
        }
        StringBuilder sb = new StringBuilder(128);
        sb.append(modelClazz.getName()).append("-#-").append(fieldName);
        Tuple2<String, Integer> columnInfo = CACHED_COLUMNINFO_MAP.get(sb.toString());
        if (columnInfo == null) {
            Field field = ReflectUtil.getField(modelClazz, fieldName);
            if (field == null) {
                return null;
            }
            TableField c = field.getAnnotation(TableField.class);
            String columnName = null;
            if (c == null) {
                TableId id = field.getAnnotation(TableId.class);
                if (id != null) {
                    columnName = id.value();
                }
            }
            if (columnName == null) {
                columnName = c == null ? CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName) : c.value();
                if (StringUtils.isBlank(columnName)) {
                    columnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
                }
            }
            // 这里缺省情况下都是按照整型去处理，因为他覆盖太多的类型了。
            // 如Integer/Long/Double/BigDecimal，可根据实际情况完善和扩充。
            String typeName = field.getType().getSimpleName();
            Integer type = NUMERIC_FIELD_TYPE;
            if (String.class.getSimpleName().equals(typeName)) {
                type = STRING_FIELD_TYPE;
            } else if (Date.class.getSimpleName().equals(typeName)) {
                type = DATE_FIELD_TYPE;
            }
            columnInfo = new Tuple2<>(columnName, type);
            CACHED_COLUMNINFO_MAP.put(sb.toString(), columnInfo);
        }
        return columnInfo;
    }

    /**
     * 映射Model主对象的Class名称，到Model所对应的表名称。
     *
     * @param modelClazz Model主对象的Class。
     * @return Model对象对应的数据表名称。
     */
    public static String mapToTableName(Class<?> modelClazz) {
        TableName t = modelClazz.getAnnotation(TableName.class);
        return t == null ? null : t.value();
    }

    /**
     * 主Model类型中，遍历所有包含RelationConstDict注解的字段，并将关联的静态字典中的数据，
     * 填充到thisModel对象的被注解字段中。
     *
     * @param thisClazz 主对象的Class对象。
     * @param thisModel 主对象。
     * @param <T>       主表对象类型。
     */
    @SuppressWarnings("unchecked")
    public static <T> void makeConstDictRelation(Class<T> thisClazz, T thisModel) {
        if (thisModel == null) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(thisClazz);
        for (Field field : fields) {
            // 这里不做任何空值判断，从而让配置错误在调试期间即可抛出
            Field thisTargetField = ReflectUtil.getField(thisClazz, field.getName());
            RelationConstDict r = thisTargetField.getAnnotation(RelationConstDict.class);
            if (r == null) {
                continue;
            }
            Field dictMapField = ReflectUtil.getField(r.constantDictClass(), "DICT_MAP");
            Map<Object, String> dictMap =
                    (Map<Object, String>) ReflectUtil.getFieldValue(r.constantDictClass(), dictMapField);
            Object id = ReflectUtil.getFieldValue(thisModel, r.masterIdField());
            if (id != null) {
                String name = dictMap.get(id);
                if (name != null) {
                    Map<String, Object> m = new HashMap<>(2);
                    m.put("id", id);
                    m.put("name", name);
                    ReflectUtil.setFieldValue(thisModel, thisTargetField, m);
                }
            }
        }
    }

    /**
     * 主Model类型中，遍历所有包含RelationConstDict注解的字段，并将关联的静态字典中的数据，
     * 填充到thisModelList集合元素对象的被注解字段中。
     *
     * @param thisClazz     主对象的Class对象。
     * @param thisModelList 主对象列表。
     * @param <T>           主表对象类型。
     */
    @SuppressWarnings("unchecked")
    public static <T> void makeConstDictRelation(Class<T> thisClazz, List<T> thisModelList) {
        if (CollectionUtils.isEmpty(thisModelList)) {
            return;
        }
        Field[] fields = ReflectUtil.getFields(thisClazz);
        for (Field field : fields) {
            // 这里不做任何空值判断，从而让配置错误在调试期间即可抛出
            Field thisTargetField = ReflectUtil.getField(thisClazz, field.getName());
            RelationConstDict r = thisTargetField.getAnnotation(RelationConstDict.class);
            if (r == null) {
                continue;
            }
            Field dictMapField = ReflectUtil.getField(r.constantDictClass(), "DICT_MAP");
            Map<Object, String> dictMap =
                    (Map<Object, String>) ReflectUtil.getFieldValue(r.constantDictClass(), dictMapField);
            for (T thisModel : thisModelList) {
                if (thisModel == null) {
                    continue;
                }
                Object id = ReflectUtil.getFieldValue(thisModel, r.masterIdField());
                if (id != null) {
                    String name = dictMap.get(id);
                    if (name != null) {
                        Map<String, Object> m = new HashMap<>(2);
                        m.put("id", id);
                        m.put("name", name);
                        ReflectUtil.setFieldValue(thisModel, thisTargetField, m);
                    }
                }
            }
        }
    }

    /**
     * 在主Model类型中，根据thisRelationField字段的RelationDict注解参数，将被关联对象thatModel中的数据，
     * 关联到thisModel对象的thisRelationField字段中。
     *
     * @param thisClazz         主对象的Class对象。
     * @param thisModel         主对象。
     * @param thatModel         字典关联对象。
     * @param thisRelationField 主表对象中保存被关联对象的字段名称。
     * @param <T>               主表对象类型。
     * @param <R>               从表对象类型。
     */
    public static <T, R> void makeDictRelation(
            Class<T> thisClazz, T thisModel, R thatModel, String thisRelationField) {
        if (thatModel == null || thisModel == null) {
            return;
        }
        // 这里不做任何空值判断，从而让配置错误在调试期间即可抛出
        Field thisTargetField = ReflectUtil.getField(thisClazz, thisRelationField);
        RelationDict r = thisTargetField.getAnnotation(RelationDict.class);
        Class<?> thatClass = r.slaveModelClass();
        Field slaveIdField = ReflectUtil.getField(thatClass, r.slaveIdField());
        Field slaveNameField = ReflectUtil.getField(thatClass, r.slaveNameField());
        Map<String, Object> m = new HashMap<>(2);
        m.put("id", ReflectUtil.getFieldValue(thatModel, slaveIdField));
        m.put("name", ReflectUtil.getFieldValue(thatModel, slaveNameField));
        ReflectUtil.setFieldValue(thisModel, thisTargetField, m);
    }

    /**
     * 在主Model类型中，根据thisRelationField字段的RelationDict注解参数，将被关联对象集合thatModelList中的数据，
     * 逐个关联到thisModelList每一个元素的thisRelationField字段中。
     *
     * @param thisClazz         主对象的Class对象。
     * @param thisModelList     主对象列表。
     * @param thatModelList     字典关联对象列表集合。
     * @param thisRelationField 主表对象中保存被关联对象的字段名称。
     * @param <T>               主表对象类型。
     * @param <R>               从表对象类型。
     */
    public static <T, R> void makeDictRelation(
            Class<T> thisClazz, List<T> thisModelList, List<R> thatModelList, String thisRelationField) {
        if (CollectionUtils.isEmpty(thatModelList)
                || CollectionUtils.isEmpty(thisModelList)) {
            return;
        }
        // 这里不做任何空值判断，从而让配置错误在调试期间即可抛出
        Field thisTargetField = ReflectUtil.getField(thisClazz, thisRelationField);
        RelationDict r = thisTargetField.getAnnotation(RelationDict.class);
        Field masterIdField = ReflectUtil.getField(thisClazz, r.masterIdField());
        Class<?> thatClass = r.slaveModelClass();
        Field slaveIdField = ReflectUtil.getField(thatClass, r.slaveIdField());
        Field slaveNameField = ReflectUtil.getField(thatClass, r.slaveNameField());
        Map<Object, R> thatMap = new HashMap<>(20);
        thatModelList.forEach(thatModel -> {
            Object id = ReflectUtil.getFieldValue(thatModel, slaveIdField);
            thatMap.put(id, thatModel);
        });
        thisModelList.forEach(thisModel -> {
            if (thisModel != null) {
                Object id = ReflectUtil.getFieldValue(thisModel, masterIdField);
                R thatModel = thatMap.get(id);
                if (thatModel != null) {
                    Map<String, Object> m = new HashMap<>(4);
                    m.put("id", id);
                    m.put("name", ReflectUtil.getFieldValue(thatModel, slaveNameField));
                    ReflectUtil.setFieldValue(thisModel, thisTargetField, m);
                }
            }
        });
    }

    /**
     * 在主Model类型中，根据thisRelationField字段的RelationDict注解参数，将被关联对象集合thatModelMap中的数据，
     * 逐个关联到thisModelList每一个元素的thisRelationField字段中。
     * 该函数之所以使用Map，主要出于性能优化考虑，在连续使用thatModelMap进行关联时，有效的避免了从多次从List转换到Map的过程。
     *
     * @param thisClazz         主对象的Class对象。
     * @param thisModelList     主对象列表。
     * @param thatMadelMap      字典关联对象映射集合。
     * @param thisRelationField 主表对象中保存被关联对象的字段名称。
     * @param <T>               主表对象类型。
     * @param <R>               从表对象类型。
     */
    public static <T, R> void makeDictRelation(
            Class<T> thisClazz, List<T> thisModelList, Map<Object, R> thatMadelMap, String thisRelationField) {
        if (MapUtils.isEmpty(thatMadelMap)
                || CollectionUtils.isEmpty(thisModelList)) {
            return;
        }
        // 这里不做任何空值判断，从而让配置错误在调试期间即可抛出
        Field thisTargetField = ReflectUtil.getField(thisClazz, thisRelationField);
        RelationDict r = thisTargetField.getAnnotation(RelationDict.class);
        Field masterIdField = ReflectUtil.getField(thisClazz, r.masterIdField());
        Class<?> thatClass = r.slaveModelClass();
        Field slaveNameField = ReflectUtil.getField(thatClass, r.slaveNameField());
        thisModelList.forEach(thisModel -> {
            if (thisModel != null) {
                Object id = ReflectUtil.getFieldValue(thisModel, masterIdField);
                R thatModel = thatMadelMap.get(id);
                if (thatModel != null) {
                    Map<String, Object> m = new HashMap<>(4);
                    m.put("id", id);
                    m.put("name", ReflectUtil.getFieldValue(thatModel, slaveNameField));
                    ReflectUtil.setFieldValue(thisModel, thisTargetField, m);
                }
            }
        });
    }

    /**
     * 在主Model类型中，根据thisRelationField字段的RelationOneToOne注解参数，将被关联对象列表thatModelList中的数据，
     * 逐个关联到thisModelList每一个元素的thisRelationField字段中。
     *
     * @param thisClazz         主对象的Class对象。
     * @param thisModelList     主对象列表。
     * @param thatModelList     一对一关联对象列表。
     * @param thisRelationField 主表对象中保存被关联对象的字段名称。
     * @param <T>               主表对象类型。
     * @param <R>               从表对象类型。
     */
    public static <T, R> void makeOneToOneRelation(
            Class<T> thisClazz, List<T> thisModelList, List<R> thatModelList, String thisRelationField) {
        if (CollectionUtils.isEmpty(thatModelList)
                || CollectionUtils.isEmpty(thisModelList)) {
            return;
        }
        // 这里不做任何空值判断，从而让配置错误在调试期间即可抛出
        Field thisTargetField = ReflectUtil.getField(thisClazz, thisRelationField);
        RelationOneToOne r = thisTargetField.getAnnotation(RelationOneToOne.class);
        Field masterIdField = ReflectUtil.getField(thisClazz, r.masterIdField());
        Class<?> thatClass = r.slaveModelClass();
        Field slaveIdField = ReflectUtil.getField(thatClass, r.slaveIdField());
        Map<Object, R> thatMap = new HashMap<>(20);
        thatModelList.forEach(thatModel -> {
            Object id = ReflectUtil.getFieldValue(thatModel, slaveIdField);
            thatMap.put(id, thatModel);
        });
        // 判断放在循环的外部，提升一点儿效率。
        if (thisTargetField.getType().equals(Map.class)) {
            thisModelList.forEach(thisModel -> {
                Object id = ReflectUtil.getFieldValue(thisModel, masterIdField);
                R thatModel = thatMap.get(id);
                if (thatModel != null) {
                    ReflectUtil.setFieldValue(thisModel, thisTargetField, BeanUtil.beanToMap(thatModel));
                }
            });
        } else {
            thisModelList.forEach(thisModel -> {
                Object id = ReflectUtil.getFieldValue(thisModel, masterIdField);
                R thatModel = thatMap.get(id);
                if (thatModel != null) {
                    ReflectUtil.setFieldValue(thisModel, thisTargetField, thatModel);
                }
            });
        }
    }

    /**
     * 根据主对象和关联对象各自的关联Id函数，将主对象列表和关联对象列表中的数据关联到一起，并将关联对象
     * 设置到主对象的指定关联字段中。
     * NOTE: 用于主对象关联字段中，没有包含RelationOneToOne注解的场景。
     *
     * @param thisClazz         主对象的Class对象。
     * @param thisModelList     主对象列表。
     * @param thisIdGetterFunc  主对象Id的Getter函数。
     * @param thatModelList     关联对象列表。
     * @param thatIdGetterFunc  关联对象Id的Getter函数。
     * @param thisRelationField 主对象中保存被关联对象的字段名称。
     * @param <T>               主表对象类型。
     * @param <R>               从表对象类型。
     */
    public static <T, R> void makeOneToOneRelation(
            Class<T> thisClazz,
            List<T> thisModelList,
            Function<T, Object> thisIdGetterFunc,
            List<R> thatModelList,
            Function<R, Object> thatIdGetterFunc,
            String thisRelationField) {
        makeOneToOneRelation(thisClazz, thisModelList,
                thisIdGetterFunc, thatModelList, thatIdGetterFunc, thisRelationField, false);
    }

    /**
     * 根据主对象和关联对象各自的关联Id函数，将主对象列表和关联对象列表中的数据关联到一起，并将关联对象
     * 设置到主对象的指定关联字段中。
     * NOTE: 用于主对象关联字段中，没有包含RelationOneToOne注解的场景。
     *
     * @param thisClazz         主对象的Class对象。
     * @param thisModelList     主对象列表。
     * @param thisIdGetterFunc  主对象Id的Getter函数。
     * @param thatModelList     关联对象列表。
     * @param thatIdGetterFunc  关联对象Id的Getter函数。
     * @param thisRelationField 主对象中保存被关联对象的字段名称。
     * @param orderByThatList   如果为true，则按照ThatModelList的顺序输出。同时thisModelList被排序后的新列表替换。
     * @param <T>               主表对象类型。
     * @param <R>               从表对象类型。
     */
    public static <T, R> void makeOneToOneRelation(
            Class<T> thisClazz,
            List<T> thisModelList,
            Function<T, Object> thisIdGetterFunc,
            List<R> thatModelList,
            Function<R, Object> thatIdGetterFunc,
            String thisRelationField,
            boolean orderByThatList) {
        if (CollectionUtils.isEmpty(thisModelList)) {
            return;
        }
        Field thisTargetField = ReflectUtil.getField(thisClazz, thisRelationField);
        boolean isMap = thisTargetField.getType().equals(Map.class);
        if (orderByThatList) {
            List<T> newThisModelList = new LinkedList<>();
            Map<Object, ? extends T> thisModelMap =
                    thisModelList.stream().collect(Collectors.toMap(thisIdGetterFunc, c -> c));
            thatModelList.forEach(thatModel -> {
                Object thatId = thatIdGetterFunc.apply(thatModel);
                T thisModel = thisModelMap.get(thatId);
                if (thisModel != null) {
                    ReflectUtil.setFieldValue(thisModel, thisTargetField, normalize(isMap, thatModel));
                    newThisModelList.add(thisModel);
                }
            });
            thisModelList.clear();
            thisModelList.addAll(newThisModelList);
        } else {
            Map<Object, R> thatMadelMap =
                    thatModelList.stream().collect(Collectors.toMap(thatIdGetterFunc, c -> c));
            thisModelList.forEach(thisModel -> {
                Object thisId = thisIdGetterFunc.apply(thisModel);
                R thatModel = thatMadelMap.get(thisId);
                if (thatModel != null) {
                    ReflectUtil.setFieldValue(thisModel, thisTargetField, normalize(isMap, thatModel));
                }
            });
        }
    }

    /**
     * 在主Model类型中，根据thisRelationField字段的RelationOneToMany注解参数，将被关联对象列表thatModelList中的数据，
     * 逐个关联到thisModelList每一个元素的thisRelationField字段中。
     *
     * @param thisClazz         主对象的Class对象。
     * @param thisModelList     主对象列表。
     * @param thatModelList     一对多关联对象列表。
     * @param thisRelationField 主表对象中保存被关联对象的字段名称。
     * @param <T>               主表对象类型。
     * @param <R>               从表对象类型。
     */
    public static <T, R> void makeOneToManyRelation(
            Class<T> thisClazz, List<T> thisModelList, List<R> thatModelList, String thisRelationField) {
        if (CollectionUtils.isEmpty(thatModelList) || CollectionUtils.isEmpty(thisModelList)) {
            return;
        }
        // 这里不做任何空值判断，从而让配置错误在调试期间即可抛出
        Field thisTargetField = ReflectUtil.getField(thisClazz, thisRelationField);
        RelationOneToMany r = thisTargetField.getAnnotation(RelationOneToMany.class);
        Field masterIdField = ReflectUtil.getField(thisClazz, r.masterIdField());
        Class<?> thatClass = r.slaveModelClass();
        Field slaveIdField = ReflectUtil.getField(thatClass, r.slaveIdField());
        Map<Object, List<R>> thatMap = new HashMap<>(20);
        thatModelList.forEach(thatModel -> {
            Object id = ReflectUtil.getFieldValue(thatModel, slaveIdField);
            List<R> thatModelSubList = thatMap.computeIfAbsent(id, k -> new LinkedList<>());
            thatModelSubList.add(thatModel);
        });
        thisModelList.forEach(thisModel -> {
            Object id = ReflectUtil.getFieldValue(thisModel, masterIdField);
            List<R> thatModel = thatMap.get(id);
            if (thatModel != null) {
                ReflectUtil.setFieldValue(thisModel, thisTargetField, thatModel);
            }
        });
    }

    private static <M> Object normalize(boolean isMap, M model) {
        return isMap ? BeanUtil.beanToMap(model) : model;
    }





    /**
     * 为实体对象字段设置缺省值。如果data对象中指定字段的值为NULL，则设置缺省值，否则跳过。
     * @param data         实体对象。
     * @param fieldName    实体对象字段名。
     * @param defaultValue 缺省值。
     * @param <M> 实体对象类型。
     * @param <V> 缺省值类型。
     */
    public static <M, V> void setDefaultValue(M data, String fieldName, V defaultValue) {
        Object v = ReflectUtil.getFieldValue(data, fieldName);
        if (v == null) {
            ReflectUtil.setFieldValue(data, fieldName, defaultValue);
        }
    }





    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private LtModelUtil() {
    }


    /**
     * 对比两个对象内容区别
     * @param a
     * @param b
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @return 返回区别的文本内容
     */
    public static String getDeclaredFields(Object a, Object b)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] declaredFields2 = a.getClass().getDeclaredFields();
        Field[] declaredFields23 = b.getClass().getDeclaredFields();
        StringBuffer content = new StringBuffer();
        List<String> fieldNames = LtModelUtil.getFieldName();
        Map<String, String> map = LtModelUtil.getAttributeName(a.getClass());
        for (int i = 0; i < declaredFields2.length; i++) {
            declaredFields2[i].setAccessible(true);
            declaredFields23[i].setAccessible(true);
            if(fieldNames.contains(declaredFields2[i].getName())){
                continue;
            }
            if (declaredFields2[i].get(a) != null && declaredFields23[i].get(b) != null) {
                if (!declaredFields2[i].get(a).equals(declaredFields23[i].get(b))) {
                    content.append(map.get(declaredFields2[i].getName()) + ":" +declaredFields2[i].get(a)+"->"+declaredFields23[i].get(b)).append(",");
                }
            } else if (declaredFields2[i].get(a) == null && StringUtils.isNotBlank(declaredFields23[i].get(b)+"")) {
                if (!"null".equals(declaredFields23[i].get(b)+"")) {
                    content.append(map.get(declaredFields2[i].getName()) + ":" +null+"->"+declaredFields23[i].get(b)).append(",");
                }
            } else if (StringUtils.isNotBlank(declaredFields2[i].get(a)+"") && declaredFields23[i].get(b) == null) {
                content.append(map.get(declaredFields2[i].getName()) + ":" +declaredFields2[i].get(a)+"->"+null).append(",");
            }
        }
        return content.toString();

    }

    /**
     *
     * @return 字段名
     */
    public static List<String> getFieldName(){
        List<String> fieldNames= Lists.newArrayList();
        fieldNames.add("createTime");
        fieldNames.add("modifierTime");
        fieldNames.add("createUser");
        fieldNames.add("modifierUser");
        return fieldNames;
    }


    /**
     *   将实体类的属性和注释转换成参数
     * @param target
     * @return
     */
    public static Map<String, String> getAttributeName(Class target){
        Map<String, String> soundVo = new HashMap();
        Field[] fields = target.getDeclaredFields();
        for (Field field : fields) {
            boolean bool = field.isAnnotationPresent(ApiModelProperty.class);
            if (bool) {
                String value = field.getAnnotation(ApiModelProperty.class).value();
                soundVo.put(field.getName(), value);
            }
        }
       // System.out.println(soundVo);
        return soundVo;
    }

    @SuppressWarnings("unchecked")
    public static <T> T deepCopy(T object) {
        try {
            // 将对象写入字节数组输出流
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(byteOut);
            out.writeObject(object);
            out.flush();

            // 将字节数组输出流的内容转为字节数组，然后读入
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);

            // 返回反序列化后的新对象
            return (T) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("深拷贝失败", e);
        }
    }

}

