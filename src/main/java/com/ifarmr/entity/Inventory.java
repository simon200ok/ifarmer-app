package com.ifarmr.entity;

import com.ifarmr.entity.enums.ItemType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
    private int quantity;

    @Column (nullable = false)
    private BigDecimal price;

    @Column (nullable = false)
    private String photoUpload;

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

}
