package com.top.effitopia.dto;

import lombok.Data;

@Data
public class DailyUsageCreateDTO {

    private int memberId;
    private int warehouseId;
    private double totalArea;
    private double storageCostPerArea;
}
