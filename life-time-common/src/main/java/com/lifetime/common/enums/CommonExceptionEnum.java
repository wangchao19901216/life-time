package com.lifetime.common.enums;

/**
 * @ClassName CommonExceptionEnum
 * @Author Liwx
 * @Description 异常枚举
 */

public enum CommonExceptionEnum {

    ERROR_FIND_DISPOSE_REPORT(500, "处置报告信息查找失败"),
    ERROR_FIND_DISPOSE(500, "处置信息查找失败"),
    ERROR_RELATION(500, "处理先联信息失败"),
    ERROR_ATTACHMENT(500, "处理附件失败"),

    ERROR_EMPTY(501, "当前数据不存在"),
    DATA_NOT_EXIST(501,"数据不存在！"),
    UNHANDLED_EXCEPTION(500,"未处理的异常！"),
    INVALID_PASSWORD(500,"密码错误，请重试！"),
    INVALID_USERNAME(500,"用户名错误，请重试！"),
    INVALID_USERNAME_PASSWORD(500,"用户名或密码错误，请重试！"),
    INVALID_ACCESS_TOKEN(500,"无效的用户访问令牌！"),
    ARGUMENT_NULL_EXIST(500,"数据验证失败，接口调用参数存在空值，请核对！"),
    ARGUMENT_PK_ID_NULL(500,"数据验证失败，接口调用主键Id参数为空，请核对！"),
    INVALID_ARGUMENT_FORMAT(500,"数据验证失败，不合法的参数格式，请核对！"),
    INVALID_STATUS_ARGUMENT(500,"数据验证失败，无效的状态参数值，请核对！"),
    DATA_SAVE_FAILED(500,"数据保存失败！"),
    DATA_DELETE_FAILED(500,"数据删除失败！"),
    DATA_UPDATE_FAILED(500,"数据更新失败！"),
    DATA_SEARCH_FAILED(500,"数据查询失败！"),
    DATA_ACCESS_FAILED(500,"数据访问失败，请联系管理员！"),
    DATA_PERM_ACCESS_FAILED(500,"数据访问失败，您没有该页面的数据访问权限！"),
    INVALID_DATA_MODEL(500,"数据验证失败，无效的数据实体对象！"),
    INVALID_DATA_FIELD(500,"数据验证失败，无效的数据实体对象字段！"),
    SERVER_INTERNAL_ERROR(500,"服务器内部错误!"),
    UNAUTHORIZED_LOGIN(500,"当前用户尚未登录或登录已超时，请重新登录！"),
    NO_ACCESS_PERMISSION(500,"当前用户没有访问权限，请核对！"),
    NO_OPERATION_PERMISSION(500,"当前用户没有操作权限，请核对！"),
    REDIS_CACHE_ACCESS_TIMEOUT(500,"Redis缓存数据访问超时，请刷新后重试！"),
    REDIS_CACHE_ACCESS_STATE_ERROR(500,"Redis缓存数据访问状态错误，请刷新后重试！"),
    FLOW_SUBMIT_ERROR(500,"流程引擎调用失败");

    final Integer code;
    final String message;

    CommonExceptionEnum(Integer code, String message) {
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
