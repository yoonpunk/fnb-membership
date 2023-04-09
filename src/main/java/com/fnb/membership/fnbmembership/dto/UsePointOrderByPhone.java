package com.fnb.membership.fnbmembership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 휴대전화번호로 포인트 사용 요청 시, 사용하는 Request/Response 객체
 * RequestPointOrderServiceController에서 사용
 */
public class UsePointOrderByPhone {

    @Data
    @Builder
    @Schema(name = "UsePointOrderByPhoneRequest")
    public static class Request {
        private String phone;
        private Long storeId;
        private Long pointAmount;
    }

    @Data
    @Builder
    @Schema(name = "UsePointOrderByPhoneResponse")
    public static class Response {
        String result;
    }


}
