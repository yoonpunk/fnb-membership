package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.repository.PointOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 포인트를 사용하고 적립하기 위한 요청을 생성하는 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RequestPointOrderService {

    private MemberService memberService;
    private PointService pointService;
    private PointOrderRepository pointOrderRepository;

}
