package com.fnb.membership.fnbmembership.dto;

import lombok.Data;

/**
 * 포인트 사용/적립 요청의 결과를 저장하는 DTO
 */
@Data
public class OrderResultDto {

    private String pointOrderType;
    private long requestedAmount;
    private long remainedAmount;
    private String phone;
    private String barcode;
}
