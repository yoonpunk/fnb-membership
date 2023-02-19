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

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PointServiceTest {

    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BrandRepository brandRepository;

    private Member existMember;
    private Brand existBrand;

    @BeforeEach
    void init() {
        pointService = new PointService(pointRepository, memberRepository, brandRepository);

        existMember = Member.createMember("01012345678");
        memberRepository.save(existMember);

        existBrand = Brand.createBrand("MOONBUCKS");
        brandRepository.save(existBrand);

        Point point = Point.createPoint(existMember, existBrand, 5000);
        pointRepository.save(point);
    }

    @Test
    void earnPoint_적립_최초_1회_성공() {

        // given
        Brand newBrand = Brand.createBrand("MARSBAKERY");
        brandRepository.save(newBrand);

        // 가입된 회원, 최초 적립하는 브랜드
        EarnPointDto earnPointDto = EarnPointDto.builder()
                .memberId(existMember.getId().toString())
                .brandId(newBrand.getId().toString())
                .amount(5000l)
                .build();

        // when
        EarnPointResultDto earnPointResultDto = pointService.earnPoint(earnPointDto);

        // then
        Optional<Point> result = pointRepository.findById(UUID.fromString((earnPointResultDto.getPointId())));
        assertThat(result).isPresent();
        assertThat(result.get().getId().toString()).isEqualTo(earnPointResultDto.getPointId());
        assertThat(result.get().getMember().getId()).isEqualTo(existMember.getId());
        assertThat(result.get().getBrand().getId()).isEqualTo(newBrand.getId());
        assertThat(result.get().getAmount()).isEqualTo(5000l);

        assertThat(earnPointResultDto.getMemberId()).isEqualTo(earnPointDto.getMemberId());
        assertThat(earnPointResultDto.getBrandId()).isEqualTo(earnPointDto.getBrandId());
        assertThat(earnPointResultDto.getRequestedAmount()).isEqualTo(earnPointDto.getAmount());
        assertThat(earnPointResultDto.getRemainedAmount()).isEqualTo(5000l);
        assertThat(earnPointResultDto.isSuccess()).isTrue();
    }

    @Test
    void earnPoint_적립_1회_성공() {

        // given
        Brand newBrand = Brand.createBrand("MARSBAKERY");
        brandRepository.save(newBrand);

        // 가입된 회원, 적립된 적 있는 브랜드
        EarnPointDto earnPointDto = EarnPointDto.builder()
                .memberId(existMember.getId().toString())
                .brandId(existBrand.getId().toString())
                .amount(5000l)
                .build();

        // when
        EarnPointResultDto earnPointResultDto = pointService.earnPoint(earnPointDto);

        // then
        Optional<Point> result = pointRepository.findById(UUID.fromString((earnPointResultDto.getPointId())));
        assertThat(result).isPresent();
        assertThat(result.get().getId().toString()).isEqualTo(earnPointResultDto.getPointId());
        assertThat(result.get().getMember().getId()).isEqualTo(existMember.getId());
        assertThat(result.get().getBrand().getId()).isEqualTo(existBrand.getId());
        assertThat(result.get().getAmount()).isEqualTo(10000l);

        assertThat(earnPointResultDto.getMemberId()).isEqualTo(earnPointDto.getMemberId());
        assertThat(earnPointResultDto.getBrandId()).isEqualTo(earnPointDto.getBrandId());
        assertThat(earnPointResultDto.getRequestedAmount()).isEqualTo(earnPointDto.getAmount());
        assertThat(earnPointResultDto.getRemainedAmount()).isEqualTo(10000);
        assertThat(earnPointResultDto.isSuccess()).isTrue();
    }

    @Test
    void usePoint_사용_1회_성공() {

        // given
        Brand newBrand = Brand.createBrand("MARSBAKERY");
        brandRepository.save(newBrand);

        // 가입된 회원, 적립된 적 있는 브랜드의 포인트 사용
        UsePointDto usePointDto = UsePointDto.builder()
                .memberId(existMember.getId().toString())
                .brandId(existBrand.getId().toString())
                .amount(2000l)
                .build();

        // when
        UsePointResultDto usePointResultDto = pointService.usePoint(usePointDto);

        // then
        Optional<Point> result = pointRepository.findById(UUID.fromString((usePointResultDto.getPointId())));
        assertThat(result).isPresent();
        assertThat(result.get().getId().toString()).isEqualTo(usePointResultDto.getPointId());
        assertThat(result.get().getMember().getId()).isEqualTo(existMember.getId());
        assertThat(result.get().getBrand().getId()).isEqualTo(existBrand.getId());
        assertThat(result.get().getAmount()).isEqualTo(3000l);

        assertThat(usePointResultDto.getMemberId()).isEqualTo(usePointDto.getMemberId());
        assertThat(usePointResultDto.getBrandId()).isEqualTo(usePointDto.getBrandId());
        assertThat(usePointResultDto.getRequestedAmount()).isEqualTo(usePointDto.getAmount());
        assertThat(usePointResultDto.getRemainedAmount()).isEqualTo(3000l);
        assertThat(usePointResultDto.isSuccess()).isTrue();
    }

    @Test
    void usePoint_사용_잔액_부족_실패() {

        // given
        // 가입된 회원, 적립된 적 있는 브랜드의 적립된 포인트 초과 사용
        UsePointDto usePointDto = UsePointDto.builder()
                .memberId(existMember.getId().toString())
                .brandId(existBrand.getId().toString())
                .amount(10000l)
                .build();

        // when & then
        assertThrows(NotEnoughPointException.class, () -> {
            pointService.usePoint(usePointDto);
        });

        // 포인트가 차감되지 않았는지 확인
        Optional<Point> point = pointRepository.findByMemberIdAndBrandIdWithOptimisticLock(
                existMember.getId(), existBrand.getId());
        assertThat(point.get().getAmount()).isEqualTo(5000l);
    }
}