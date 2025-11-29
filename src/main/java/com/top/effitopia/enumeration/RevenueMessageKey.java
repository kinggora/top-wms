package com.top.effitopia.enumeration;

public enum RevenueMessageKey {

    CONTRACT_COST("revenue.details.contract_cost"),
    STORAGE_COST("revenue.details.storage_cost"),
    INBOUND_FEE("revenue.details.inbound_fee"),
    OUTBOUND_FEE("revenue.details.outbound_fee"),
    FREIGHT_COST("revenue.details.freight_cost");

    private final String key;

    RevenueMessageKey(String key) {
        this.key = key;
    }

    public String key() {
        return key;
    }
}
