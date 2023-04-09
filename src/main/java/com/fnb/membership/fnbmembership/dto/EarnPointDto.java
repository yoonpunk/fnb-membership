package com.fnb.membership.fnbmembership.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EarnPointDto {

    private Long brandId;
    private Long memberId;
    private Long amount;
}
