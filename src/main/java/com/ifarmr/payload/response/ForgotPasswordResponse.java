package com.ifarmr.payload.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class ForgotPasswordResponse {
    private String resetToken;
    private String responseCode;
    private String responseMessage;
}
