package com.example.springinaction.tacoapp.core.domain.entity;

public enum OrderStatus {
    CREATED (CustomerStatus.PREPARING),
    COOKING (CustomerStatus.PREPARING),
    READY (CustomerStatus.PREPARING),
    DELIVERING (CustomerStatus.SHIPPED),
    DELIVERED (CustomerStatus.ARRIVED),
    CANCELLED(CustomerStatus.VOIDED);

    private final CustomerStatus customerStatus;

    OrderStatus(CustomerStatus customerStatus) {
        this.customerStatus = customerStatus;
    }

    public CustomerStatus toCustomerStatus() {
        return this.customerStatus;
    }
}

