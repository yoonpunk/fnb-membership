package com.fnb.membership.fnbmembership.dto;

import com.fnb.membership.fnbmembership.domain.PointOrderType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

@Data
@Builder
public class SearchPointOrderDto {

    private String memberId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Pageable pageable;

}
