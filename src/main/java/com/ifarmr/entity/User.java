package com.ifarmr.entity;

import com.ifarmr.entity.enums.Gender;
import com.ifarmr.entity.enums.Roles;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "user_tbl")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseClass implements UserDetails, Serializable {

    @NotBlank(message = "First Name is required")
    @Size(min = 3, max = 50, message = "First name must be between 3 and 50 Characters")
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(min = 3, max = 50, message = "Last name must be between 3 and 50 Characters")
    private String lastName;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 Characters")
    private String userName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid Email Address")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no spaces"
    )
    private String password;

    @Column(name = "last_login_time")
    private LocalDateTime lastLoginTime;

    @Column(name = "last_logout_time")
    private LocalDateTime lastLogoutTime;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Roles role;

    private String displayPhoto;


    @Column(nullable = false)
    private String businessName;

    @Column(nullable = false)
    private boolean isActive = false;

    @Column
    private String resetPasswordToken;

    @Column
    private LocalDateTime resetTokenExpiry;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<SupportTicket> supportTickets;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CropDetails> crops;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<AnimalDetails> animals;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Inventory> inventory;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private TokenVerification tokenVerification;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PushSubscription> pushSubscriptions;


    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notifications> notifications;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}
