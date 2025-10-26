package com.top.effitopia.service;

import com.top.effitopia.dto.*;
import com.top.effitopia.enumeration.OutboundStatus;

import java.util.List;
import java.util.Optional;

public interface OutboundService {

    void registerOrder(OrderDTO orderDTO);

    void updateOrder(Integer id, OrderDTO orderDTO);

    void updateOrderStatus(Integer id, OutboundStatus status);

//    List<OrderDTO> getOrders(PageRequestDTO pageRequestDTO);
    PageResponseDTO<AllInOneDTO> getOrders(PageRequestDTO pageRequestDTO);

    Optional<DetailsDTO> getOrderDetails(Integer id);
}
