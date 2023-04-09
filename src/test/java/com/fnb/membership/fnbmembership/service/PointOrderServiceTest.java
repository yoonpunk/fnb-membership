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

import java.time.LocalDateTime;
import java.util.List;
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
    private Brand moonbucks;
    private Store moonbucksMetan;
    private Point point;

    @BeforeEach
    public void init() {

        pointOrderService = new PointOrderService(pointOrderRepository, memberRepository, pointRepository, storeRepository);

        member = Member.createMemberWithUuidAndBarcode("01012345678", LocalDateTime.now());
        memberRepository.save(member);

        moonbucks = Brand.createBrandWithUuid("MOONBUCKS", LocalDateTime.now());
        brandRepository.save(moonbucks);

        moonbucksMetan = Store.createStoreWithUuid("MOONBUCKS 매탄점", moonbucks, LocalDateTime.now());
        storeRepository.save(moonbucksMetan);

        point = Point.createPointWithUuid(member, moonbucks, 5000L, LocalDateTime.now());
        pointRepository.save(point);

    }

    @Test
    public void createEarnPointOrder_성공() {

        // given
        CheckedMemberDto checkedMemberDto = CheckedMemberDto.builder()
                .id(member.getId())
                .phone(member.getPhone())
                .barcode(member.getBarcode())
                .build();

        CheckedStoreDto checkedStoreDto = CheckedStoreDto.builder()
                .brandName(moonbucks.getName())
                .brandId(moonbucks.getId())
                .storeName(moonbucksMetan.getName())
                .storeId(moonbucksMetan.getId())
                .build();

        EarnPointResultDto earnPointResultDto = EarnPointResultDto.builder()
                .pointId(point.getId())
                .remainedAmount(point.getAmount())
                .requestedAmount(3000l)
                .brandId(moonbucks.getId())
                .memberId(member.getId())
                .pointId(point.getId())
                .isSuccess(true)
                .build();

        // when
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createEarnPointOrder(
                checkedMemberDto, checkedStoreDto, earnPointResultDto);

        // then
        Optional<PointOrder> result = pointOrderRepository.findById(createPointOrderResultDto.getPointOrderId());

        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo(member.getId());
        assertThat(result.get().getBrandName()).isEqualTo(moonbucks.getName());
        assertThat(result.get().getStoreName()).isEqualTo(moonbucksMetan.getName());
        assertThat(result.get().getRequestedPointAmount()).isEqualTo(3000l);
        assertThat(result.get().getType()).isEqualTo(PointOrderType.EARN);

        assertThat(createPointOrderResultDto.getPointOrderId()).isEqualTo(result.get().getId());
        assertThat(createPointOrderResultDto.getRequestedPointAmount()).isEqualTo(3000l);
    }

    @Test
    public void createUsePointOrder_성공() {

        // given
        CheckedMemberDto checkedMemberDto = CheckedMemberDto.builder()
                .id(member.getId())
                .phone(member.getPhone())
                .barcode(member.getBarcode())
                .build();

        CheckedStoreDto checkedStoreDto = CheckedStoreDto.builder()
                .brandName(moonbucks.getName())
                .brandId(moonbucks.getId())
                .storeName(moonbucksMetan.getName())
                .storeId(moonbucksMetan.getId())
                .build();

        UsePointResultDto usePointResultDto = UsePointResultDto.builder()
                .pointId(point.getId())
                .remainedAmount(point.getAmount())
                .requestedAmount(2000l)
                .brandId(moonbucks.getId())
                .memberId(member.getId())
                .pointId(point.getId())
                .isSuccess(true)
                .build();

        // when
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createUsePointOrder(
                checkedMemberDto, checkedStoreDto, usePointResultDto);

        // then
        Optional<PointOrder> result = pointOrderRepository.findById(createPointOrderResultDto.getPointOrderId());

        assertThat(result).isPresent();
        assertThat(result.get().getMemberId()).isEqualTo(member.getId());
        assertThat(result.get().getBrandName()).isEqualTo(moonbucks.getName());
        assertThat(result.get().getStoreName()).isEqualTo(moonbucksMetan.getName());
        assertThat(result.get().getRequestedPointAmount()).isEqualTo(2000l);
        assertThat(result.get().getType()).isEqualTo(PointOrderType.USE);

        assertThat(createPointOrderResultDto.getPointOrderId()).isEqualTo(result.get().getId());
        assertThat(createPointOrderResultDto.getRequestedPointAmount()).isEqualTo(2000l);
    }

    @Test
    public void searchPointOrder_성공() {

        // given
        PointOrder pointOrder1 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.EARN, 5000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder1);
        LocalDateTime startTime = LocalDateTime.now();
        PointOrder pointOrder2 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.EARN, 3000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder2);
        PointOrder pointOrder3 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.USE, 2000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder3);
        PointOrder pointOrder4 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.USE, 1000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder4);
        PointOrder pointOrder5 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.EARN, 3000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder5);
        PointOrder pointOrder6 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.EARN, 7000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder6);
        PointOrder pointOrder7 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.EARN, 3000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder7);
        PointOrder pointOrder8 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.USE, 500L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder8);
        PointOrder pointOrder9 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.EARN, 4000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder9);
        LocalDateTime endTime = LocalDateTime.now();
        PointOrder pointOrder10 = PointOrder.createPointOrderWithUuid(member.getId(), moonbucks.getName(), moonbucksMetan.getName(), PointOrderType.EARN, 3000L, LocalDateTime.now());
        pointOrderRepository.save(pointOrder10);

        // when
        SearchPointOrderDto searchPointOrderDto1 = SearchPointOrderDto.builder()
                .memberId(member.getId())
                .startTime(startTime)
                .endTime(endTime)
                .page(0)
                .size(3)
                .build();

        List<SearchedPointOrderDto> searchedPointOrderDtos1 = pointOrderService.searchPointOrder(searchPointOrderDto1);

        SearchPointOrderDto searchPointOrderDto2 = SearchPointOrderDto.builder()
                .memberId(member.getId())
                .startTime(startTime)
                .endTime(endTime)
                .page(1)
                .size(3)
                .build();

        List<SearchedPointOrderDto> searchedPointOrderDtos2 = pointOrderService.searchPointOrder(searchPointOrderDto2);

        // then
        // DTO1
        assertThat(searchedPointOrderDtos1).isNotEmpty();
        assertThat(searchedPointOrderDtos1.size()).isEqualTo(3);
        assertThat(searchedPointOrderDtos1.get(0).getBrandName()).isEqualTo(pointOrder9.getBrandName());
        assertThat(searchedPointOrderDtos1.get(0).getStoreName()).isEqualTo(pointOrder9.getStoreName());
        assertThat(searchedPointOrderDtos1.get(0).getPointOrderType()).isEqualTo(pointOrder9.getType());
        assertThat(searchedPointOrderDtos1.get(0).getRequestedAmount()).isEqualTo(pointOrder9.getRequestedPointAmount());

        assertThat(searchedPointOrderDtos1.get(1).getBrandName()).isEqualTo(pointOrder8.getBrandName());
        assertThat(searchedPointOrderDtos1.get(1).getStoreName()).isEqualTo(pointOrder8.getStoreName());
        assertThat(searchedPointOrderDtos1.get(1).getPointOrderType()).isEqualTo(pointOrder8.getType());
        assertThat(searchedPointOrderDtos1.get(1).getRequestedAmount()).isEqualTo(pointOrder8.getRequestedPointAmount());

        assertThat(searchedPointOrderDtos1.get(2).getBrandName()).isEqualTo(pointOrder7.getBrandName());
        assertThat(searchedPointOrderDtos1.get(2).getStoreName()).isEqualTo(pointOrder7.getStoreName());
        assertThat(searchedPointOrderDtos1.get(2).getPointOrderType()).isEqualTo(pointOrder7.getType());
        assertThat(searchedPointOrderDtos1.get(2).getRequestedAmount()).isEqualTo(pointOrder7.getRequestedPointAmount());

        // DTO2
        assertThat(searchedPointOrderDtos2).isNotEmpty();
        assertThat(searchedPointOrderDtos2.size()).isEqualTo(3);
        assertThat(searchedPointOrderDtos2.get(0).getBrandName()).isEqualTo(pointOrder6.getBrandName());
        assertThat(searchedPointOrderDtos2.get(0).getStoreName()).isEqualTo(pointOrder6.getStoreName());
        assertThat(searchedPointOrderDtos2.get(0).getPointOrderType()).isEqualTo(pointOrder6.getType());
        assertThat(searchedPointOrderDtos2.get(0).getRequestedAmount()).isEqualTo(pointOrder6.getRequestedPointAmount());

        assertThat(searchedPointOrderDtos2.get(1).getBrandName()).isEqualTo(pointOrder5.getBrandName());
        assertThat(searchedPointOrderDtos2.get(1).getStoreName()).isEqualTo(pointOrder5.getStoreName());
        assertThat(searchedPointOrderDtos2.get(1).getPointOrderType()).isEqualTo(pointOrder5.getType());
        assertThat(searchedPointOrderDtos2.get(1).getRequestedAmount()).isEqualTo(pointOrder5.getRequestedPointAmount());

        assertThat(searchedPointOrderDtos2.get(2).getBrandName()).isEqualTo(pointOrder4.getBrandName());
        assertThat(searchedPointOrderDtos2.get(2).getStoreName()).isEqualTo(pointOrder4.getStoreName());
        assertThat(searchedPointOrderDtos2.get(2).getPointOrderType()).isEqualTo(pointOrder4.getType());
        assertThat(searchedPointOrderDtos2.get(2).getRequestedAmount()).isEqualTo(pointOrder4.getRequestedPointAmount());

    }

}