package com.fnb.membership.fnbmembership.controller;

import com.fnb.membership.fnbmembership.dto.CheckedMemberDto;
import com.fnb.membership.fnbmembership.dto.SearchPointOrderByPhone;
import com.fnb.membership.fnbmembership.dto.SearchPointOrderDto;
import com.fnb.membership.fnbmembership.dto.SearchedPointOrderDto;
import com.fnb.membership.fnbmembership.exception.NoSuchMemberException;
import com.fnb.membership.fnbmembership.service.MemberService;
import com.fnb.membership.fnbmembership.service.PointOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("fnb/membership")
@RequiredArgsConstructor
@Slf4j
public class PointOrderServiceController {

    private final PointOrderService pointOrderService;

    private final MemberService memberService;

    @Operation(summary = "fnb membership searching pointOrders",
            description = "fnb membership 포인트 이력 조회 기능입니다. " +
                    "기존 회원의 휴대폰 번호와 조회기간을 통해 포인트 이력을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @GetMapping("pointorder/{phone}/byphone")
    public ResponseEntity<SearchPointOrderByPhone.Response> searchPointOrderByPhone(
            @PathVariable String phone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @PageableDefault(size=100, sort="approvedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("searchPointOrderByPhone requested. phone=" + phone);

        try {
            CheckedMemberDto checkedMemberDto = memberService.checkMemberByPhone(phone);

            SearchPointOrderDto searchPointOrderDto = SearchPointOrderDto.builder()
                    .memberId(checkedMemberDto.getId())
                    .startTime(startTime)
                    .endTime(endTime)
                    .pageable(pageable)
                    .build();

            Slice<SearchedPointOrderDto> searchedPointOrderDtos = pointOrderService.searchPointOrder(searchPointOrderDto);

            List<SearchPointOrderByPhone.History> histories = searchedPointOrderDtos.get()
                    .map(dto -> SearchPointOrderByPhone.History.builder()
                            .approvedAt(dto.getApprovedAt())
                            .pointOrderType(dto.getPointOrderType())
                            .brandName(dto.getBrandName())
                            .storeName(dto.getStoreName())
                            .requestedAmount(dto.getRequestedAmount())
                            .build()
                    ).collect(Collectors.toList());

            SearchPointOrderByPhone.Response response = SearchPointOrderByPhone.Response.builder()
                    .history(histories)
                    .build();

            log.info("searchPointOrderByPhone completed. response=" + response.toString());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchMemberException nsm) {
            log.error("NoSuchMemberException occurs phone=" + phone);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Unknown Exception occurs phone=" + phone);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @Operation(summary = "fnb membership searching pointOrders",
            description = "fnb membership 포인트 이력 조회 기능입니다. " +
                    "기존 회원의 바코드와 조회기간을 통해 포인트 이력을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @GetMapping("pointorder/{barcode}/bybarcode")
    public ResponseEntity<SearchPointOrderByPhone.Response> searchPointOrderByBarcode(
            @PathVariable String barcode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @PageableDefault(size=100, sort="approvedAt", direction = Sort.Direction.DESC) Pageable pageable) {

        log.info("searchPointOrderByPhone requested. barcode=" + barcode);

        try {
            CheckedMemberDto checkedMemberDto = memberService.checkMemberByBarcode(barcode);

            SearchPointOrderDto searchPointOrderDto = SearchPointOrderDto.builder()
                    .memberId(checkedMemberDto.getId())
                    .startTime(startTime)
                    .endTime(endTime)
                    .pageable(pageable)
                    .build();

            Slice<SearchedPointOrderDto> searchedPointOrderDtos = pointOrderService.searchPointOrder(searchPointOrderDto);

            List<SearchPointOrderByPhone.History> histories = searchedPointOrderDtos.get()
                    .map(dto -> SearchPointOrderByPhone.History.builder()
                            .approvedAt(dto.getApprovedAt())
                            .pointOrderType(dto.getPointOrderType())
                            .brandName(dto.getBrandName())
                            .storeName(dto.getStoreName())
                            .requestedAmount(dto.getRequestedAmount())
                            .build()
                    ).collect(Collectors.toList());

            SearchPointOrderByPhone.Response response = SearchPointOrderByPhone.Response.builder()
                    .history(histories)
                    .build();

            log.info("searchPointOrderByPhone completed. response=" + response.toString());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (NoSuchMemberException nsm) {
            log.error("NoSuchMemberException occurs barcode=" + barcode);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            log.error("Unknown Exception occurs barcode=" + barcode);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
