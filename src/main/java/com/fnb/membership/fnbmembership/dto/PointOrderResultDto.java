package com.fnb.membership.fnbmembership.dto;

import lombok.Data;

/**
 * RequestPointOrderService의 포인트 적립/사용 요청 생성 후의 결과를 저장하는 DTO
 */
@Data
public class PointOrderResultDto {

    String barcode;
    String phone;
    Long requestedPointAmount;
    Long remainedPointAmount;
    String storeId;
    String brandId;
}
