package com.top.effitopia.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MonthlyUsageDTO {

    private int warehouseId;
    private int memberId;
    private double totalStorageCost;
    private LocalDate baseDate;
}
