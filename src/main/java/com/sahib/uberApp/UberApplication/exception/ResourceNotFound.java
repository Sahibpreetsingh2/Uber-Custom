package com.sahib.uberApp.UberApplication.exception;

public class ResourceNotFound extends RuntimeException{

    public ResourceNotFound(String name){
        super(name);
    }

}
