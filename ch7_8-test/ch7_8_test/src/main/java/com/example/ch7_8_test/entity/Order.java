package com.example.ch7_8_test.entity;

import com.example.ch7_8_test.constant.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private LocalDateTime orderDate; //주문일

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus; //주문상태

    // 현재, 주문, 엔티티 클래스, 멤버로 , 주문이된 상품들의 리스트 가지고 있다.
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL
            , orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    // 주문 -> 주문_상품을 추가하는 메서드.
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 주문을 만드는 과정.
    // 준비물)
    // 1. 구매자, 2. 주문이된 상품,
    public static Order createOrder(Member member, List<OrderItem> orderItemList) {
        // 각 각의 주문,
        Order order = new Order();
        // 1. 구매자 추가.
        order.setMember(member);
        // 2. 주문이된 상품을, 주문에 추가하는 반복문.
        for(OrderItem orderItem : orderItemList) {
            order.addOrderItem(orderItem);
        }
        // 3. 주무의 상태를 , 주문중 -> 배송중 -> 결제 완료.
        order.setOrderStatus(OrderStatus.ORDER);
        // 4. 주문 일자.
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 구매한 총 상품의 가격.
    public int getTotalPrice() {
        // 임시 상태 변수 , 전체 가격을 나타내기 위한.
        int totalPrice = 0;
        // 주문_상품의 내용물에서 가격을 가져와서, 누적 합산.
        for(OrderItem orderItem : orderItems){
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    // 주문취소, 재고 수량을 원래대로 복귀하는 과정도 이 안에 로직이 추가.
    public void cancelOrder() {
        this.orderStatus = OrderStatus.CANCEL;
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

}