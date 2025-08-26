package com.codexmaker.services.model.entity;


import lombok.Builder;

@Builder
public class DemandeCongeResponse {

    private Object response;
    private int status;
    private String message;

    public DemandeCongeResponse(){

    }

    public DemandeCongeResponse(Object response, int status, String message){
        this.response = response;
        this.message = message;
        this.status = status;
    }
    public Object getResponse() {
        return response;
    }

    public void setResponse(Object response) {
        this.response = response;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
