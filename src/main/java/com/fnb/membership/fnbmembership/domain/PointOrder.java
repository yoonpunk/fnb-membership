package com.fnb.membership.fnbmembership.domain;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 포인트 적립/사용 주문 내역 Entity
 */
@Entity
@Getter
public class PointOrder {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "POINT_ORDER_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Enumerated(EnumType.STRING)
    private PointOrderType type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "STORE_ID")
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POINT_ID")
    private Point point;

    private Long requestedPointAmount;

    private LocalDateTime approvedAt;

    protected PointOrder() {

    }

    private PointOrder(Member member, PointOrderType type, Store store, Point point, Long requestedPointAmount) {
        this.member = member;
        this.type = type;
        this.store = store;
        this.point = point;
        this.requestedPointAmount = requestedPointAmount;
    }

    public static PointOrder createPointOrder(Member member, PointOrderType pointOrderType, Store store, Point point, Long requestedPointAmount) {
        return new PointOrder(member, pointOrderType, store, point, requestedPointAmount);
    }
}
