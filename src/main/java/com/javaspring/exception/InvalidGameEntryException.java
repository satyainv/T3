package com.javaspring.exception;

public class InvalidGameEntryException extends RuntimeException{
    private String message;
    public InvalidGameEntryException(String aMessage){
        this.message=aMessage;
    }

    public String getMessage(){
        return message;
    }
}
