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

    @Column(columnDefinition = "BINARY(16)")
    private UUID brandId;

    @Column(columnDefinition = "BINARY(16)")
    private UUID storeId;

    @Enumerated(EnumType.STRING)
    private PointOrderType type;

    private Long requestedPointAmount;

    private LocalDateTime approvedAt;

    protected PointOrder() {

    }

    private PointOrder(Member member, UUID brandId, UUID storeId, PointOrderType type, Long requestedPointAmount) {
        this.member = member;
        this.brandId = brandId;
        this.storeId = storeId;
        this.type = type;
        this.requestedPointAmount = requestedPointAmount;
    }

    public static PointOrder createPointOrder(Member member, UUID brandId, UUID storeId, PointOrderType pointOrderType, Long requestedPointAmount) {
        return new PointOrder(member, brandId, storeId, pointOrderType, requestedPointAmount);
    }
}
