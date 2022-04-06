package com.javaspring.exception;

public class InvalidGameIdException extends RuntimeException{
    private String message;
    public InvalidGameIdException(String aMessage){
      this.message=aMessage;
    }

    public String getMessage(){
        return message;
    }
}
