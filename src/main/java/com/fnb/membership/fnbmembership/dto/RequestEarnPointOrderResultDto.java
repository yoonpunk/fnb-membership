package com.fnb.membership.fnbmembership.dto;

import lombok.Builder;
import lombok.Data;

/**
 * RequestPointOrderService의 포인트 적립 요청 생성 후의 결과를 저장하는 DTO
 */
@Data
@Builder
public class RequestEarnPointOrderResultDto {

    String barcode;
    String phone;
    Long requestedPointAmount;
    Long remainedPointAmount;
    String storeId;
    String brandId;

}
