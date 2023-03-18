package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Brand;
import com.fnb.membership.fnbmembership.domain.Member;
import com.fnb.membership.fnbmembership.domain.Point;
import com.fnb.membership.fnbmembership.dto.EarnPointDto;
import com.fnb.membership.fnbmembership.dto.EarnPointResultDto;
import com.fnb.membership.fnbmembership.dto.UsePointDto;
import com.fnb.membership.fnbmembership.dto.UsePointResultDto;
import com.fnb.membership.fnbmembership.exception.NoSuchBrandException;
import com.fnb.membership.fnbmembership.exception.NoSuchMemberException;
import com.fnb.membership.fnbmembership.exception.NotEnoughPointException;
import com.fnb.membership.fnbmembership.repository.BrandRepository;
import com.fnb.membership.fnbmembership.repository.MemberRepository;
import com.fnb.membership.fnbmembership.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.Optional;
import java.util.UUID;

/**
 * 포인트 정보를 관리하기 위한 서비스
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PointService {

    private final PointRepository pointRepository;
    private final MemberRepository memberRepository;
    private final BrandRepository brandRepository;


    /**
     * 포인트 적립 요청
     * 포인트 적립 성공 시, 잔액 포인트 정보 제공
     * 해당 브랜드에 최초 적립 성공 시, 포인트 생성
     * 부적합 회원 정보 시, NoSuchMemberException 발생
     * 부적합 브랜드 정보 시, NoSuchBrandException 발생
     * 동시성 문제 발생 시, 적립 하지 않고 종료
     * @param earnPointDto
     * @return
     */
    public EarnPointResultDto earnPoint(EarnPointDto earnPointDto) {

        log.info("earnPoint requested. earnPointDto=" + earnPointDto.toString());

        try {
            // 적립된 포인트가 있는지 확인, 낙관적락을 걸어 조회
            Optional<Point> point = pointRepository.findByMemberIdAndBrandIdWithOptimisticLock(
                    UUID.fromString(earnPointDto.getMemberId()), UUID.fromString(earnPointDto.getBrandId()));

            Point resultPoint;

            // 적립된 포인트가 없을 경우, 포인트 최초 생성
            if (point.isEmpty()) {

                // 회원 조회
                Optional<Member> member = memberRepository.findById(UUID.fromString(earnPointDto.getMemberId()));

                if (member.isEmpty()) {
                    log.error("invalid memberId requested. memberId=" + earnPointDto.getMemberId());
                    throw new NoSuchMemberException();
                }

                // 브랜드 조회
                Optional<Brand> brand = brandRepository.findById(UUID.fromString(earnPointDto.getBrandId()));

                if (member.isEmpty()) {
                    log.error("invalid brandId requested. brandId=" + earnPointDto.getBrandId());
                    throw new NoSuchBrandException();
                }

                resultPoint = Point.createPoint(member.get(), brand.get(), earnPointDto.getAmount());
                resultPoint = pointRepository.save(resultPoint);

                EarnPointResultDto result = EarnPointResultDto.builder()
                        .memberId(earnPointDto.getMemberId())
                        .brandId(earnPointDto.getBrandId())
                        .pointId(resultPoint.getId().toString())
                        .isSuccess(true)
                        .requestedAmount(earnPointDto.getAmount())
                        .remainedAmount(resultPoint.getAmount())
                        .build();
                log.info("earnPoint completed. first pointResultDto=" + earnPointDto.toString());

                return result;

            } else { // 적립된 포인트가 있을 경우, 포인트 적립
                Point validPoint = point.get();
                validPoint.earnPoint(earnPointDto.getAmount());
                pointRepository.save(validPoint);

                EarnPointResultDto result = EarnPointResultDto.builder()
                        .memberId(earnPointDto.getMemberId())
                        .brandId(earnPointDto.getBrandId())
                        .pointId(validPoint.getId().toString())
                        .isSuccess(true)
                        .requestedAmount(earnPointDto.getAmount())
                        .remainedAmount(validPoint.getAmount())
                        .build();

                log.info("earnPoint completed. pointResultDto=" + earnPointDto.toString());

                return result;
            }
        } catch(OptimisticLockException ole) {
            log.error("A request was made to earn the points elsewhere. please try again.");

            EarnPointResultDto result = EarnPointResultDto.builder()
                    .memberId(earnPointDto.getMemberId())
                    .brandId(earnPointDto.getBrandId())
                    .pointId(null)
                    .isSuccess(false)
                    .requestedAmount(earnPointDto.getAmount())
                    .remainedAmount(0l)
                    .build();

            return result;
        }
    }

    /**
     * 포인트 사용 요청
     * 적립된 포인트 사용 성공 시, 잔액 포인트 정보 제공
     * 적립된 포인트가 존재하지 않거나 부족할 경우에는 NotEnoughPointException 발생
     * @param usePointDto
     * @return
     */
    public UsePointResultDto usePoint(UsePointDto usePointDto) throws NotEnoughPointException {

        log.info("usePoint requested. usePointDto=" + usePointDto.toString());

        try {
            // 적립된 포인트가 있는 지 확인, 낙관적락을 걸어 조회
            Optional<Point> point = pointRepository.findByMemberIdAndBrandIdWithOptimisticLock(
                    UUID.fromString(usePointDto.getMemberId()), UUID.fromString(usePointDto.getBrandId()));

            // 적립된 포인트가 없다면 예외처리
            if (point.isEmpty()) {
                log.error("earned point is not exist.");
                throw new NotEnoughPointException();
            }

            Point validPoint = point.get();

            // 적립된 포인트가 존재할 때, 사용 요청 된 포인트보다 적립금액이 작을 경우 예외처리
            if (!validPoint.isEnoughToUse(usePointDto.getAmount())) {
                log.error("earned point is not enough.");
                throw new NotEnoughPointException();
            }

            // 포인트 사용
            validPoint.usePoint(usePointDto.getAmount());
            pointRepository.save(validPoint);

            log.info("use point completed. usePointDto=" + usePointDto.toString());
            return UsePointResultDto.builder()
                    .memberId(validPoint.getMember().getId().toString())
                    .brandId(validPoint.getBrand().getId().toString())
                    .pointId(validPoint.getId().toString())
                    .isSuccess(true)
                    .requestedAmount(usePointDto.getAmount())
                    .remainedAmount(validPoint.getAmount())
                    .build();

        } catch(OptimisticLockException ole) {
            log.error("A request was made to use the points elsewhere. please try again.");

            UsePointResultDto result = UsePointResultDto.builder()
                    .memberId(usePointDto.getMemberId())
                    .brandId(usePointDto.getBrandId())
                    .pointId(null)
                    .isSuccess(false)
                    .requestedAmount(usePointDto.getAmount())
                    .remainedAmount(0l)
                    .build();

            return result;
        }
    }
}
