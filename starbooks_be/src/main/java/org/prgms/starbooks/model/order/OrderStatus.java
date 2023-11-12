package org.prgms.starbooks.model.order;

public enum OrderStatus {
    ACCEPTED("주문 완료"),
    READY_FOR_DELIVERY("배송 준비중"),
    SHIPPED("배송중"),
    SETTLED("배송 완료"),
    CANCELED("주문 취소");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
