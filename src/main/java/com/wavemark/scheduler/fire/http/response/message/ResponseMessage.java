package com.wavemark.scheduler.fire.http.response.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseMessage {

    private String type;
    private String description;
    private String cause;

    public ResponseMessage(String type, String description)
    {
        this.type = type;
        this.description = description;
    }
}