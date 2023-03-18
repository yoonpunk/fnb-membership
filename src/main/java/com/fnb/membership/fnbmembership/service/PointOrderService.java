package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.*;
import com.fnb.membership.fnbmembership.dto.*;
import com.fnb.membership.fnbmembership.exception.NoSuchMemberException;
import com.fnb.membership.fnbmembership.exception.NoSuchPointExcpetion;
import com.fnb.membership.fnbmembership.exception.NoSuchStoreException;
import com.fnb.membership.fnbmembership.repository.MemberRepository;
import com.fnb.membership.fnbmembership.repository.PointOrderRepository;
import com.fnb.membership.fnbmembership.repository.PointRepository;
import com.fnb.membership.fnbmembership.repository.StoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * 포인트를 사용요청/적립요청을 생성하기위한 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointOrderService {

    private final PointOrderRepository pointOrderRepository;
    private final MemberRepository memberRepository;
    private final PointRepository pointRepository;
    private final StoreRepository storeRepository;

    /**
     * PointOrder PointOrderType.EARN 생성을 위한 메서드
     * @param checkedMember 검증된 회원 정보
     * @param checkedStore 검증된 점포 정보
     * @param earnPointResultDto 포인트 적립 요청 결과 정보
     * @return CreatePointOrderResultDto 생성된 PointOrder의 정보가 담긴 DTO
     * @throws NoSuchMemberException
     * @throws NoSuchStoreException
     * @throws IllegalArgumentException
     */
    public CreatePointOrderResultDto createEarnPointOrder (
            CheckedMemberDto checkedMember, CheckedStoreDto checkedStore, EarnPointResultDto earnPointResultDto)
            throws NoSuchMemberException, NoSuchStoreException, IllegalArgumentException {

        log.info("createEarnPointOrder requested. " +
                " memberId=" + checkedMember.getId() +
                " storeId=" + checkedStore.getStoreId() +
                " brandId=" + checkedStore.getBrandId() +
                " type=" + PointOrderType.EARN +
                " isSuccess=" + earnPointResultDto.isSuccess() +
                " requestedPointAmount=" + earnPointResultDto.getRequestedAmount());

        // Member 객체 조회
        Optional<Member> searchedMember = memberRepository.findById(UUID.fromString(checkedMember.getId()));

        if (searchedMember.isEmpty()) {
            log.error("member is invalid. memberId=" + checkedMember.getId());
            throw new NoSuchMemberException();
        }

        // Store 객체 조회
        Optional<Store> searchedStore = storeRepository.findByIdWithBrand(UUID.fromString(checkedStore.getStoreId()));

        if (searchedStore.isEmpty()) {
            log.error("store is invalid. storeId=" + checkedStore.getStoreId());
            throw new NoSuchStoreException();
        }
        // Point 객체 조회
        Optional<Point> searchedPoint = pointRepository.findById(UUID.fromString(earnPointResultDto.getPointId()));

        if (searchedPoint.isEmpty()) {
            log.error("point is invaild. pointId=" + earnPointResultDto.getPointId());
            throw new NoSuchPointExcpetion();
        }

        // PointOrder 생성
        // earnPointResultDto의 성공여부 체크, 실패 시 예외처리
        if (!earnPointResultDto.isSuccess()) {
            throw new IllegalArgumentException("Failed Point result.");
        }

        PointOrder pointOrder = PointOrder.createPointOrder(
                searchedMember.get(),
                searchedStore.get().getBrand().getId(),
                searchedStore.get().getId(),
                PointOrderType.EARN,
                earnPointResultDto.getRequestedAmount()
        );

        pointOrder = pointOrderRepository.save(pointOrder);
        log.info("createEarnPointOrder completed. pointOrderId=" + pointOrder.getId().toString());

        CreatePointOrderResultDto createPointOrderResultDto = CreatePointOrderResultDto.builder()
                .pointOrderId(pointOrder.getId().toString())
                .pointOrderType(PointOrderType.EARN)
                .requestedPointAmount(pointOrder.getRequestedPointAmount())
                .approvedAt(pointOrder.getApprovedAt())
                .build();

        log.info("createEarnPointOrder completed. CreatePointOrderResultDto=" + createPointOrderResultDto.toString());

        return createPointOrderResultDto;
    }

    /**
     * PointOrder PointOrderType.USE 생성을 위한 메서드
     * @param checkedMember 검증된 회원 정보
     * @param checkedStore 검증된 점포 정보
     * @param usePointResultDto 포인트 사용 요청 결과 정보
     * @return CreatePointOrderResultDto 생성된 PointOrder의 정보가 담긴 DTO
     * @throws NoSuchMemberException
     * @throws NoSuchStoreException
     * @throws IllegalArgumentException
     */
    public CreatePointOrderResultDto createUsePointOrder (
            CheckedMemberDto checkedMember, CheckedStoreDto checkedStore, UsePointResultDto usePointResultDto)
            throws NoSuchMemberException, NoSuchStoreException, IllegalArgumentException {

        log.info("createUsePointOrder requested. " +
                " memberId=" + checkedMember.getId() +
                " storeId=" + checkedStore.getStoreId() +
                " brandId=" + checkedStore.getBrandId() +
                " type=" + PointOrderType.USE +
                " isSuccess=" + usePointResultDto.isSuccess() +
                " requestedPointAmount=" + usePointResultDto.getRequestedAmount());

        // Member 객체 조회
        Optional<Member> searchedMember = memberRepository.findById(UUID.fromString(checkedMember.getId()));

        if (searchedMember.isEmpty()) {
            log.error("member is invalid. memberId=" + checkedMember.getId());
            throw new NoSuchMemberException();
        }

        // Store 객체 조회
        Optional<Store> searchedStore = storeRepository.findById(UUID.fromString(checkedStore.getStoreId()));

        if (searchedStore.isEmpty()) {
            log.error("store is invalid. storeId=" + checkedStore.getStoreId());
            throw new NoSuchStoreException();
        }

        // Point 객체 조회
        Optional<Point> searchedPoint = pointRepository.findById(UUID.fromString(usePointResultDto.getPointId()));

        if (searchedPoint.isEmpty()) {
            log.error("point is invaild. pointId=" + usePointResultDto.getPointId());
            throw new NoSuchPointExcpetion();
        }

        // PointOrder 생성
        // usePointResultDto 성공여부 체크, 실패 시 예외처리
        if (!usePointResultDto.isSuccess()) {
            throw new IllegalArgumentException("Failed Point result.");
        }

        PointOrder pointOrder = PointOrder.createPointOrder(
                searchedMember.get(),
                searchedStore.get().getBrand().getId(),
                searchedStore.get().getId(),
                PointOrderType.USE,
                usePointResultDto.getRequestedAmount()
        );

        pointOrder = pointOrderRepository.save(pointOrder);
        log.info("createUsePointOrder completed. pointOrderId=" + pointOrder.getId().toString());

        CreatePointOrderResultDto createPointOrderResultDto = CreatePointOrderResultDto.builder()
                .pointOrderId(pointOrder.getId().toString())
                .pointOrderType(PointOrderType.USE)
                .requestedPointAmount(pointOrder.getRequestedPointAmount())
                .approvedAt(pointOrder.getApprovedAt())
                .build();

        log.info("createUsePointOrder completed. CreateUseOrderResultDto=" + createPointOrderResultDto.toString());

        return createPointOrderResultDto;
    }
}
