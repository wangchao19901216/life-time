package com.lifetime.common.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author:wangchao
 * @date: 2023/5/9-21:32
 * @description: com.lifetime.common.util
 * @Version:1.0
 */
public class LtCommonUtil {
    /**
     * 创建uuid。
     *
     * @return 返回uuid。
     */
    public static String generateUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 对用户密码进行加盐后加密。
     *
     * @param password     明文密码。
     * @param passwordSalt 盐值。
     * @return 加密后的密码。
     */
    public static String encryptPassword(String password, String passwordSalt) {
        return DigestUtil.md5Hex(password + passwordSalt);
    }

    /**
     * 这个方法一般用于Controller对于入口参数的基本验证。
     * 对于字符串，如果为空字符串，也将视为Blank，同时返回true。
     *
     * @param objs 一组参数。
     * @return 返回是否存在null或空字符串的参数。
     */
    public static boolean existBlankArgument(Object...objs) {
        for (Object obj : objs) {
            if (LtCommonUtil.isBlankOrNull(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结果和 existBlankArgument 相反。
     *
     * @param objs 一组参数。
     * @return 返回是否存在null或空字符串的参数。
     */
    public static boolean existNotBlankArgument(Object...objs) {
        for (Object obj : objs) {
            if (!LtCommonUtil.isBlankOrNull(obj)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 验证参数是否为空。
     *
     * @param obj 待判断的参数。
     * @return 空或者null返回true，否则false。
     */
    public static boolean isBlankOrNull(Object obj) {
        if (obj instanceof Collection) {
            return CollUtil.isEmpty((Collection<?>) obj);
        }
        return obj == null || (obj instanceof CharSequence && StrUtil.isBlank((CharSequence) obj));
    }

    /**
     * 验证参数是否为非空。
     *
     * @param obj 待判断的参数。
     * @return 空或者null返回false，否则true。
     */
    public static boolean isNotBlankOrNull(Object obj) {
        return !isBlankOrNull(obj);
    }


    /**
     * 拼接参数中的字符串列表，用指定分隔符进行分割，同时每个字符串对象用单引号括起来。
     *
     * @param dataList  字符串集合。
     * @param separator 分隔符。
     * @return 拼接后的字符串。
     */
    public static String joinString(Collection<String> dataList, final char separator) {
        int index = 0;
        StringBuilder sb = new StringBuilder(128);
        for (String data : dataList) {
            sb.append("'").append(data).append("'");
            if (index++ != dataList.size() - 1) {
                sb.append(separator);
            }
        }
        return sb.toString();
    }

    /**
     * 将SQL Like中的通配符替换为字符本身的含义，以便于比较。
     *
     * @param str 待替换的字符串。
     * @return 替换后的字符串。
     */
    public static String replaceSqlWildcard(String str) {
        if (StrUtil.isBlank(str)) {
            return str;
        }
        return StrUtil.replaceChars(StrUtil.replaceChars(str, "_", "\\_"), "%", "\\%");
    }

    /**
     * 获取对象中，非空字段的名字列表。
     *
     * @param object 数据对象。
     * @param clazz  数据对象的class类型。
     * @param <T>    数据对象类型。
     * @return 数据对象中，值不为NULL的字段数组。
     */
    public static <T> String[] getNotNullFieldNames(T object, Class<T> clazz) {
        Field[] fields = ReflectUtil.getFields(clazz);
        List<String> fieldNameList = Arrays.stream(fields)
                .filter(f -> ReflectUtil.getFieldValue(object, f) != null)
                .map(Field::getName).collect(Collectors.toList());
        if (CollUtil.isNotEmpty(fieldNameList)) {
            return fieldNameList.toArray(new String[]{});
        }
        return new String[]{};
    }


    /**
     * 私有构造函数，明确标识该常量类的作用。
     */
    private LtCommonUtil() {
    }
}
