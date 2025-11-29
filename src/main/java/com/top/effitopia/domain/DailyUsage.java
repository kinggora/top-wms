package com.top.effitopia.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyUsage {

    private Integer id;
    private Integer memberId;
    private Integer warehouseId;
    private double totalArea;
    private double storageCostPerArea;
    private LocalDate baseDate;
    private LocalDate regDate;

}
