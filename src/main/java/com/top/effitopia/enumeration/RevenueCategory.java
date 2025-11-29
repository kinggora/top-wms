package com.top.effitopia.enumeration;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum RevenueCategory {
    CONTRACT_COST("창고 계약비"),
    STORAGE_COST("창고 보관료"),
    INBOUND_FEE("입고 수수료"),
    OUTBOUND_FEE("출고 수수료"),
    FREIGHT_COST("운임료");

    public static final Map<String, String> CATEGORY_MAP = Collections.unmodifiableMap(
            Stream.of(values()).collect(Collectors.toMap(RevenueCategory::getValue, RevenueCategory::name)));
    private final String value;

    RevenueCategory(String value) {
        this.value = value;
    }
}
