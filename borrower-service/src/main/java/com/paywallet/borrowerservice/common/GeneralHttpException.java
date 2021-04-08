package com.paywallet.borrowerservice.common;

import java.util.HashMap;
import java.util.HashSet;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.Getter;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class GeneralHttpException extends RuntimeException {

    private static final long serialVersionUID = -7201163830646384086L;
    private HashMap<String,String> errors = new HashMap<String,String>();
    public GeneralHttpException(String parameter,String message) {
        super(message);
        this.errors.put(parameter, message);
    }

    public GeneralHttpException() {
        super();
    }

    public GeneralHttpException append(String parameter,String message){
        this.errors.put(parameter, message);
        return this;
    }

    public GeneralHttpException clear(){
        this.errors.clear();
        return this;
    }

    public HashMap<String,String> getErrors(){
        return this.errors;
    }
}