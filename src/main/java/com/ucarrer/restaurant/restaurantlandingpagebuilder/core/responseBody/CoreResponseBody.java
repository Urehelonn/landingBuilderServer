package com.ucarrer.restaurant.restaurantlandingpagebuilder.core.responseBody;

public class CoreResponseBody<T> {
    private T result;
    private Exception error;

    public CoreResponseBody(T result,  String message, Exception error){
        this.result=result;
        this.message=message;
        this.error=error;
    }

    public Exception getError() {
        return error;
    }

    public void setError(Exception error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;


    public T getResult(){
        return result;
    }

    public T setResult(T result){
        this.result = result;
        return this.result;
    }




}
