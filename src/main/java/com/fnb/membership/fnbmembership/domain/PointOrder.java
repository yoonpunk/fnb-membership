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

    private String brandName;

    private String storeName;

    @Enumerated(EnumType.STRING)
    private PointOrderType type;

    private Long requestedPointAmount;

    private LocalDateTime approvedAt;

    protected PointOrder() {
        this.approvedAt = LocalDateTime.now();
    }

    private PointOrder(Member member, String brandName, String storeName, PointOrderType type, Long requestedPointAmount) {
        this.member = member;
        this.brandName = brandName;
        this.storeName = storeName;
        this.type = type;
        this.requestedPointAmount = requestedPointAmount;
        this.approvedAt = LocalDateTime.now();
    }

    public static PointOrder createPointOrder(Member member, String brandName, String storeName, PointOrderType pointOrderType, Long requestedPointAmount) {
        return new PointOrder(member, brandName, storeName, pointOrderType, requestedPointAmount);
    }
}
