package com.lifetime.common.response;

import com.lifetime.common.enums.CommonExceptionEnum;
import lombok.Data;

/**
 * @author:wangchao
 * @date: 2023/5/9-17:48
 * @description: com.sh3h.common.response
 * @Version:1.0
 */
@Data
public class ResponseResult<T> {
    /**
     * 为了优化性能，所有没有携带数据的正确结果，均可用该对象表示。
     */
    private static final ResponseResult<Void> OK = new ResponseResult<>();

    /**
     * 是否成功标记。
     */
    private Integer code = 200;
    private String message="成功";
    /**
     * 实际数据。
     */
    private T data = null;

    /**
     * 创建带有返回数据的成功对象。
     *
     * @param data 返回的数据对象
     * @return 返回创建的ResponseResult实例对象
     */
    public static <T> ResponseResult<T> success(T data) {
        ResponseResult<T> resp = new ResponseResult<>();
        resp.data = data;
        return resp;
    }

    /**
     * 创建带有返回数据的成功对象。
     *
     * @param data 返回的数据对象
     * @return 返回创建的ResponseResult实例对象
     */
    public static <T> ResponseResult<T> success(String message,T data) {
        ResponseResult<T> resp = new ResponseResult<>();
        resp.message=message;
        resp.data = data;
        return resp;
    }
    /**
     * 创建错误对象。
     *
     * @param data 返回的数据对象
     * @return 返回创建的ResponseResult实例对象
     */
    public static <T> ResponseResult<T> error(Integer errorCode, String errorMessage,T data) {
        return new ResponseResult<>(errorCode, errorMessage,data);
    }

    /**
     * 创建错误对象。
     * 如果返回错误对象，errorCode 和 errorMessage 分别取自于参数 errorCode 和参数 errorMessage。
     *
     * @param errorCode    自定义的错误码
     * @param errorMessage 自定义的错误信息
     * @return 返回创建的ResponseResult实例对象
     */
    public static <T> ResponseResult<T> error(Integer errorCode, String errorMessage) {
        return new ResponseResult<>(errorCode, errorMessage);
    }

    /**
     * 创建错误对象。
     * 如果返回错误对象，errorCode 和 errorMessage 分别取自于参数 errorCodeEnum 的 getCode() 和参数 errorMessage。
     *
     * @param errorCodeEnum 错误码枚举
     * @param errorMessage  自定义的错误信息
     * @return 返回创建的ResponseResult实例对象
     */
    public static <T> ResponseResult<T> error(CommonExceptionEnum errorCodeEnum, String errorMessage) {
        return error(errorCodeEnum.getCode(), errorMessage);
    }

    /**
     * 创建错误对象。
     * 如果返回错误对象，errorCode 和 errorMessage 分别取自于参数 errorCodeEnum 的 getCode() 和 getMessage()。
     *
     * @param errorCodeEnum 错误码枚举
     * @return 返回创建的ResponseResult实例对象
     */
    public static <T> ResponseResult<T> error(CommonExceptionEnum errorCodeEnum) {
        return error(errorCodeEnum.getCode(), errorCodeEnum.getMessage());
    }


    private ResponseResult() {
    }
    private ResponseResult(Integer errorCode, String errorMessage) {
        this.code = errorCode;
        this.message = errorMessage;
    }

    private ResponseResult(Integer errorCode, String errorMessage,T data) {
        this.code = errorCode;
        this.message = errorMessage;
        this.data=data;
    }

}
