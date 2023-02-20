package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.dto.*;
import com.fnb.membership.fnbmembership.exception.NoSuchMemberException;
import com.fnb.membership.fnbmembership.exception.NoSuchStoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;

/**
 * 포인트를 사용하고 적립하기 위한 요청을 생성하는 서비스
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

    public RequestEarnPointOrderResultDto createEarnPointOrderByBarcode(String barcode, Long pointAmount, String storeId)
            throws NoSuchMemberException, NoSuchStoreException, IllegalArgumentException {

        log.info("createEarnPointOrderByBarcode requested. " +
                "barcde=" + barcode + " pointAmount=" + pointAmount + "storeId=" + storeId);

        // 멤버 검증
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByBarcode(barcode);

        // 점포 & 브랜드 검증
        CheckedStoreDto checkedStoreDto = storeService.checkStore(storeId);

        // 포인트 적립
        EarnPointDto earnPointDto = EarnPointDto.builder()
                .memberId(checkedMemberDto.getId())
                .brandId(checkedStoreDto.getBrandId())
                .amount(pointAmount)
                .build();

        EarnPointResultDto earnPointResultDto = pointService.earnPoint(earnPointDto);

        // 포인트 적립 결과 확인, 낙관적락 취득 실패로 인한 결과 처리
        // 임시로 이렇게 처리, Exception Handling 리팩터링 시 수정
        if (!earnPointResultDto.isSuccess()) {
            throw new OptimisticLockException();
        }

        // PointOrder 생성
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createEarnPointOrder(checkedMemberDto, checkedStoreDto, earnPointResultDto);

        // 결과 리턴
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

    public RequestEarnPointOrderResultDto createEarnPointOrderByPhone(String phone, Long pointAmount, String storeId) {

        log.info("createEarnPointOrderByPhone requested. " +
                "phone=" + phone + " pointAmount=" + pointAmount + "storeId=" + storeId);

        // 멤버 검증
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByPhone(phone);

        // 점포 & 브랜드 검증
        CheckedStoreDto checkedStoreDto = storeService.checkStore(storeId);

        // 포인트 적립
        EarnPointDto earnPointDto = EarnPointDto.builder()
                .memberId(checkedMemberDto.getId())
                .brandId(checkedStoreDto.getBrandId())
                .amount(pointAmount)
                .build();

        EarnPointResultDto earnPointResultDto = pointService.earnPoint(earnPointDto);

        // 포인트 적립 결과 확인, 낙관적락 취득 실패로 인한 결과 처리
        // 임시로 이렇게 처리, Exception Handling 리팩터링 시 수정
        if (!earnPointResultDto.isSuccess()) {
            throw new OptimisticLockException();
        }

        // PointOrder 생성
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createEarnPointOrder(checkedMemberDto, checkedStoreDto, earnPointResultDto);

        // 결과 리턴
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

    public RequestUsePointOrderResultDto createUsePointOrderByBarcode(String barcode, Long pointAmount, String storeId)
            throws NoSuchMemberException, NoSuchStoreException, IllegalArgumentException {

        log.info("createUsePointOrderByBarcode requested. " +
                "barcde=" + barcode + " pointAmount=" + pointAmount + "storeId=" + storeId);

        // 멤버 검증
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByBarcode(barcode);

        // 점포 & 브랜드 검증
        CheckedStoreDto checkedStoreDto = storeService.checkStore(storeId);

        // 포인트 사용
        UsePointDto usePointDto = UsePointDto.builder()
                .memberId(checkedMemberDto.getId())
                .brandId(checkedStoreDto.getBrandId())
                .amount(pointAmount)
                .build();

        UsePointResultDto usePointResultDto = pointService.usePoint(usePointDto);

        // 포인트 적립 결과 확인, 낙관적락 취득 실패로 인한 결과 처리
        // 임시로 이렇게 처리, Exception Handling 리팩터링 시 수정
        if (!usePointResultDto.isSuccess()) {
            throw new OptimisticLockException();
        }

        // PointOrder 생성
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createUsePointOrder(checkedMemberDto, checkedStoreDto, usePointResultDto);

        // 결과 리턴
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

    public RequestUsePointOrderResultDto createUsePointOrderByPhone(String phone, Long pointAmount, String storeId)
            throws NoSuchMemberException, NoSuchStoreException, IllegalArgumentException {

        log.info("createUsePointOrderByPhone requested. " +
                "barcde=" + phone + " pointAmount=" + pointAmount + "storeId=" + storeId);

        // 멤버 검증
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByPhone(phone);

        // 점포 & 브랜드 검증
        CheckedStoreDto checkedStoreDto = storeService.checkStore(storeId);

        // 포인트 사용
        UsePointDto usePointDto = UsePointDto.builder()
                .memberId(checkedMemberDto.getId())
                .brandId(checkedStoreDto.getBrandId())
                .amount(pointAmount)
                .build();

        UsePointResultDto usePointResultDto = pointService.usePoint(usePointDto);

        // 포인트 적립 결과 확인, 낙관적락 취득 실패로 인한 결과 처리
        // 임시로 이렇게 처리, Exception Handling 리팩터링 시 수정
        if (!usePointResultDto.isSuccess()) {
            throw new OptimisticLockException();
        }

        // PointOrder 생성
        CreatePointOrderResultDto createPointOrderResultDto = pointOrderService.createUsePointOrder(checkedMemberDto, checkedStoreDto, usePointResultDto);

        // 결과 리턴
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
