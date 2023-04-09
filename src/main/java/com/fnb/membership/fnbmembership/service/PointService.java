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
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * A service to manage point information.
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
     * A method for requesting to earn point.
     * Return the point information if the point earning process is successful.
     * If the member is invalid, throw NoSuchMemberException.
     * If the brand is invalid, throw NoSuchMemberException.
     * If getting the optimistic lock fails, then do nothing.
     * @param earnPointDto
     * @return
     */
    public EarnPointResultDto earnPoint(EarnPointDto earnPointDto) {

        log.info("earnPoint requested. earnPointDto=" + earnPointDto.toString());

        try {
            // Check the exist points while obtaining the optimistic lock.
            Optional<Point> point = pointRepository.findByMemberIdAndBrandIdWithOptimisticLock(earnPointDto.getMemberId(), earnPointDto.getBrandId());
            Point resultPoint;

            // Creates a new point, if point doesn't exist.
            if (point.isEmpty()) {

                // Find the member.
                Optional<Member> member = memberRepository.findById(earnPointDto.getMemberId());

                if (member.isEmpty()) {
                    log.error("invalid memberId requested. memberId=" + earnPointDto.getMemberId());
                    throw new NoSuchMemberException();
                }

                // Find the brand.
                Optional<Brand> brand = brandRepository.findById(earnPointDto.getBrandId());

                if (member.isEmpty()) {
                    log.error("invalid brandId requested. brandId=" + earnPointDto.getBrandId());
                    throw new NoSuchBrandException();
                }

                resultPoint = Point.createPoint(UUID.randomUUID(), member.get(), brand.get(), earnPointDto.getAmount(), LocalDateTime.now());
                resultPoint = pointRepository.save(resultPoint);

                EarnPointResultDto result = EarnPointResultDto.builder()
                        .memberId(earnPointDto.getMemberId())
                        .brandId(earnPointDto.getBrandId())
                        .pointId(resultPoint.getId())
                        .isSuccess(true)
                        .requestedAmount(earnPointDto.getAmount())
                        .remainedAmount(resultPoint.getAmount())
                        .build();
                log.info("earnPoint completed. first pointResultDto=" + earnPointDto.toString());

                return result;

            } else { // If points already exist, earns points.
                Point validPoint = point.get();
                validPoint.earnPoint(earnPointDto.getAmount());
                pointRepository.save(validPoint);

                EarnPointResultDto result = EarnPointResultDto.builder()
                        .memberId(earnPointDto.getMemberId())
                        .brandId(earnPointDto.getBrandId())
                        .pointId(validPoint.getId())
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
     * A method for requesting to use point.
     * Return the point information if the point using process is successful.
     * If the point is not sufficient, throw NotEoughPointException.
     * @param usePointDto
     * @return
     * @throws NotEnoughPointException
     */
    public UsePointResultDto usePoint(UsePointDto usePointDto) throws NotEnoughPointException {

        log.info("usePoint requested. usePointDto=" + usePointDto.toString());

        try {
            // Checking the earned points while obtaining optimistic lock.
            Optional<Point> point = pointRepository.findByMemberIdAndBrandIdWithOptimisticLock(
                    usePointDto.getMemberId(), usePointDto.getBrandId());

            // If Point doesn't exist, throw NotEnoughPointException.
            if (point.isEmpty()) {
                log.error("earned point is not exist.");
                throw new NotEnoughPointException();
            }

            Point validPoint = point.get();

            // If Point already exist but not sufficient, throw NotEnoughPointException.
            if (!validPoint.isEnoughToUse(usePointDto.getAmount())) {
                log.error("earned point is not enough.");
                throw new NotEnoughPointException();
            }

            // Use points.
            validPoint.usePoint(usePointDto.getAmount());
            pointRepository.save(validPoint);

            log.info("use point completed. usePointDto=" + usePointDto.toString());
            return UsePointResultDto.builder()
                    .memberId(validPoint.getMember().getId())
                    .brandId(validPoint.getBrand().getId())
                    .pointId(validPoint.getId())
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
