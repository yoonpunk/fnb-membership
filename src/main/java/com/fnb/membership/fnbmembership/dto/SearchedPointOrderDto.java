package com.fnb.membership.fnbmembership.dto;

import com.fnb.membership.fnbmembership.domain.PointOrderType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SearchedPointOrderDto {

    private LocalDateTime approvedAt;
    private PointOrderType pointOrderType;
    private String brandName;
    private String storeName;
    private Long requestedAmount;

}
