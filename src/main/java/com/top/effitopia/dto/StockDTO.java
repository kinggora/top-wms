package com.top.effitopia.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 재고 DTO
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StockDTO {
    private int	id;	                    //재고ID
    private int	stockAmount;	        //재고수량
    private LocalDate manufacturingDate;//제조일자
    private LocalDate expirationDate;	//유통기한
    private LocalDate regDate;	        //재고생성일
    private LocalDate modDate;          //재고변동일
    private MemberDTO memberDTO;	    //회원Dto
    private CellDTO	cellDTO;	        //창고세부구역Dto
    private ProductDTO	productDTO;	    //상품Dto
}
