package com.ifarmr.payload.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PushSubscriptionDTO {

    private String endpoint;

    private String p256dh;

    private String auth;

    private Long userId;
}
