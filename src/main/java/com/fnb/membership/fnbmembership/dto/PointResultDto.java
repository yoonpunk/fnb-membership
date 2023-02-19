package com.fnb.membership.fnbmembership.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 포인트 적립/사용 요청의 결과 정보를 담은 DTO
 */
@Data
@Builder
public class PointResultDto {

    private String brandId;
    private String memberId;
    private String pointId;
    private Long requestedAmount;
    private Long remainedAmount;
}
