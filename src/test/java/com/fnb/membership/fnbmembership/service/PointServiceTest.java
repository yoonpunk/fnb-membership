package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Brand;
import com.fnb.membership.fnbmembership.domain.Member;
import com.fnb.membership.fnbmembership.domain.Point;
import com.fnb.membership.fnbmembership.dto.EarnPointDto;
import com.fnb.membership.fnbmembership.dto.EarnPointResultDto;
import com.fnb.membership.fnbmembership.dto.UsePointDto;
import com.fnb.membership.fnbmembership.dto.UsePointResultDto;
import com.fnb.membership.fnbmembership.exception.NotEnoughPointException;
import com.fnb.membership.fnbmembership.repository.BrandRepository;
import com.fnb.membership.fnbmembership.repository.MemberRepository;
import com.fnb.membership.fnbmembership.repository.PointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointServiceTest {

    private PointService sut;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setUpSut() {
        sut = new PointService(pointRepository, memberRepository, brandRepository);
    }

    @Test
    void testEarnPointFirstTime() {

        // arrange
        Member expectedMember = Member.createMember(UUID.randomUUID(), "01012345678", Member.createBarcode(), LocalDateTime.now());
        expectedMember = memberRepository.save(expectedMember);

        Brand expectedBrand = Brand.createBrand(UUID.randomUUID(), "MOONBUCKS", LocalDateTime.now());
        expectedBrand = brandRepository.save(expectedBrand);

        // A registered member
        // The first brand for which points were earned.
        EarnPointDto earnPointDto = EarnPointDto.builder()
                .memberId(expectedMember.getId())
                .brandId(expectedBrand.getId())
                .amount(5000L)
                .build();

        // act
        EarnPointResultDto earnPointResultDto = sut.earnPoint(earnPointDto);

        // assert
        assertThat(earnPointResultDto.getMemberId()).isEqualTo(expectedMember.getId());
        assertThat(earnPointResultDto.getBrandId()).isEqualTo(expectedBrand.getId());
        assertThat(earnPointResultDto.getRequestedAmount()).isEqualTo(5000L);
        assertThat(earnPointResultDto.getRemainedAmount()).isEqualTo(5000L);
        assertThat(earnPointResultDto.isSuccess()).isTrue();
    }

    @Test
    void testEarnPointAlreadyExists() {

        // arrange
        Member expectedMember = Member.createMember(UUID.randomUUID(), "01012345678", Member.createBarcode(), LocalDateTime.now());
        expectedMember = memberRepository.save(expectedMember);

        Brand expectedBrand = Brand.createBrand(UUID.randomUUID(), "MOONBUCKS", LocalDateTime.now());
        expectedBrand = brandRepository.save(expectedBrand);

        Point expectedPoint = Point.createPoint(UUID.randomUUID(), expectedMember, expectedBrand, 5000L, LocalDateTime.now());
        expectedPoint = pointRepository.save(expectedPoint);

        // A registered member
        // Points already exists.
        EarnPointDto earnPointDto = EarnPointDto.builder()
                .memberId(expectedMember.getId())
                .brandId(expectedBrand.getId())
                .amount(5000L)
                .build();

        // act
        EarnPointResultDto earnPointResultDto = sut.earnPoint(earnPointDto);

        // assert
        assertThat(earnPointResultDto.getMemberId()).isEqualTo(expectedMember.getId());
        assertThat(earnPointResultDto.getBrandId()).isEqualTo(expectedBrand.getId());
        assertThat(earnPointResultDto.getPointId()).isEqualTo(expectedPoint.getId());
        assertThat(earnPointResultDto.getRequestedAmount()).isEqualTo(5000L);
        assertThat(earnPointResultDto.getRemainedAmount()).isEqualTo(10000L);
        assertThat(earnPointResultDto.isSuccess()).isTrue();
    }

    @Test
    void testUsePointWhenPointsAreSufficient() {

        // arrange
        Member expectedMember = Member.createMember(UUID.randomUUID(), "01012345678", Member.createBarcode(), LocalDateTime.now());
        expectedMember = memberRepository.save(expectedMember);

        Brand expectedBrand = Brand.createBrand(UUID.randomUUID(), "MOONBUCKS", LocalDateTime.now());
        expectedBrand = brandRepository.save(expectedBrand);

        Point expectedPoint = Point.createPoint(UUID.randomUUID(), expectedMember, expectedBrand, 5000L, LocalDateTime.now());
        expectedPoint = pointRepository.save(expectedPoint);

        // A registered member
        // Points are sufficient.
        UsePointDto usePointDto = UsePointDto.builder()
                .memberId(expectedMember.getId())
                .brandId(expectedBrand.getId())
                .amount(2000L)
                .build();

        // act
        UsePointResultDto usePointResultDto = sut.usePoint(usePointDto);

        // assert
        assertThat(usePointResultDto.getMemberId()).isEqualTo(expectedMember.getId());
        assertThat(usePointResultDto.getBrandId()).isEqualTo(expectedBrand.getId());
        assertThat(usePointResultDto.getRequestedAmount()).isEqualTo(2000L);
        assertThat(usePointResultDto.getRemainedAmount()).isEqualTo(3000L);
        assertThat(usePointResultDto.isSuccess()).isTrue();
    }

    @Test
    void testUserPointWhenPointsAreNotSufficient() {

        // arrange
        Member expectedMember = Member.createMember(UUID.randomUUID(), "01012345678", Member.createBarcode(), LocalDateTime.now());
        expectedMember = memberRepository.save(expectedMember);

        Brand expectedBrand = Brand.createBrand(UUID.randomUUID(), "MOONBUCKS", LocalDateTime.now());
        expectedBrand = brandRepository.save(expectedBrand);

        Point expectedPoint = Point.createPoint(UUID.randomUUID(), expectedMember, expectedBrand, 5000L, LocalDateTime.now());
        expectedPoint = pointRepository.save(expectedPoint);

        // A registered member
        // Points are not sufficient.
        UsePointDto usePointDto = UsePointDto.builder()
                .memberId(expectedMember.getId())
                .brandId(expectedBrand.getId())
                .amount(10000L)
                .build();

        // act & assert
        assertThrows(NotEnoughPointException.class, () -> {
            sut.usePoint(usePointDto);
        });

        Point point = pointRepository.findByMemberIdAndBrandIdWithOptimisticLock(
                expectedMember.getId(), expectedBrand.getId()).get();
        assertThat(point.getAmount()).isEqualTo(5000L);
    }
}