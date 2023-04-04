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

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A service for creating and searching pointOrders.
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
     * This method creates a pointOrder with the type of PointOrderType.EARN.
     * @param checkedMember
     * @param checkedStore
     * @param earnPointResultDto
     * @return CreatePointOrderResultDto
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

        // Searching the member.
        Optional<Member> searchedMember = memberRepository.findById(UUID.fromString(checkedMember.getId()));

        if (searchedMember.isEmpty()) {
            log.error("member is invalid. memberId=" + checkedMember.getId());
            throw new NoSuchMemberException();
        }

        // Searching the store.
        Optional<Store> searchedStore = storeRepository.findByIdWithBrand(UUID.fromString(checkedStore.getStoreId()));

        if (searchedStore.isEmpty()) {
            log.error("store is invalid. storeId=" + checkedStore.getStoreId());
            throw new NoSuchStoreException();
        }
        // Searching the point.
        Optional<Point> searchedPoint = pointRepository.findById(UUID.fromString(earnPointResultDto.getPointId()));

        if (searchedPoint.isEmpty()) {
            log.error("point is invaild. pointId=" + earnPointResultDto.getPointId());
            throw new NoSuchPointExcpetion();
        }

        // Creating a pointOrder.
        // Check the success status of earnPointResultDto and handle exceptions in case of failure.
        if (!earnPointResultDto.isSuccess()) {
            throw new IllegalArgumentException("Failed Point result.");
        }

        PointOrder pointOrder = PointOrder.createPointOrder(
                searchedMember.get(),
                searchedStore.get().getBrand().getName(),
                searchedStore.get().getName(),
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
     * This method creates a pointOrder with the type of PointOrderType.USE.
     * @param checkedMember
     * @param checkedStore
     * @param usePointResultDto
     * @return CreatePointOrderResultDto
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

        // Searching the member.
        Optional<Member> searchedMember = memberRepository.findById(UUID.fromString(checkedMember.getId()));

        if (searchedMember.isEmpty()) {
            log.error("member is invalid. memberId=" + checkedMember.getId());
            throw new NoSuchMemberException();
        }

        // Searching the store.
        Optional<Store> searchedStore = storeRepository.findById(UUID.fromString(checkedStore.getStoreId()));

        if (searchedStore.isEmpty()) {
            log.error("store is invalid. storeId=" + checkedStore.getStoreId());
            throw new NoSuchStoreException();
        }

        // Searching the point
        Optional<Point> searchedPoint = pointRepository.findById(UUID.fromString(usePointResultDto.getPointId()));

        if (searchedPoint.isEmpty()) {
            log.error("point is invaild. pointId=" + usePointResultDto.getPointId());
            throw new NoSuchPointExcpetion();
        }

        // Creating a pointOrder
        // Checks the success status of usePointResultDto and handle exceptions in case of failure.
        if (!usePointResultDto.isSuccess()) {
            throw new IllegalArgumentException("Failed Point result.");
        }

        PointOrder pointOrder = PointOrder.createPointOrder(
                searchedMember.get(),
                searchedStore.get().getBrand().getName(),
                searchedStore.get().getName(),
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

    /**
     * A method for searching pointOrder histories.
     * @param searchPointOrder A DTO object containing search conditions.
     * @return A List of searched pointOrder histories.
     */
    public List<SearchedPointOrderDto> searchPointOrder(SearchPointOrderDto searchPointOrder) {

        log.info("searchPointOrder requested. " +
                " memberId=" + searchPointOrder.getMemberId() +
                " startTime=" + searchPointOrder.getStartTime() +
                " endTime=" + searchPointOrder.getStartTime());

        List<PointOrder> pointOrders = pointOrderRepository.findByMemberIdAndTimeOrderByApprovedAtDescWithPaging(
                UUID.fromString(searchPointOrder.getMemberId()),
                searchPointOrder.getStartTime(),
                searchPointOrder.getEndTime(),
                searchPointOrder.getPage(),
                searchPointOrder.getSize()
        );

        return pointOrders.stream()
                .map(pointOrder -> SearchedPointOrderDto.builder()
                        .approvedAt(pointOrder.getApprovedAt())
                        .requestedAmount(pointOrder.getRequestedPointAmount())
                        .brandName(pointOrder.getBrandName())
                        .storeName(pointOrder.getStoreName())
                        .pointOrderType(pointOrder.getType())
                        .build())
                .collect(Collectors.toList());
    }
}
