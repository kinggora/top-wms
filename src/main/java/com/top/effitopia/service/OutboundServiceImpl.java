package com.top.effitopia.service;

import com.top.effitopia.domain.*;
import com.top.effitopia.dto.*;
import com.top.effitopia.enumeration.OutboundStatus;
import com.top.effitopia.enumeration.WaybillStatus;
import com.top.effitopia.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class OutboundServiceImpl implements OutboundService {

    private final OrderMapper orderMapper;
    private final ModelMapper modelMapper;

    @Override
    public void registerOrder(OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        orderMapper.insertOrder(order);

        OutboundDTO outboundDTO = OutboundDTO.builder()
                .orderDTO(orderDTO)
                .outboundStatus(OutboundStatus.PENDING)
                .regDate(LocalDateTime.now())
                .build();
        Outbound outbound = modelMapper.map(outboundDTO, Outbound.class);
        orderMapper.insertOutbound(outbound);
    }

    @Override
    public void updateOrder(Integer id, OrderDTO orderDTO) {
        Order order = modelMapper.map(orderDTO, Order.class);
        orderMapper.updateOrder(order);

        OutboundDTO outboundDTO = OutboundDTO.builder()
                .orderDTO(modelMapper.map(orderMapper.findById(id), OrderDTO.class))
                .modDate(LocalDateTime.now())
                .build();
        Outbound outbound = modelMapper.map(outboundDTO, Outbound.class);
        orderMapper.updateOutbound(outbound);
    }

    @Override
    public void updateOrderStatus(Integer id, OutboundStatus status) {
        Outbound outbound = orderMapper.findOutboundByOrderId(id);
        if (outbound != null) {
            OutboundDTO outboundDTO = OutboundDTO.builder()
                    .outboundStatus(status)
                    .modDate(LocalDateTime.now())
                    .build();
            outbound = modelMapper.map(outboundDTO, Outbound.class);
            orderMapper.updateOutbound(outbound);

            if (status == OutboundStatus.APPROVED) {
                WaybillDTO waybillDTO = WaybillDTO.builder()
                        .waybillNum(generateWaybillNum(outbound.getOutboundId()))
                        .waybillPrice(calculateWaybillPrice(outbound.getOrder().getOrderId()))
                        .regDate(LocalDateTime.now())
                        .estimatedArrivalDate(LocalDateTime.now().plusDays(3))
                        .waybillStatus(WaybillStatus.IN_TRANSIT)
                        .build();
                Waybill waybill = modelMapper.map(waybillDTO, Waybill.class);
                orderMapper.insertWaybill(waybill);

                Order order = orderMapper.findById(outbound.getOrder().getOrderId());
                Stock stock = orderMapper.findStockById(order.getStock().getId());
                if (order != null && stock != null) {
                    TempStock tempStock = TempStock.builder()
                            .changeAmount(order.getBuyerQuantity())
                            .manufacturingDate(stock.getManufacturingDate())
                            .expirationDate(stock.getExpirationDate())
                            .member(order.getMember())
                            .cell(stock.getCell())
                            .product(stock.getProduct())
                            .build();

                    orderMapper.insertTempStock(tempStock);
                }
            }
        }
    }

    private String generateWaybillNum(Integer outboundId) {
        return "WB-" + outboundId + "-" + LocalDateTime.now().toString();
    }

    private double calculateWaybillPrice(Integer orderId) {
        Order order = orderMapper.findById(orderId);
        if (order != null) {
            Integer stockId = order.getStock().getId();

            Stock stock = orderMapper.findStockById(stockId);
            if (stock != null) {
                Integer warehouseId = stock.getCell().getWarehouse().getId();

                Product product = orderMapper.findProductByStockId(stockId);

                Integer outboundBasicFee = orderMapper.findOutboundBasicFeeByWarehouseId(warehouseId);

                if (product != null && outboundBasicFee != null) {
                    return product.getProductWeight() * outboundBasicFee;
                }
            }
        }
        return 0.0;
    }

//    @Override
//    public List<OrderDTO> getOrders(PageRequestDTO pageRequestDTO) {
//        List<Order> orders = orderMapper.findAll(pageRequestDTO);
//        return orders.stream()
//                .map(order -> modelMapper.map(order, OrderDTO.class))
//                .collect(Collectors.toList());
//    }

    @Override
    public PageResponseDTO<AllInOneDTO> getOrders(PageRequestDTO pageRequestDTO) {
        List<Order> orders = orderMapper.findAll(pageRequestDTO);
        int total = orderMapper.getOrderCount(pageRequestDTO);
        List<AllInOneDTO> allInOneDTOList = new ArrayList<>();
        orders.forEach(order -> {
            MemberDTO memberDTO = MemberDTO.builder()
                    .id(order.getMember().getId())
                    .name(order.getMember().getName())
                    .email(order.getMember().getEmail())
                    .phone(order.getMember().getPhone())
                    .businessNumber(order.getMember().getBusinessNumber())
                    .build();

            ProductDTO productDTO = ProductDTO.builder()
                    .id(order.getStock().getProduct().getId())
                    .name(order.getStock().getProduct().getName())
                    .productBrand(order.getStock().getProduct().getProductBrand())
                    .productWeight(order.getStock().getProduct().getProductWeight())
                    .productStorageType(order.getStock().getProduct().getProductStorageType())
                    .build();

            StockDTO stockDTO = StockDTO.builder()
                    .id(order.getStock().getId())
                    .productDTO(productDTO)
                    .stockAmount(order.getStock().getStockAmount())
                    .expirationDate(order.getStock().getExpirationDate())
                    .manufacturingDate(order.getStock().getManufacturingDate())
                    .build();


            OutboundDTO outboundDTO = OutboundDTO.builder()
                    .outboundId((orderMapper.findOutboundByOrderId(order.getOrderId())).getOutboundId())
                    .orderDTO(modelMapper.map(order, OrderDTO.class))
                    .outboundStatus((orderMapper.findOutboundByOrderId(order.getOrderId())).getOutboundStatus())
                    .regDate((orderMapper.findOutboundByOrderId(order.getOrderId())).getRegDate())
                    .modDate((orderMapper.findOutboundByOrderId(order.getOrderId())).getModDate())
                    .build();

            OrderDTO orderDTO = OrderDTO.builder()
                    .orderId(order.getOrderId())
                    .buyerName(order.getBuyerName())
                    .zipCode(order.getZipCode())
                    .buyerRoadName(order.getBuyerRoadName())
                    .buyerLotNumber(order.getBuyerLotNumber())
                    .buyerDetailAddress(order.getBuyerDetailAddress())
                    .buyerLatitude(order.getBuyerLatitude())
                    .buyerLongitude(order.getBuyerLongitude())
                    .buyerQuantity(order.getBuyerQuantity())
                    .stockDTO(stockDTO)
                    .memberDTO(memberDTO)
                    .build();

            AllInOneDTO allInOneDTO = AllInOneDTO.builder()
                    .orderDTO(orderDTO)
                    .outboundDTO(outboundDTO)
                    .build();

            allInOneDTOList.add(allInOneDTO);
        });
        return new PageResponseDTO<>(pageRequestDTO, allInOneDTOList, total);
    }

    @Override
    public Optional<DetailsDTO> getOrderDetails(Integer id) {
        return Optional.ofNullable(orderMapper.findById(id))
                .map(order -> {
                    OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);

                    Outbound outbound = orderMapper.findOutboundByOrderId(order.getOrderId());
                    OutboundDTO outboundDTO = (outbound != null)
                            ? modelMapper.map(outbound, OutboundDTO.class)
                            : null;

                    Dispatch dispatch = (outbound != null)
                            ? orderMapper.findDispatchByOutboundId(outbound.getOutboundId())
                            : null;
                    DispatchDTO dispatchDTO = (dispatch != null)
                            ? modelMapper.map(dispatch, DispatchDTO.class)
                            : null;

                    Waybill waybill = (outbound != null)
                            ? orderMapper.findWaybillByOutboundId(outbound.getOutboundId())
                            : null;
                    WaybillDTO waybillDTO = (waybill != null)
                            ? modelMapper.map(waybill, WaybillDTO.class)
                            : null;

                    return DetailsDTO.builder()
                            .orderDTO(orderDTO)
                            .outboundDTO(outboundDTO)
                            .dispatchDTO(dispatchDTO)
                            .waybillDTO(waybillDTO)
                            .build();
                });
    }

}
