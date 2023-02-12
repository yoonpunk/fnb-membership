package com.fnb.membership.fnbmembership.domain;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 포인트 적립/사용 내역 정보 Entity
 * 해당 테이블은 이력성 테이블로 Member, Brand, Store의 정보를 포함하지만 연관관계를 맺지 않고 사용
 * (위 세 정보의 생성,수정,삭제 시 제약조건으로부터 자유롭게 사용하기 위함)
 * Insert와 Select만 이루어지는 테이블
 */
@Entity
@Getter
public class PointHistory {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @Column(name = "POINT_HISTORY_ID", columnDefinition = "BINARY(16)")
    private UUID id;
    private PointHistoryType type;
    private String brandName;
    private String storeName;
    private LocalDateTime approvedAt;
}
