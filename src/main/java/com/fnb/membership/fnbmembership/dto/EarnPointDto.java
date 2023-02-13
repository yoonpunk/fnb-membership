package com.fnb.membership.fnbmembership.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EarnPointDto {

    private String storeId;
    private String brandId;
    private String memberId;
    private Long amount;
}
