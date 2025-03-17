package com.lifetime.common.enums;

/**
 * @author:wangchao
 * @date: 2025/2/26-17:04
 * @description: com.lifetime.common.enums
 * @Version:1.0
 */
public enum ApiAuthEnum {
    NONE("none", "无认证"),
    CODE("app_code", "code认证"),
    APP_SECRET("app_secret", "密钥认证");

    private String name;
    private String value;

    ApiAuthEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
