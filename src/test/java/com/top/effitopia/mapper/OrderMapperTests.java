package com.top.effitopia.mapper;

import com.top.effitopia.domain.*;
import com.top.effitopia.dto.*;
import com.top.effitopia.enumeration.OutboundStatus;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Log4j2
@Transactional
@Rollback
public class OrderMapperTests {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MemberMapper memberMapper;

    private OrderDTO testOrderDTO;

//    @BeforeEach
//    void setup() {
//        Stock stock = orderMapper.findStockById(1);
//        assertThat(stock).isNotNull();
//        testOrderDTO = OrderDTO.builder()
//                .buyerName("John Doe")
//                .zipCode("12345")
//                .buyerRoadName("123 Test St")
//                .buyerLotNumber("Test Lot")
//                .buyerDetailAddress("Apt 101")
//                .buyerLatitude(37.123456)
//                .buyerLongitude(127.123456)
//                .buyerQuantity(10)
//                .memberDTO(MemberDTO.builder().id(1).build())
//                .stockDTO(modelMapper.map(stock, StockDTO.class))
//                .build();
//    }

    @Test
    void testInsertOrder() {
//        Stock stock = orderMapper.findStockById(1);
//        log.info("sdlkajlajhdaljhaljhdaljhaljahlakjhdalkjhdlakjhlkajhldakhdalkjhadldjashldakjhadslkdhs" + stock);
//        assertThat(stock).isNotNull();
//        testOrderDTO = OrderDTO.builder()
//                .buyerName("John Doe")
//                .zipCode("12345")
//                .buyerRoadName("123 Test St")
//                .buyerLotNumber("Test Lot")
//                .buyerDetailAddress("Apt 101")
//                .buyerLatitude(37.123456)
//                .buyerLongitude(127.123456)
//                .buyerQuantity(10)
//                .memberDTO(MemberDTO.builder().id(1).build())
//                .stockDTO(modelMapper.map(stock, StockDTO.class))
//                .build();
//        log.info("OrderMapperTests testInsertOrder");
//
//        log.info(testOrderDTO.toString() + "-------------------------------------------");
//
//        Order order = modelMapper.map(testOrderDTO, Order.class);
//        orderMapper.insertOrder(order);

        Stock stock = Stock.builder()
                .id(1)
                .build();

        Member member = Member.builder()
                .id(21)
                .build();

        Order order = Order.builder()
                .buyerName("Test Buyer")
                .zipCode("12345")
                .buyerRoadName("Test Road")
                .buyerLotNumber("123")
                .buyerDetailAddress("Test Address")
                .buyerLatitude(37.1)
                .buyerLongitude(127.1)
                .buyerQuantity(10)
                .stock(stock)
                .member(member)
                .build();

        orderMapper.insertOrder(order);
        assertThat(order.getOrderId()).isNotNull();

        assertThat(order.getOrderId()).isNotNull();
        log.info("Inserted Order ID: {}", order.getOrderId());
    }

    @Test
    void testFindById() {
        log.info("OrderMapperTests testFindById");

        Order order = orderMapper.findById(1);
        assertThat(order).isNotNull();
        assertThat(order.getOrderId()).isEqualTo(1);

        log.info("Found Order: {}", order);
    }

//    @Test
//    void testFindAll() {
//        log.info("OrderMapperTests testFindAll");
//
//        List<Order> orders = orderMapper.findAll(new PageRequestDTO());
//        assertThat(orders).isNotEmpty();
//        log.info("Total Orders: {}", orders.size());
//    }

    @Test
    void testFindAllWithSearch() {
        log.info("OrderMapperTests testFindAllWithSearch");

        PageRequestDTO pageRequestDTO = new PageRequestDTO();

        OutboundSearchDTO searchDTO = OutboundSearchDTO.builder()
                .region("Seoul")
                .productName("ProductA")
                .startDate(LocalDateTime.of(2023, 1, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                .outboundStatus("PENDING")
                .build();

        List<Order> orders = orderMapper.findAll(pageRequestDTO);

        assertThat(orders).isNotEmpty();
        log.info("Total Orders with search criteria: {}", orders.size());

        orders.forEach(order -> {
            assertThat(order.getBuyerName()).isNotNull();
            assertThat(order.getStock().getProduct().getName()).isEqualTo("ProductA");
        });
    }


    @Test
    void testUpdateOrder() {
        log.info("OrderMapperTests testUpdateOrder");

        Order order = orderMapper.findById(2);
        assertThat(order).isNotNull();

        OrderDTO updateOrderDTO = OrderDTO.builder()
                .orderId(order.getOrderId())
                .buyerName("Jane Doe")
                .zipCode(order.getZipCode())
                .buyerRoadName(order.getBuyerRoadName())
                .buyerLotNumber(order.getBuyerLotNumber())
                .buyerDetailAddress(order.getBuyerDetailAddress())
                .buyerLatitude(order.getBuyerLatitude())
                .buyerLongitude(order.getBuyerLongitude())
                .buyerQuantity(order.getBuyerQuantity())
                .stockDTO(modelMapper.map(order.getStock(), StockDTO.class))
                .build();

        Order updatedOrder = modelMapper.map(updateOrderDTO, Order.class);

        orderMapper.updateOrder(updatedOrder);

        Order resultOrder = orderMapper.findById(2);
        assertThat(resultOrder.getBuyerName()).isEqualTo("Jane Doe");
        log.info("Updated Order: {}", resultOrder);
    }

    @Test
    void testInsertOutbound() {
        log.info("OrderMapperTests testInsertOutbound");

        Outbound outbound = Outbound.builder()
                .order(Order.builder().orderId(1).build())
                .outboundStatus(OutboundStatus.PENDING)
                .regDate(LocalDateTime.now())
                .build();

        orderMapper.insertOutbound(outbound);
        assertThat(outbound.getOutboundId()).isNotNull();
        log.info("Inserted Outbound ID: {}", outbound.getOutboundId());
    }
}
