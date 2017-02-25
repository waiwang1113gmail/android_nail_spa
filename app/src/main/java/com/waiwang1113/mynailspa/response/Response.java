package com.waiwang1113.mynailspa.response;

import com.waiwang1113.mynailspa.entities.Entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Weige on 2/20/17.
 */

public class Response<T extends Entity> {
    private ResponseCode responseCode;

    private List<String> responseMessage;
    private List<T> payload;

    public Response() {
        responseMessage = new ArrayList<>();
        payload = new ArrayList<>();
    }

    public List<String> getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(List<String> responseMessage) {
        this.responseMessage = responseMessage;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(ResponseCode responseCode) {
        this.responseCode = responseCode;
    }

    public List<T> getPayload() {
        return payload;
    }

    public void setPayload(List<T> payload) {
        this.payload = payload;
    }
    public static enum ResponseCode {
        FAIL,SUCCESS;
    }
    public void addMessage(String msg){
        this.responseMessage.add(msg);
    }
    public static <D extends Entity> Response<D> getPayloadResponse(List<D> entities){
        Response<D> newRes = new Response<>();
        newRes.setResponseCode(ResponseCode.SUCCESS);
        newRes.setPayload(entities);
        return newRes;
    }
    public static <D extends Entity> Response<D> getEmptyResponse(ResponseCode code){
        Response<D> newRes = new Response<>();
        newRes.setResponseCode(code);
        return newRes;
    }
    public static <D extends Entity> Response<D> getEmptyResponse(ResponseCode code,String ...msgs){
        Response<D> newRes = new Response<>();
        newRes.setResponseCode(code);
        for(String msg:msgs){
            newRes.addMessage(msg);
        }
        return newRes;
    }
}
