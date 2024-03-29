package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Brand;
import com.fnb.membership.fnbmembership.domain.Member;
import com.fnb.membership.fnbmembership.domain.Point;
import com.fnb.membership.fnbmembership.domain.Store;
import com.fnb.membership.fnbmembership.dto.RequestEarnPointOrderResultDto;
import com.fnb.membership.fnbmembership.dto.RequestUsePointOrderResultDto;
import com.fnb.membership.fnbmembership.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RequestPointOrderServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private PointOrderRepository pointOrderRepository;

    private RequestPointOrderService requestPointOrderService;

    private MemberService memberService;

    private StoreService storeService;

    private PointService pointService;

    private PointOrderService pointOrderService;

    private Member existMember;

    private Brand seoulBaguette;
    private Brand burgerQueen;

    private Store seoulBaguetteSinsa;
    private Store burgerQueenPangyo;

    @BeforeEach
    private void init() {

        memberService = new MemberService(memberRepository);
        storeService = new StoreService(storeRepository);
        pointService = new PointService(pointRepository, memberRepository, brandRepository);
        pointOrderService = new PointOrderService(pointOrderRepository, memberRepository, pointRepository, storeRepository);
        requestPointOrderService = new RequestPointOrderService(memberService, storeService, pointService, pointOrderService);

        existMember = Member.createMember(UUID.randomUUID(),"01012345678", Member.createBarcode(), LocalDateTime.now());
        memberRepository.save(existMember);

        seoulBaguette = Brand.createBrand(UUID.randomUUID(), "SEOULBAGUETTE", LocalDateTime.now());
        brandRepository.save(seoulBaguette);

        seoulBaguetteSinsa = Store.createStore(UUID.randomUUID(), "SEOULBAGUETTE 신사점", seoulBaguette, LocalDateTime.now());
        storeRepository.save(seoulBaguetteSinsa);

        burgerQueen = Brand.createBrand(UUID.randomUUID(), "BURGERQUEEN", LocalDateTime.now());
        brandRepository.save(burgerQueen);

        burgerQueenPangyo = Store.createStore(UUID.randomUUID(), "BURGERQUEEN 판교점", burgerQueen, LocalDateTime.now());
        storeRepository.save(burgerQueenPangyo);

        Point point = Point.createPoint(UUID.randomUUID(), existMember, burgerQueen, 15000l, LocalDateTime.now());
        pointRepository.save(point);

    }

    @Test
    void createEarnPointOrderByBarcode() {

        // given
        String barcode = existMember.getBarcode();

        // when
        // 2번 적립, 최초 포인트 적립, 기존 포인트에 누적으로 적립
        requestPointOrderService.createEarnPointOrderByBarcode(barcode, 5000l, seoulBaguetteSinsa.getId());
        RequestEarnPointOrderResultDto result = requestPointOrderService.createEarnPointOrderByBarcode(
                barcode, 5000l, seoulBaguetteSinsa.getId());

        // then
        assertThat(result.getBarcode()).isEqualTo(existMember.getBarcode());
        assertThat(result.getPhone()).isEqualTo(existMember.getPhone());
        assertThat(result.getBrandId()).isEqualTo(seoulBaguette.getId());
        assertThat(result.getStoreId()).isEqualTo(seoulBaguetteSinsa.getId());
        assertThat(result.getRequestedPointAmount()).isEqualTo(5000l);
        assertThat(result.getRemainedPointAmount()).isEqualTo(10000l);
    }

    @Test
    void createEarnPointOrderByPhone() {

        // given
        String phone = existMember.getPhone();

        // when
        // 2번 적립, 최초 포인트 적립, 기존 포인트에 누적으로 적립
        requestPointOrderService.createEarnPointOrderByPhone(phone, 5000l, seoulBaguetteSinsa.getId());
        RequestEarnPointOrderResultDto result = requestPointOrderService.createEarnPointOrderByPhone(
                phone, 5000l, seoulBaguetteSinsa.getId());

        // then
        assertThat(result.getBarcode()).isEqualTo(existMember.getBarcode());
        assertThat(result.getPhone()).isEqualTo(existMember.getPhone());
        assertThat(result.getBrandId()).isEqualTo(seoulBaguette.getId());
        assertThat(result.getStoreId()).isEqualTo(seoulBaguetteSinsa.getId());
        assertThat(result.getRequestedPointAmount()).isEqualTo(5000l);
        assertThat(result.getRemainedPointAmount()).isEqualTo(10000l);
    }

    @Test
    void createUsePointOrderByBarcode() {
        // given
        String barcode = existMember.getBarcode();

        // when
        RequestUsePointOrderResultDto result = requestPointOrderService.createUsePointOrderByBarcode(
                barcode, 5000l, burgerQueenPangyo.getId());

        // then
        assertThat(result.getBarcode()).isEqualTo(existMember.getBarcode());
        assertThat(result.getPhone()).isEqualTo(existMember.getPhone());
        assertThat(result.getBrandId()).isEqualTo(burgerQueen.getId());
        assertThat(result.getStoreId()).isEqualTo(burgerQueenPangyo.getId());
        assertThat(result.getRequestedPointAmount()).isEqualTo(5000l);
        assertThat(result.getRemainedPointAmount()).isEqualTo(10000l);
    }

    @Test
    void createUsePointOrderByPhone() {
        // given
        String phone = existMember.getPhone();

        // when
        RequestUsePointOrderResultDto result = requestPointOrderService.createUsePointOrderByPhone(
                phone, 5000l, burgerQueenPangyo.getId());

        // then
        assertThat(result.getBarcode()).isEqualTo(existMember.getBarcode());
        assertThat(result.getPhone()).isEqualTo(existMember.getPhone());
        assertThat(result.getBrandId()).isEqualTo(burgerQueen.getId());
        assertThat(result.getStoreId()).isEqualTo(burgerQueenPangyo.getId());
        assertThat(result.getRequestedPointAmount()).isEqualTo(5000l);
        assertThat(result.getRemainedPointAmount()).isEqualTo(10000l);
    }
}