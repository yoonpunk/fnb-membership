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
 * F&B 회원 정보 Entity
 */
@Entity
@Getter
public class Member {

    /**
     * Member의 ID, 추후 해당 서비스의 scale-out을 대비하여 PK를 auto-increment가 아닌 uuid를 활용하여 pk를 관리
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "MEMBER_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(unique = true)
    private String phone;

    @Column(unique = true)
    private String barcode;

    private LocalDateTime createdAt;

}
