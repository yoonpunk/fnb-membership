package com.fnb.membership.fnbmembership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

/**
 * 휴대전화번호로 포인트 적립/사용 요청 시, 사용하는 Request/Response 객체
 * RequestPointOrderServiceController에서 사용
 */
public class EarnPointOrderByBarcode {

    @Data
    @Builder
    public static class Request {
        private String barcode;
        private String storeId;
        private Long pointAmount;
    }

    @Data
    @Builder
    public static class Response {
        String result;
    }


}
