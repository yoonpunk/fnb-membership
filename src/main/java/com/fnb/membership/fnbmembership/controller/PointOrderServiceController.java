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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("fnb/membership")
@RequiredArgsConstructor
@Validated
@Slf4j
public class PointOrderServiceController {

    private final PointOrderService pointOrderService;

    private final MemberService memberService;

    @Operation(summary = "A point history searching API.",
            description = "Providing a point history searching service by phone number and period.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @GetMapping("pointorder/{phone}/byphone")
    public ResponseEntity<SearchPointOrderByPhone.Response> searchPointOrderByPhone(
            @PathVariable String phone,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page has to be more than or equal to 0") int page,
            @RequestParam(defaultValue = "100") @Max(value = 1000, message = "size has to be less than or equal to 1000" ) int size) {

        log.info("searchPointOrderByPhone requested. phone=" + phone);

        try {
            CheckedMemberDto checkedMemberDto = memberService.checkMemberByPhone(phone);

            SearchPointOrderDto searchPointOrderDto = SearchPointOrderDto.builder()
                    .memberId(checkedMemberDto.getId())
                    .startTime(startTime)
                    .endTime(endTime)
                    .page(page)
                    .size(size)
                    .build();

            List<SearchedPointOrderDto> searchedPointOrderDtos = pointOrderService.searchPointOrder(searchPointOrderDto);

            List<SearchPointOrderByPhone.History> histories = searchedPointOrderDtos.stream()
                    .map(dto -> SearchPointOrderByPhone.History.builder()
                            .approvedAt(dto.getApprovedAt())
                            .pointOrderType(dto.getPointOrderType())
                            .brandName(dto.getBrandName())
                            .storeName(dto.getStoreName())
                            .requestedAmount(dto.getRequestedAmount())
                            .build())
                    .collect(Collectors.toList());

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

    @Operation(summary = "A point history searching API.",
            description = "Providing a point history searching service by barcode number and period.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @GetMapping("pointorder/{barcode}/bybarcode")
    public ResponseEntity<SearchPointOrderByPhone.Response> searchPointOrderByBarcode(
            @PathVariable String barcode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") @Min(value = 0, message = "page has to be more than or equal to 0") int page,
            @RequestParam(defaultValue = "100") @Max(value = 1000, message = "size has to be less than or equal to 1000") int size) {

        log.info("searchPointOrderByPhone requested. barcode=" + barcode);

        try {
            CheckedMemberDto checkedMemberDto = memberService.checkMemberByBarcode(barcode);

            SearchPointOrderDto searchPointOrderDto = SearchPointOrderDto.builder()
                    .memberId(checkedMemberDto.getId())
                    .startTime(startTime)
                    .endTime(endTime)
                    .page(page)
                    .size(size)
                    .build();

            List<SearchedPointOrderDto> searchedPointOrderDtos = pointOrderService.searchPointOrder(searchPointOrderDto);

            List<SearchPointOrderByPhone.History> histories = searchedPointOrderDtos.stream()
                    .map(dto -> SearchPointOrderByPhone.History.builder()
                            .approvedAt(dto.getApprovedAt())
                            .pointOrderType(dto.getPointOrderType())
                            .brandName(dto.getBrandName())
                            .storeName(dto.getStoreName())
                            .requestedAmount(dto.getRequestedAmount())
                            .build())
                    .collect(Collectors.toList());

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
