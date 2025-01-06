package com.ifarmr.entity;

import com.ifarmr.entity.enums.ItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "inventory_tbl")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Inventory extends BaseClass {

    @Column (nullable = false)
    @Enumerated (EnumType.STRING)
    private ItemType item;

    @Column (nullable = false)
    private String name;

    @Column (nullable = false)
    private String quantity;

    @Column (nullable = false)
    private BigDecimal cost;

    @Column(name = "location", length = 500)
    private String location;

    @Column(name = "condition", length = 500)
    private String condition;

    @Column(name = "date_acquired", length = 500)
    private LocalDate dateAcquired;

    @Column(name = "Description", length = 500)
    private String description;

    @Column (nullable = false)
    private String photoUpload;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
