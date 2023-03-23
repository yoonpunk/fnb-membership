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
 * 브랜드 정보 Entity
 */
@Entity
@Getter
public class Brand {

    /**
     * Brand ID, 추후 해당 서비스의 scale-out을 대비하여 PK를 auto-increment가 아닌 uuid를 활용하여 pk를 관리
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "BRAND_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "BRAND_NAME", unique = true)
    private String name;

    private LocalDateTime createdAt;

    protected Brand() {
        this.createdAt = LocalDateTime.now();
    }

    private Brand(String name) {

        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    public static Brand createBrand(String name) {
        return new Brand(name);
    }

}
