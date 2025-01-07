package com.ifarmr.payload.request;

import lombok.Data;

@Data
public class UpdateUserRequestDto{
    private String firstName;
    private String lastName;
    private String userName;
    private String email;
    private String password;
    private String businessName;
    private String displayPhoto;
}
