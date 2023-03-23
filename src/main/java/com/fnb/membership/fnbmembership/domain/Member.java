package com.fnb.membership.fnbmembership.domain;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Random;
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

    protected Member() {
        this.createdAt = LocalDateTime.now();
    }

    private Member(String phone) {
        this.phone = phone;
        this.barcode = this.createBarcode();
        this.createdAt = LocalDateTime.now();
    }

    /**
     * 신규 Member 객체 생성 시 사용하는 메서드
     * 12자리 숫자형 문자열 Barcode를 임의로 생성해 Set
     * @param phone
     */
    public static Member createMember(String phone) {
        return new Member(phone);
    }

    private String createBarcode() {

        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        StringBuilder sb = new StringBuilder();

        for (int i=0; i<10; i++) {
            sb.append(Integer.toString(random.nextInt(10)));
        }

        return sb.toString();
    }
}
