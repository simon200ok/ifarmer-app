package com.ifarmr.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table (name = "userProfile_tbl")
public class UserProfile extends BaseClass{

    @Column(nullable = false)
    private String userName;

    private String displayPhoto;

    @Column(nullable = false)
    private String BusinessName;

    @OneToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_Id")
    private Farmer farmer;

}
