package com.lifetime.common.enums;

public enum LevelEnum {
    /**简单*/
    EASY(1,"简单"),
    /**中等 */
    MIDIUM(2,"中等"),
    /**强 */
    STRONG(3,"强"),
    /**很强 */
    VERY_STRONG(4,"很强"),
    /**极强 */
    EXTREMELY_STRONG(5,"极强");

    final Integer code;
    final String message;

    LevelEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
