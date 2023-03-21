package com.example.shortchainssystem.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> implements Serializable {

    private String msg;
    private Integer code;
    private T data;

    private static final int ERROR_CODE = 500;
    private static final int SUCCESS_CODE = 200;
    private static final String MSG = "操作失败";

    public static <T> BaseResponse<T> success(T context) {
        return new BaseResponse<>(MSG, SUCCESS_CODE, context);
    }

    public static <T> BaseResponse<T> failed(String msg) {
        return new BaseResponse<>(msg, ERROR_CODE, null);
    }

}
