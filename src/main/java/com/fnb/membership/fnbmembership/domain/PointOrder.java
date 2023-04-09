package com.fnb.membership.fnbmembership.domain;

import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * 포인트 적립/사용 주문 내역 Entity
 */
@Entity
@Getter
public class PointOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINT_ORDER_ID")
    private Long id;

    @Column(name = "POINT_ORDER_UUID", columnDefinition = "BINARY(16)", unique = true)
    private UUID uuid;

    private Long memberId;

    private String brandName;

    private String storeName;

    @Enumerated(EnumType.STRING)
    private PointOrderType type;

    private Long requestedPointAmount;

    private LocalDateTime approvedAt;

    protected PointOrder() {

    }

    private PointOrder(UUID uuid, Long memberId, String brandName, String storeName, PointOrderType type, Long requestedPointAmount, LocalDateTime approvedAt) {
        this.uuid = uuid;
        this.memberId = memberId;
        this.brandName = brandName;
        this.storeName = storeName;
        this.type = type;
        this.requestedPointAmount = requestedPointAmount;
        this.approvedAt = approvedAt;
    }

    /**
     * Creates a pointOrder object with auto-generated UUID.
     * @param uuid
     * @param memberId
     * @param brandName
     * @param storeName
     * @param pointOrderType
     * @param requestedPointAmount
     * @param approvedAt
     * @return
     */
    public static PointOrder createPointOrder(
            @NotNull UUID uuid,
            @NotNull Long memberId,
            @NotEmpty String brandName,
            @NotEmpty String storeName,
            @NotNull PointOrderType pointOrderType,
            @NotNull Long requestedPointAmount,
            @NotNull LocalDateTime approvedAt
    ) {
        return new PointOrder(uuid, memberId, brandName, storeName, pointOrderType, requestedPointAmount, approvedAt);
    }

    // Using UUID for comparing equality of objects.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Brand brand = (Brand) o;
        return Objects.equals(getUuid(), brand.getUuid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUuid());
    }
}
