package com.fnb.membership.fnbmembership.domain;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 적립금 정보 Table
 */
@Entity
@Getter
public class Point {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "POINT_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID")
    private Brand brand;

    private Long amount;

    private LocalDateTime createdAt;

    @Version
    private Long version;

    protected Point() {
        this.createdAt = LocalDateTime.now();
    }

    public Point(Member member, Brand brand, Long amount) {
        this.member = member;
        this.brand = brand;
        this.amount = amount;
        this.createdAt = LocalDateTime.now();
    }

    /**
     * Point 최초 생성 메서드
     * Member, Brand 가 null이거나 amount가 0보다 작으면 IllegalArgumentException 발생
     * @param member
     * @param brand
     * @param amount
     * @return Point
     * @throws IllegalArgumentException
     */
    public static Point createPoint(Member member, Brand brand, long amount) throws IllegalArgumentException {
        if (member == null || brand == null || amount < 0) {
            throw new IllegalArgumentException();
        }
        return new Point(member, brand, amount);
    }

    /**
     * 포인트 사용 시, 적립된 포인트가 충분한지 체크하는 메서드
     * @param useAmount
     * @return boolean True: 충분, False: 불충분
     */
    public boolean isEnoughToUse(long useAmount) {
        if (amount - useAmount >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 전달된 사용금액만큼 포인트를 차감
     * @param useAmount
     * @return 사용 후 남은 금액
     */
    public Long usePoint(long useAmount) {
        amount -= useAmount;
        return amount;
    }

    /**
     * 전달된 적립금액만큼 포인트를 적립
     * @param earnAmount
     * @return 적립 후 남은 금액
     */
    public Long earnPoint(long earnAmount) {
        amount += earnAmount;
        return amount;
    }
}
