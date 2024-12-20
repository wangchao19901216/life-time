package com.lifetime.common.enums;

public enum PermissionTypeEnum {

   Catalog ("0","目录"),

    Menu("1","菜单"),

    Button("2","按钮");

    final String code;
    final String message;

    PermissionTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
