package com.fnb.membership.fnbmembership.domain;

import lombok.Getter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 점포 정보 Entity
 */
@Entity
@Getter
public class Store {

    /**
     * Store ID, 추후 해당 서비스의 scale-out을 대비하여 PK를 auto-increment가 아닌 uuid를 활용하여 pk를 관리
     */
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name="uuid2", strategy = "uuid2")
    @Column(name = "STORE_ID", columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BRAND_ID")
    private Brand brand;

    @Column(name = "STORE_NAME")
    private String name;
    private LocalDateTime createdAt;
}
