package com.top.effitopia.dto;

import lombok.Data;

@Data
public class DailyInOutboundDTO {

    private int warehouseId;
    private int memberId;
    private int inboundBoxCnt;
    private int outboundBoxCnt;
    private double inboundFeePerBox;
    private double outboundFeePerBox;
}
