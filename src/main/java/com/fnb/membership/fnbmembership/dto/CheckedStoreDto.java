package com.fnb.membership.fnbmembership.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 점포 생성 및 체크 결과를 전달하기 위한 DTO
 */
@Data
@Builder
public class CheckedStoreDto {

    private Long brandId;
    private String brandName;
    private Long storeId;
    private String storeName;
}
