package com.fnb.membership.fnbmembership.dto;

import com.fnb.membership.fnbmembership.domain.PointOrderType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * PointOrderService에서 사용
 * PointOrder의 생성 결과를 저장하기 위함 DTO
 */
@Data
@Builder
public class CreatePointOrderResultDto {

    public String pointOrderId;
    public PointOrderType pointOrderType;
    public Long requestedPointAmount;
    public LocalDateTime approvedAt;
}
