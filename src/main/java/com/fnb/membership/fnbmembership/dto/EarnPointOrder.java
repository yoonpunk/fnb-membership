package com.fnb.membership.fnbmembership.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 포인트 적립/사용 요청 시, 사용하는 Request/Response 객체
 * RequestPointOrderServiceController에서 사용
 */
public class EarnPointOrder {

    @Data
    @Builder
    public static class Request {

    }

    @Data
    @Builder
    public static class Response {

    }


}
