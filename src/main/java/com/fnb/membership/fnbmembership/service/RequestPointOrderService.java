package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.dto.*;
import com.fnb.membership.fnbmembership.exception.NoSuchBrandException;
import com.fnb.membership.fnbmembership.exception.NoSuchMemberException;
import com.fnb.membership.fnbmembership.exception.NoSuchStoreException;
import com.fnb.membership.fnbmembership.exception.NotEnoughPointException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;

/**
 * A service for requesting to use and to earn points.
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RequestPointOrderService {

    private final MemberService memberService;
    private final StoreService storeService;
    private final PointService pointService;
    private final PointOrderService pointOrderService;

    public RequestEarnPointOrderResultDto createEarnPointOrderByBarcode(String barcode, Long pointAmount, Long storeId)
            throws NoSuchMemberException, NoSuchStoreException, IllegalArgumentException {

        log.info("createEarnPointOrderByBarcode requested. " +
                "barcde=" + barcode + " pointAmount=" + pointAmount + "storeId=" + storeId);

        // Checking the member by barcode.
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByBarcode(barcode);

        // Checking the brand and the store.
        CheckedStoreDto checkedStoreDto = storeService.checkStore(storeId);

        // Earning points.
        EarnPointDto earnPointDto = EarnPointDto.builder()
                .memberId(checkedMemberDto.getId())
                .brandId(checkedStoreDto.getBrandId())
                .amount(pointAmount)
                .build();

        EarnPointResultDto earnPointResultDto = pointService.earnPoint(earnPointDto);

        // Check the result of earning points and throw an OptimisticLockException if the operation fails.
        // This code requires refactoring.
        if (!earnPointResultDto.isSuccess()) {
            throw new OptimisticLockException();
        }

        // Creating a pointOrder.
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createEarnPointOrder(checkedMemberDto, checkedStoreDto, earnPointResultDto);

        // Returning the result.
        RequestEarnPointOrderResultDto result = RequestEarnPointOrderResultDto.builder()
                .phone(checkedMemberDto.getPhone())
                .barcode(checkedMemberDto.getBarcode())
                .brandId(checkedStoreDto.getBrandId())
                .storeId(checkedStoreDto.getStoreId())
                .requestedPointAmount(earnPointResultDto.getRequestedAmount())
                .remainedPointAmount(earnPointResultDto.getRemainedAmount())
                .build();

        log.info("createEarnPointOrderByBarcode completed. result=" + result.toString());
        return result;
    }

    public RequestEarnPointOrderResultDto createEarnPointOrderByPhone(String phone, Long pointAmount, Long storeId)
            throws NoSuchMemberException, NoSuchStoreException, NoSuchBrandException, OptimisticLockException {

        log.info("createEarnPointOrderByPhone requested. " +
                "phone=" + phone + " pointAmount=" + pointAmount + "storeId=" + storeId);

        // Checking the member by phone number.
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByPhone(phone);

        // Checking the brand and store.
        CheckedStoreDto checkedStoreDto = storeService.checkStore(storeId);

        // Earning points.
        EarnPointDto earnPointDto = EarnPointDto.builder()
                .memberId(checkedMemberDto.getId())
                .brandId(checkedStoreDto.getBrandId())
                .amount(pointAmount)
                .build();

        EarnPointResultDto earnPointResultDto = pointService.earnPoint(earnPointDto);

        // Check the result of earning points and throw an OptimisticLockException if the operation fails.
        // This code requires refactoring.
        if (!earnPointResultDto.isSuccess()) {
            throw new OptimisticLockException();
        }

        // Creating a pointOrder
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createEarnPointOrder(checkedMemberDto, checkedStoreDto, earnPointResultDto);

        // Returning the result.
        RequestEarnPointOrderResultDto result = RequestEarnPointOrderResultDto.builder()
                .phone(checkedMemberDto.getPhone())
                .barcode(checkedMemberDto.getBarcode())
                .brandId(checkedStoreDto.getBrandId())
                .storeId(checkedStoreDto.getStoreId())
                .requestedPointAmount(earnPointResultDto.getRequestedAmount())
                .remainedPointAmount(earnPointResultDto.getRemainedAmount())
                .build();

        log.info("createEarnPointOrderByPhone completed. result=" + result.toString());
        return result;
    }

    public RequestUsePointOrderResultDto createUsePointOrderByBarcode(String barcode, Long pointAmount, Long storeId)
            throws NoSuchMemberException, NoSuchStoreException, IllegalArgumentException, NotEnoughPointException,
            OptimisticLockException {

        log.info("createUsePointOrderByBarcode requested. " +
                "barcde=" + barcode + " pointAmount=" + pointAmount + "storeId=" + storeId);

        // Checking the member by barcode
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByBarcode(barcode);

        // Checking the brand and store.
        CheckedStoreDto checkedStoreDto = storeService.checkStore(storeId);

        // Using points.
        UsePointDto usePointDto = UsePointDto.builder()
                .memberId(checkedMemberDto.getId())
                .brandId(checkedStoreDto.getBrandId())
                .amount(pointAmount)
                .build();

        UsePointResultDto usePointResultDto = pointService.usePoint(usePointDto);

        // Check the result of using points and throw an OptimisticLockException if the operation fails.
        // This code requires refactoring.
        if (!usePointResultDto.isSuccess()) {
            throw new OptimisticLockException();
        }

        // Creating a pointOrder
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createUsePointOrder(checkedMemberDto, checkedStoreDto, usePointResultDto);

        // Returning the result.
        RequestUsePointOrderResultDto result = RequestUsePointOrderResultDto.builder()
                .phone(checkedMemberDto.getPhone())
                .barcode(checkedMemberDto.getBarcode())
                .brandId(checkedStoreDto.getBrandId())
                .storeId(checkedStoreDto.getStoreId())
                .requestedPointAmount(usePointResultDto.getRequestedAmount())
                .remainedPointAmount(usePointResultDto.getRemainedAmount())
                .build();

        log.info("createUsePointOrderByBarcode completed. result=" + result.toString());
        return result;
    }

    public RequestUsePointOrderResultDto createUsePointOrderByPhone(String phone, Long pointAmount, Long storeId)
            throws NoSuchMemberException, NoSuchStoreException, IllegalArgumentException, NotEnoughPointException,
            OptimisticLockException {

        log.info("createUsePointOrderByPhone requested. " +
                "barcde=" + phone + " pointAmount=" + pointAmount + "storeId=" + storeId);

        // Checking the member by phone.
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByPhone(phone);

        // Checking the brand and the store.
        CheckedStoreDto checkedStoreDto = storeService.checkStore(storeId);

        // Using points.
        UsePointDto usePointDto = UsePointDto.builder()
                .memberId(checkedMemberDto.getId())
                .brandId(checkedStoreDto.getBrandId())
                .amount(pointAmount)
                .build();

        UsePointResultDto usePointResultDto = pointService.usePoint(usePointDto);

        // Check the result of using points and throw an OptimisticLockException if the operation fails.
        // This code requires refactoring.
        if (!usePointResultDto.isSuccess()) {
            throw new OptimisticLockException();
        }

        // Creating a pointOrder.
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createUsePointOrder(checkedMemberDto, checkedStoreDto, usePointResultDto);

        // Returning result.
        RequestUsePointOrderResultDto result = RequestUsePointOrderResultDto.builder()
                .phone(checkedMemberDto.getPhone())
                .barcode(checkedMemberDto.getBarcode())
                .brandId(checkedStoreDto.getBrandId())
                .storeId(checkedStoreDto.getStoreId())
                .requestedPointAmount(usePointResultDto.getRequestedAmount())
                .remainedPointAmount(usePointResultDto.getRemainedAmount())
                .build();

        log.info("createUsePointOrderByPhone completed. result=" + result.toString());
        return result;
    }

}
