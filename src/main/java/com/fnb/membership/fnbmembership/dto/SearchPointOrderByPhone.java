package com.fnb.membership.fnbmembership.dto;

import com.fnb.membership.fnbmembership.domain.PointOrderType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Response object for searching point order histories by phone.
 * this object is used in PointOrderServiceController.
 */
public class SearchPointOrderByPhone {

    @Data
    @Builder
    @Schema(name = "SearchPointOrderByPhoneResponse")
    public static class Response {
        private List<History> history;
    }

    @Data
    @Builder
    public static class History {

        private LocalDateTime approvedAt;
        private PointOrderType pointOrderType;
        private String brandName;
        private String storeName;
        private Long requestedAmount;
    }
}
