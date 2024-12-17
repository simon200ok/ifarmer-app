package com.ifarmr.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {

    private String responseCode;
    private String responseMessage;
    private RegistrationInfo registrationInfo;
}
