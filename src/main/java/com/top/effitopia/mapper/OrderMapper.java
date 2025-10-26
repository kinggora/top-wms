package com.top.effitopia.mapper;

import com.top.effitopia.domain.*;
import com.top.effitopia.dto.OutboundSearchDTO;
import com.top.effitopia.dto.PageRequestDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {

    void insertOrder(Order order);

    void updateOrder(Order order);

    Order findById(Integer orderId);

//    List<Order> findAll(PageRequestDTO pageRequestDTO);
    List<Order> findAll(PageRequestDTO pageRequestDTO);

    int getOrderCount(PageRequestDTO pageRequestDTO);

    Outbound findOutboundByOrderId(Integer orderId);

    void insertOutbound(Outbound outbound);

    void updateOutbound(Outbound outbound);

    Dispatch findDispatchByOutboundId(Integer outboundId);

    Waybill findWaybillByOutboundId(Integer outboundId);

    Product findProductByStockId(Integer stockId);

    Stock findStockById(Integer stockId);

    Integer findOutboundBasicFeeByWarehouseId(Integer warehouseId);

    void insertWaybill(Waybill waybill);

    //
    Stock findStockById(int id);

    void insertTempStock(TempStock tempStock);
}
