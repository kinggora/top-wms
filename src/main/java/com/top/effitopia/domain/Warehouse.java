package com.top.effitopia.domain;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Warehouse {
    private Integer id;

    private Member member;

    private WarehouseType warehouseType;

    private String code;

    private String name;

    private String phone;

    private String zipCode;

    private String roadName;

    private String lotNumber;

    private String detailAddress;

    private int	width;

    private int	length;

    private int	height;

    private int	capacity;

    private double latitude;

    private double longitude;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    public Warehouse(int id) {
        this.id = id;
    }
}
