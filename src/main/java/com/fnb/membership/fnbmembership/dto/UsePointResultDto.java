package com.fnb.membership.fnbmembership.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 포인트 사용 요청의 결과 정보를 담은 DTO
 * PointService 에서 사용
 */
@Data
@Builder
public class UsePointResultDto {

    private String brandId;
    private String memberId;
    private String pointId;
    private boolean isSuccess;
    private Long requestedAmount;
    private Long remainedAmount;
}
