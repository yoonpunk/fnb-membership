package com.fnb.membership.fnbmembership.controller;

import com.fnb.membership.fnbmembership.service.RequestPointOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fnb/requestorder")
@RequiredArgsConstructor
@Slf4j
public class RequestPointOrderServiceController extends RequestPointOrderService {

    private RequestPointOrderService pointOrderService;
}
