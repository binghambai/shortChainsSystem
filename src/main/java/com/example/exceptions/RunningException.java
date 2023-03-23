package com.example.exceptions;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class RunningException extends RuntimeException{
    private String errCode = "";
    private Object data;
    private String result = "fail";

    public RunningException() {
        super();
        this.errCode = "K-000001";
    }

    public RunningException(String errCode, String result) {
        super();
        this.result = result;
        this.errCode = errCode;
    }
}
