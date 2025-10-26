package com.top.effitopia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OutboundSearchDTO {
    private String region;
    private String productName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String outboundStatus;
}
