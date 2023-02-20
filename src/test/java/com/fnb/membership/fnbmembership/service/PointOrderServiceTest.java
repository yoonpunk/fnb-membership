package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.*;
import com.fnb.membership.fnbmembership.dto.*;
import com.fnb.membership.fnbmembership.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointOrderServiceTest {

    private PointOrderService pointOrderService;

    @Autowired
    private PointOrderRepository pointOrderRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PointRepository pointRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private BrandRepository brandRepository;

    private Member member;
    private Brand bakery;
    private Store moonbucks;
    private Point point;

    @BeforeEach
    public void init() {

        pointOrderService = new PointOrderService(pointOrderRepository, memberRepository, pointRepository, storeRepository);

        member = Member.createMember("01012345678");
        memberRepository.save(member);

        bakery = Brand.createBrand("BAKERY");
        brandRepository.save(bakery);

        moonbucks = Store.createStore("MOONBUCKS", bakery);
        storeRepository.save(moonbucks);

        point = Point.createPoint(member, bakery, 5000);
        pointRepository.save(point);

    }

    @Test
    public void createEarnPointOrder_성공() {

        // given
        CheckedMemberDto checkedMemberDto = CheckedMemberDto.builder()
                .id(member.getId().toString())
                .phone(member.getPhone())
                .barcode(member.getBarcode())
                .build();

        CheckedStoreDto checkedStoreDto = CheckedStoreDto.builder()
                .brandName(bakery.getName())
                .brandId(bakery.getId().toString())
                .storeName(moonbucks.getName())
                .storeId(moonbucks.getId().toString())
                .build();

        EarnPointResultDto earnPointResultDto = EarnPointResultDto.builder()
                .pointId(point.getId().toString())
                .remainedAmount(point.getAmount())
                .requestedAmount(3000l)
                .brandId(bakery.getId().toString())
                .memberId(member.getId().toString())
                .pointId(point.getId().toString())
                .isSuccess(true)
                .build();

        // when
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createEarnPointOrder(
                checkedMemberDto, checkedStoreDto, earnPointResultDto);

        // then
        Optional<PointOrder> result = pointOrderRepository.findById(UUID.fromString(createPointOrderResultDto.getPointOrderId()));

        assertThat(result).isPresent();
        assertThat(result.get().getMember().getId()).isEqualTo(member.getId());
        assertThat(result.get().getStore().getId()).isEqualTo(moonbucks.getId());
        assertThat(result.get().getPoint().getId()).isEqualTo(point.getId());
        assertThat(result.get().getRequestedPointAmount()).isEqualTo(3000l);
        assertThat(result.get().getType()).isEqualTo(PointOrderType.EARN);

        assertThat(createPointOrderResultDto.getPointOrderId()).isEqualTo(result.get().getId().toString());
        assertThat(createPointOrderResultDto.getRequestedPointAmount()).isEqualTo(3000l);
    }

    @Test
    public void createUsePointOrder_성공() {

        // given
        CheckedMemberDto checkedMemberDto = CheckedMemberDto.builder()
                .id(member.getId().toString())
                .phone(member.getPhone())
                .barcode(member.getBarcode())
                .build();

        CheckedStoreDto checkedStoreDto = CheckedStoreDto.builder()
                .brandName(bakery.getName())
                .brandId(bakery.getId().toString())
                .storeName(moonbucks.getName())
                .storeId(moonbucks.getId().toString())
                .build();

        UsePointResultDto usePointResultDto = UsePointResultDto.builder()
                .pointId(point.getId().toString())
                .remainedAmount(point.getAmount())
                .requestedAmount(2000l)
                .brandId(bakery.getId().toString())
                .memberId(member.getId().toString())
                .pointId(point.getId().toString())
                .isSuccess(true)
                .build();

        // when
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createUsePointOrder(
                checkedMemberDto, checkedStoreDto, usePointResultDto);

        // then
        Optional<PointOrder> result = pointOrderRepository.findById(UUID.fromString(createPointOrderResultDto.getPointOrderId()));

        assertThat(result).isPresent();
        assertThat(result.get().getMember().getId()).isEqualTo(member.getId());
        assertThat(result.get().getStore().getId()).isEqualTo(moonbucks.getId());
        assertThat(result.get().getPoint().getId()).isEqualTo(point.getId());
        assertThat(result.get().getRequestedPointAmount()).isEqualTo(2000l);
        assertThat(result.get().getType()).isEqualTo(PointOrderType.USE);

        assertThat(createPointOrderResultDto.getPointOrderId()).isEqualTo(result.get().getId().toString());
        assertThat(createPointOrderResultDto.getRequestedPointAmount()).isEqualTo(2000l);
    }

}