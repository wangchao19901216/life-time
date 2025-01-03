package com.lifetime.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.CaseFormat;
import com.lifetime.common.model.QueryModel;
import com.lifetime.common.response.SearchModel;
import com.lifetime.common.response.SearchRequest;
import com.lifetime.common.response.SearchResponse;
import org.apache.commons.lang.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author:wangchao
 * @date: 2023/5/4-19:22
 * @description: com.lifetime.common.util
 * @Version:1.0
 */
public class QueryUtil<T> {
    public static <T> QueryModel<T> buildMyQuery(SearchRequest searchRequest, Class<T> tClass) {
        QueryModel<T> myQuery = new QueryModel<>();

        Page<T> page = new Page<>(searchRequest.pageParams.pageIndex, searchRequest.pageParams.pageSize);
        QueryWrapper<T> queryWrapper = new QueryWrapper<>();
        for (SearchModel searchModel : searchRequest.searchParams) {
            if (StringUtils.isNotBlank(searchModel.value)) {
                switch (searchModel.match) {
                    case "like":
                        queryWrapper.like(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "eq":
                        queryWrapper.eq(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "ne":
                        queryWrapper.ne(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "gt":
                        queryWrapper.gt(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "ge":
                        queryWrapper.ge(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "lt":
                        queryWrapper.lt(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "notLike":
                        queryWrapper.notLike(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "likeLeft":
                        queryWrapper.likeLeft(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "likeRight":
                        queryWrapper.likeRight(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "isNull":
                        queryWrapper.isNull(LtModelUtil.mapToColumnName(searchModel.key, tClass));
                        break;
                    case "isNotNull":
                        queryWrapper.isNotNull(LtModelUtil.mapToColumnName(searchModel.key, tClass));
                        break;
                    case "in":
                        queryWrapper.in(LtModelUtil.mapToColumnName(searchModel.key, tClass), Arrays.asList(searchModel.value.split(",")));
                        break;
                    case "notIn":
                        queryWrapper.notIn(LtModelUtil.mapToColumnName(searchModel.key, tClass), Arrays.asList(searchModel.value.split(",")));
                        break;
                    case "inSql":
                        queryWrapper.inSql(LtModelUtil.mapToColumnName(searchModel.key, tClass), searchModel.value);
                        break;
                    case "apply":
                        queryWrapper.apply(searchModel.value);
                        break;
                    case "exists":
                        queryWrapper.exists(searchModel.value);
                        break;
                    case "groupBy":
                        queryWrapper.groupBy(searchModel.value);
                        break;
                    case "orderBy":
                        queryWrapper.orderBy(true, true, exchange(Arrays.asList(searchModel.value.split(",")),tClass));
                        break;
                    case "orderByAsc":
                        queryWrapper.orderByAsc(exchange(Arrays.asList(searchModel.value.split(",")),tClass));
                        break;
                    case "orderByDesc":
                        queryWrapper.orderByDesc(exchange(Arrays.asList(searchModel.value.split(",")),tClass));
                        break;
                    case "having":
                        queryWrapper.having(searchModel.value);
                        break;
                    default:
                        break;
                }
            }
        }
        myQuery.setPage(page);
        myQuery.setQueryWrapper(queryWrapper);
        return myQuery;
    }



    public static  List<String>  exchange(List<String> sources,Class tClass){
        List<String> target=new ArrayList<>();
        for(String str:sources){
            String res= LtModelUtil.mapToColumnName(str, tClass);
            target.add(res);
        }
        return  target;
    }

    public static <T> SearchResponse<T> executeQuery(SearchRequest searchRequest, QueryModel myQuery,
                                                     ServiceImpl service) {
        SearchResponse<T> searchResponse = new SearchResponse<>();
        searchResponse.pageInfo = searchRequest.pageParams;
        if (searchResponse.pageInfo.pageSize == -1) {
            searchResponse.results = service.list(myQuery.getQueryWrapper());
            searchResponse.pageInfo.total = searchResponse.results.size();
        } else {
            IPage<T> iPage = service.page(myQuery.getPage(), myQuery.getQueryWrapper());
            searchResponse.results = iPage.getRecords();
            searchResponse.pageInfo.total = (int) iPage.getTotal();
        }
        return searchResponse;
    }

    /**
     * @return
     * @Author Liwx
     * @Description 把queryWrapper<Entity> 转换为sqlsegment
     * @Date 2023/5/30 14:43
     * @Param
     **/
    public static <T> QueryWrapper<T> entityToWrapper(Object obj)   {
        Class aClass = obj.getClass();
        Class superclass = aClass.getSuperclass();
        QueryWrapper wrapper = new QueryWrapper();
        //遍历属性
        List<Field> allFields = new ArrayList<Field>();

        Field[] declaredFields = aClass.getDeclaredFields();
        allFields.addAll(new ArrayList<>(Arrays.asList(declaredFields)));
        allFields.addAll(new ArrayList<>(Arrays.asList(superclass.getDeclaredFields())));

        for (Field field : allFields) {
            Method method = null;

            field.setAccessible(true);
            String fieldName = field.getName();
            //跳过serialVersionUID
            if (fieldName.equals("serialVersionUID")) {
                continue;
            }
            //把列名转为库里的字段名称
            String value = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fieldName);
            //get方法
            try {
                method = aClass.getDeclaredMethod("get" + captureName(fieldName), null);

            } catch (NoSuchMethodException e) {
                try {
                    method = superclass.getDeclaredMethod("get" + captureName(fieldName), null);
                } catch (NoSuchMethodException ex) {
                    throw new RuntimeException(ex);
                }

            }
            Object returnValue = null;
            try {
                returnValue = method.invoke(obj);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
            if (null == returnValue) {
                continue;
            }
            if (returnValue instanceof String) {
                String str = (String) returnValue;
                wrapper.eq(StringUtils.isNotBlank(str), value, returnValue);
            } else {
                wrapper.eq(returnValue != null, value, returnValue);
            }

        }

        return wrapper;


    }

    private static String captureName(String str) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] cs = str.toCharArray();
        cs[0] -= 32;
        return String.valueOf(cs);
    }


}
