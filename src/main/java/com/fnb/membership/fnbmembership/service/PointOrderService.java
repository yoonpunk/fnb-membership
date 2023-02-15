package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.repository.PointOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 포인트를 사용요청/적립요청을 생성하기위한 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PointOrderService {

    private MemberService memberService;
    private PointService pointService;
    private PointOrderRepository pointOrderRepository;

//    public OrderResultDto createEarnPointOrderByPhone(String phone, String brandName, Long pointAmount) {
//
//        log.info("createOrderByPhone requested. phone=" + phone);
//
//        try {
//            // 회원 정보 조회
//            CheckedMemberDto checkedMemberDto = memberService.checkMemberByPhone(phone);
//            log.info("phone info is valid.");
//
//            // 브랜드 정보 조회
//
//        }
//
//        //
//    }
}
