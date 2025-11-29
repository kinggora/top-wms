package com.top.effitopia.mapper;

import com.top.effitopia.domain.DailyUsage;
import com.top.effitopia.dto.DailyInOutboundDTO;
import com.top.effitopia.dto.DailyUsageCreateDTO;
import com.top.effitopia.dto.MonthlyUsageDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RevenueBatchMapper {

    int insert(DailyUsage dailyUsage);
    List<DailyUsageCreateDTO> selectDailyStockArea();
    List<DailyInOutboundDTO> selectDailyInboundOutboundSummary();
    List<DailyInOutboundDTO> selectDailyTotalFreightCost();
    List<MonthlyUsageDTO> selectMonthlyTotalStorageCost();
}
