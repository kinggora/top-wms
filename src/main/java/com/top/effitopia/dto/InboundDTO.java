package com.top.effitopia.dto;

import com.top.effitopia.domain.Inbound;
import com.top.effitopia.domain.Member;
import com.top.effitopia.domain.Product;
import com.top.effitopia.domain.Warehouse;
import com.top.effitopia.enumeration.InboundStatus;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundDTO {

    private Integer Id;
    private MemberDTO memberDTO;
    private WarehouseDTO warehouseDTO;
    private ProductDTO productDTO;
    private VendorDTO vendorDTO;
    private LocalDate inboundRequestDate;
    private LocalDate inboundApprovedDate;
    private LocalDate inboundExpectDate;
    private LocalDate inboundCompletedDate;
    private InboundStatus inboundStatus;

    private Inbound toEntity(int memberId,int warehouseId, int productId) {
        return Inbound.builder()
            .id(this.Id)
            .member(Member.builder()
                .id(memberId)
                .build())
            .warehouse(Warehouse.builder()
                .id(warehouseId)
                .build())
            .product(Product.builder()
                .id(productId)
                .build())
            .inboundRequestDate(this.inboundRequestDate)
            .inboundApprovedDate(this.inboundApprovedDate)
            .inboundExpectDate(this.inboundExpectDate)
            .inboundCompletedDate(this.inboundCompletedDate)
            .inboundStatus(this.inboundStatus)
            .build();
    }

    private InboundDTO toDTO(Inbound inbound) {
        return InboundDTO.builder()
            .Id(inbound.getId())
            .memberDTO(MemberDTO.builder()
                .id(inbound.getMember().getId())
                .username(inbound.getMember().getUsername())
                .build())
            .warehouseDTO(WarehouseDTO.builder()
                .id(inbound.getWarehouse().getId())
                .build())
            .productDTO(ProductDTO.builder()
                .id(inbound.getProduct().getId())
                .build())
            .inboundRequestDate(inbound.getInboundRequestDate())
            .inboundApprovedDate(inbound.getInboundApprovedDate())
            .inboundExpectDate(inbound.getInboundExpectDate())
            .inboundCompletedDate(inbound.getInboundCompletedDate())
            .inboundStatus(inbound.getInboundStatus())
            .build();
    } // 모든 데이터를 넣기

}
