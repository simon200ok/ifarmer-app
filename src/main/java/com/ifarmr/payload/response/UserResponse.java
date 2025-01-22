package com.ifarmr.payload.response;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.ifarmr.entity.User;
import com.ifarmr.entity.enums.Gender;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;


import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

        private Long userId;

        private String fullName;

        private String businessName;

        private String email;

        @Enumerated(EnumType.STRING)
        private Gender gender;

//        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
//        private LocalDateTime dateCreated;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        private LocalDateTime createdAt;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        private LocalDateTime lastLogoutTime;

        private String username;

        private String displayPhoto;

}
