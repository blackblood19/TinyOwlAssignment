package com.example.chaitanya.tinyowlassignment;

/**
 * Created by Chaitanya on 9/26/2015.
 */
public class post {
    private String mMessage;
    private String mNodeId;

    post(String message, String nodeId){
        this.mMessage = message;
        this.mNodeId = nodeId;
    }

    String getMessage() {return mMessage;}
    String getNodeId()  {return mNodeId;}
}
