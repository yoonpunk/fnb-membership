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

import static org.assertj.core.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RequestPointOrderServiceTest {

    private RequestPointOrderService requestPointOrderService;

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

        existMember = Member.createMember("01012345678");
        memberRepository.save(existMember);

        seoulBaguette = Brand.createBrand("SEOULBAGUETTE");
        brandRepository.save(seoulBaguette);

        seoulBaguetteSinsa = Store.createStore("SEOULBAGUETTE 신사점", seoulBaguette);
        storeRepository.save(seoulBaguetteSinsa);

        burgerQueen = Brand.createBrand("BURGERQUEEN");
        brandRepository.save(burgerQueen);

        burgerQueenPangyo = Store.createStore("BURGERQUEEN 판교점", burgerQueen);
        storeRepository.save(burgerQueenPangyo);

        Point point = Point.createPoint(existMember, burgerQueen, 15000l);
        pointRepository.save(point);

    }

    @Test
    void createEarnPointOrderByBarcode() {

        // given
        String barcode = existMember.getBarcode();

        // when
        // 2번 적립, 최초 포인트 적립, 기존 포인트에 누적으로 적립
        requestPointOrderService.createEarnPointOrderByBarcode(barcode, 5000l, seoulBaguetteSinsa.getId().toString());
        RequestEarnPointOrderResultDto result = requestPointOrderService.createEarnPointOrderByBarcode(
                barcode, 5000l, seoulBaguetteSinsa.getId().toString());

        // then
        assertThat(result.getBarcode()).isEqualTo(existMember.getBarcode());
        assertThat(result.getPhone()).isEqualTo(existMember.getPhone());
        assertThat(result.getBrandId()).isEqualTo(seoulBaguette.getId().toString());
        assertThat(result.getStoreId()).isEqualTo(seoulBaguetteSinsa.getId().toString());
        assertThat(result.getRequestedPointAmount()).isEqualTo(5000l);
        assertThat(result.getRemainedPointAmount()).isEqualTo(10000l);
    }

    @Test
    void createEarnPointOrderByPhone() {

        // given
        String phone = existMember.getPhone();

        // when
        // 2번 적립, 최초 포인트 적립, 기존 포인트에 누적으로 적립
        requestPointOrderService.createEarnPointOrderByPhone(phone, 5000l, seoulBaguetteSinsa.getId().toString());
        RequestEarnPointOrderResultDto result = requestPointOrderService.createEarnPointOrderByPhone(
                phone, 5000l, seoulBaguetteSinsa.getId().toString());

        // then
        assertThat(result.getBarcode()).isEqualTo(existMember.getBarcode());
        assertThat(result.getPhone()).isEqualTo(existMember.getPhone());
        assertThat(result.getBrandId()).isEqualTo(seoulBaguette.getId().toString());
        assertThat(result.getStoreId()).isEqualTo(seoulBaguetteSinsa.getId().toString());
        assertThat(result.getRequestedPointAmount()).isEqualTo(5000l);
        assertThat(result.getRemainedPointAmount()).isEqualTo(10000l);
    }

    @Test
    void createUsePointOrderByBarcode() {
        // given
        String barcode = existMember.getBarcode();

        // when
        RequestUsePointOrderResultDto result = requestPointOrderService.createUsePointOrderByBarcode(
                barcode, 5000l, burgerQueenPangyo.getId().toString());

        // then
        assertThat(result.getBarcode()).isEqualTo(existMember.getBarcode());
        assertThat(result.getPhone()).isEqualTo(existMember.getPhone());
        assertThat(result.getBrandId()).isEqualTo(burgerQueen.getId().toString());
        assertThat(result.getStoreId()).isEqualTo(burgerQueenPangyo.getId().toString());
        assertThat(result.getRequestedPointAmount()).isEqualTo(5000l);
        assertThat(result.getRemainedPointAmount()).isEqualTo(10000l);
    }

    @Test
    void createUsePointOrderByPhone() {
        // given
        String phone = existMember.getPhone();

        // when
        RequestUsePointOrderResultDto result = requestPointOrderService.createUsePointOrderByPhone(
                phone, 5000l, burgerQueenPangyo.getId().toString());

        // then
        assertThat(result.getBarcode()).isEqualTo(existMember.getBarcode());
        assertThat(result.getPhone()).isEqualTo(existMember.getPhone());
        assertThat(result.getBrandId()).isEqualTo(burgerQueen.getId().toString());
        assertThat(result.getStoreId()).isEqualTo(burgerQueenPangyo.getId().toString());
        assertThat(result.getRequestedPointAmount()).isEqualTo(5000l);
        assertThat(result.getRemainedPointAmount()).isEqualTo(10000l);
    }
}