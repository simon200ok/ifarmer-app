package com.ifarmr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequestDto {

    private String firstName;

    private String lastName;
    private String email;
    private String userName;
    private String password;
    private String gender;
    private String role;
    private String displayPhoto;
    private String businessName;
    private String supportTickets;


}
