package com.ifarmr.payload.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommentResponse {

    private String responseCode;

    private String responseMessage;

    private CommentInfo commentInfo;
}
