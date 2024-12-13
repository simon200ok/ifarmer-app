package com.ifarmr.payload.response;

import lombok.Data;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

@Data
public class ApiResponse<T> {
    private String responseMessage;
    private T responseData;
    private String timestamp;
    private boolean success;

    public ApiResponse(String message, T data) {
        this.responseMessage = responseMessage;
        this.responseData = data;
        this.timestamp = convertDateToFormat();
        this.success = true;
    }

    public ApiResponse() {
        this.timestamp = convertDateToFormat();
        this.success = true;
    }

    private String convertDateToFormat() {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        LocalDateTime localDateTime = LocalDateTime.now();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat.format(localDateTime);
    }
}
