package com.fnb.membership.fnbmembership.controller;

import com.fnb.membership.fnbmembership.dto.EarnPointOrder;
import com.fnb.membership.fnbmembership.service.RequestPointOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fnb/membership")
@RequiredArgsConstructor
@Slf4j
public class RequestPointOrderServiceController {

    private final RequestPointOrderService requestPointOrderService;

    @Operation(summary = "fnb membership pointorder earning",
            description = "fnb membership 포인트 적립 API 입니다. " +
                    "기존 회원의 휴대폰 번호를 통해 포인트를 적립합니다. " +
                    "같은 브랜드의 점포에서는 포인트를 통합하여 적립할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @PostMapping("pointorder/earning/byphone")
    public ResponseEntity<EarnPointOrder.Response> earnPointOrder(EarnPointOrder.Request request) {

        log.info("earnPointOrder requested. request="+ request.toString());

//        try {
//            requestPointOrderService.requestEarnPoint();
//        } catch ( ) {
//
//        }
        return null;
    }

}
