package com.ifarmr.entity;


import com.ifarmr.entity.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "support_tbl")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class SupportTicket extends BaseClass{

    private String ticketTitle;

    private String ticketDescription;

    @Enumerated (EnumType.STRING)
    private TicketStatus ticketStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "farmer_id")
    private Farmer farmer;

}
