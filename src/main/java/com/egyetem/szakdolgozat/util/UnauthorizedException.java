package com.egyetem.szakdolgozat.util;

public class UnauthorizedException extends RuntimeException{

    public UnauthorizedException(String errorMsg){
        super(errorMsg);
    }

    public UnauthorizedException(){
        super();
    }
}
