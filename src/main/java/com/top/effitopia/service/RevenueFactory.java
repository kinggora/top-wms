package com.top.effitopia.service;

import com.top.effitopia.domain.Member;
import com.top.effitopia.domain.Revenue;
import com.top.effitopia.domain.Warehouse;
import com.top.effitopia.dto.DailyInOutboundDTO;
import com.top.effitopia.dto.FreightCostDTO;
import com.top.effitopia.dto.MonthlyUsageDTO;
import com.top.effitopia.enumeration.RevenueCategory;
import com.top.effitopia.enumeration.RevenueMessageKey;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class RevenueFactory {

    private final MessageSource messageSource;

    public Revenue createStorageCost(MonthlyUsageDTO dto, LocalDate date) {
        Object[] messageParams = new Object[]{
                date.getYear(),
                date.getDayOfMonth()
        };
        String details = getMessage(RevenueMessageKey.STORAGE_COST, messageParams);
        return Revenue.builder()
                .warehouse(Warehouse.builder().id(dto.getWarehouseId()).build())
                .member(Member.builder().id(dto.getWarehouseId()).build())
                .category(RevenueCategory.STORAGE_COST)
                .amount(dto.getTotalStorageCost())
                .revenueDetails(details)
                .regDate(LocalDateTime.now())
                .requestDate(LocalDateTime.now())
                .paid(false)
                .deleted(false)
                .build();
    }

    public Revenue createInboundFee(DailyInOutboundDTO dto, LocalDate date) {
        Object[] messageParams = new Object[]{
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth(),
                dto.getInboundBoxCnt(),
                dto.getInboundFeePerBox()
        };
        String details = getMessage(RevenueMessageKey.INBOUND_FEE, messageParams);
        return Revenue.builder()
                .warehouse(Warehouse.builder().id(dto.getWarehouseId()).build())
                .member(Member.builder().id(dto.getWarehouseId()).build())
                .category(RevenueCategory.INBOUND_FEE)
                .amount(dto.getInboundBoxCnt() * dto.getInboundFeePerBox())
                .revenueDetails(details)
                .regDate(LocalDateTime.now())
                .requestDate(LocalDateTime.now())
                .paid(false)
                .deleted(false)
                .build();
    }

    public Revenue createOutboundFee(DailyInOutboundDTO dto, LocalDate date) {
        Object[] messageParams = new Object[]{
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth(),
                dto.getOutboundBoxCnt(),
                dto.getOutboundFeePerBox()
        };
        String details = getMessage(RevenueMessageKey.OUTBOUND_FEE, messageParams);
        return Revenue.builder()
                .warehouse(Warehouse.builder().id(dto.getWarehouseId()).build())
                .member(Member.builder().id(dto.getWarehouseId()).build())
                .category(RevenueCategory.OUTBOUND_FEE)
                .amount(dto.getOutboundBoxCnt() * dto.getOutboundFeePerBox())
                .revenueDetails(details)
                .regDate(LocalDateTime.now())
                .requestDate(LocalDateTime.now())
                .paid(false)
                .deleted(false)
                .build();
    }


    public Revenue createFreightCost(FreightCostDTO dto, LocalDate date) {
        Object[] messageParams = new Object[]{
                date.getYear(),
                date.getMonthValue(),
                date.getDayOfMonth(),
                dto.getWaybillCnt()
        };
        String details = getMessage(RevenueMessageKey.FREIGHT_COST, messageParams);
        return Revenue.builder()
                .warehouse(Warehouse.builder().id(dto.getWarehouseId()).build())
                .member(Member.builder().id(dto.getWarehouseId()).build())
                .category(RevenueCategory.FREIGHT_COST)
                .amount(dto.getTotalFreightCost())
                .revenueDetails(details)
                .regDate(LocalDateTime.now())
                .requestDate(LocalDateTime.now())
                .paid(false)
                .deleted(false)
                .build();
    }

    private String getMessage(RevenueMessageKey messageKey, Object... args) {
        return messageSource.getMessage(messageKey.key(), args, Locale.KOREA);
    }
}
