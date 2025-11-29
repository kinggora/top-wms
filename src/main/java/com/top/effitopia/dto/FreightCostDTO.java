package com.top.effitopia.dto;

import lombok.Data;

@Data
public class FreightCostDTO {

    private int warehouseId;
    private int memberId;
    private int waybillCnt;
    private double totalFreightCost;
}
