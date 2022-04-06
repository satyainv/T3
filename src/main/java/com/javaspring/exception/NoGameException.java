package com.javaspring.exception;

public class NoGameException extends RuntimeException{
    private String message;
    public NoGameException(String aMessage){
        this.message=aMessage;
    }

    public String getMessage(){
        return message;
    }
}
