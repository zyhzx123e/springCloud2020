package com.atguigu.springcloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN,reason = "Too Many Users")
public class UserTooManyException extends RuntimeException{
    public UserTooManyException(String message){
        super(message);
    }
}
