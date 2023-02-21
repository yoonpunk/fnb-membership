package com.fnb.membership.fnbmembership.controller;

import com.fnb.membership.fnbmembership.dto.EarnPointOrderByBarcode;
import com.fnb.membership.fnbmembership.dto.EarnPointOrderByPhone;
import com.fnb.membership.fnbmembership.dto.RequestEarnPointOrderResultDto;
import com.fnb.membership.fnbmembership.exception.NoSuchBrandException;
import com.fnb.membership.fnbmembership.exception.NoSuchMemberException;
import com.fnb.membership.fnbmembership.exception.NoSuchStoreException;
import com.fnb.membership.fnbmembership.service.RequestPointOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.OptimisticLockException;

@RestController
@RequestMapping("fnb/membership")
@RequiredArgsConstructor
@Slf4j
public class RequestPointOrderServiceController {

    private final RequestPointOrderService requestPointOrderService;

    @Operation(summary = "fnb membership pointorder earning",
            description = "fnb membership 포인트 적립 API 입니다. " +
                    "기존 회원의 휴대폰 번호를 통해 포인트를 적립합니다. " +
                    "같은 브랜드의 점포에서는 포인트를 통합하여 적립할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @PostMapping("pointorder/byphone")
    public ResponseEntity<EarnPointOrderByPhone.Response> earnPointOrderByPhone(@RequestBody EarnPointOrderByPhone.Request request) {

        log.info("earnPointOrderByPhone requested. request="+ request.toString());

        try {
            RequestEarnPointOrderResultDto resultDto = requestPointOrderService.createEarnPointOrderByPhone(
                    request.getPhone(), request.getPointAmount(), request.getStoreId());

            EarnPointOrderByPhone.Response response = EarnPointOrderByPhone.Response.builder()
                    .result("success").build();

            log.info("earnPointOrderByPhone completed. response="+ response.toString());

            // 결과 리턴
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (NoSuchMemberException nme) {
            log.error("NoSuchMemberException occurs request=" + request.toString());
            EarnPointOrderByPhone.Response response = EarnPointOrderByPhone.Response.builder()
                    .result("No Such Member Information").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (NoSuchStoreException nsse) {
            log.error("NoSuchStoreException occurs request=" + request.toString());
            EarnPointOrderByPhone.Response response = EarnPointOrderByPhone.Response.builder()
                    .result("No Such Store Information").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (NoSuchBrandException nsbe) {
            log.error("NoSuchBrandException occurs request=" + request.toString());
            EarnPointOrderByPhone.Response response = EarnPointOrderByPhone.Response.builder()
                    .result("Invalid Brand Information").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException iae) {
            log.error("IllegalArgumentException occurs request=" + request.toString());
            EarnPointOrderByPhone.Response response = EarnPointOrderByPhone.Response.builder()
                    .result("Illegal Argument requested.").build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (OptimisticLockException ole) {
            log.error("OptimisticLockException occurs request=" + request.toString());
            EarnPointOrderByPhone.Response response = EarnPointOrderByPhone.Response.builder()
                    .result("Concurrent point access occurred.").build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            log.error("Unknown Exception occurs request=" + request.toString());
            EarnPointOrderByPhone.Response response = EarnPointOrderByPhone.Response.builder()
                    .result("Unknown Exception occurs request.").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @Operation(summary = "fnb membership pointorder earning",
            description = "fnb membership 포인트 적립 API 입니다. " +
                    "기존 회원의 바코드 번호를 통해 포인트를 적립합니다. " +
                    "같은 브랜드의 점포에서는 포인트를 통합하여 적립할 수 있습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @PostMapping("pointorder/bybarcode")
    public ResponseEntity<EarnPointOrderByBarcode.Response> earnPointOrderByBarcode(@RequestBody EarnPointOrderByBarcode.Request request) {

        log.info("earnPointOrderByBarcode requested. request="+ request.toString());

        try {
            RequestEarnPointOrderResultDto resultDto = requestPointOrderService.createEarnPointOrderByBarcode(
                    request.getBarcode(), request.getPointAmount(), request.getStoreId());

            EarnPointOrderByBarcode.Response response = EarnPointOrderByBarcode.Response.builder()
                    .result("success").build();

            log.info("earnPointOrderByBarcode completed. response="+ response.toString());

            // 결과 리턴
            return ResponseEntity.status(HttpStatus.OK).body(response);

        } catch (NoSuchMemberException nme) {
            log.error("NoSuchMemberException occurs request=" + request.toString());
            EarnPointOrderByBarcode.Response response = EarnPointOrderByBarcode.Response.builder()
                    .result("No Such Member Information").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (NoSuchStoreException nsse) {
            log.error("NoSuchStoreException occurs request=" + request.toString());
            EarnPointOrderByBarcode.Response response = EarnPointOrderByBarcode.Response.builder()
                    .result("No Such Store Information").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (NoSuchBrandException nsbe) {
            log.error("NoSuchBrandException occurs request=" + request.toString());
            EarnPointOrderByBarcode.Response response = EarnPointOrderByBarcode.Response.builder()
                    .result("Invalid Brand Information").build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (IllegalArgumentException iae) {
            log.error("IllegalArgumentException occurs request=" + request.toString());
            EarnPointOrderByBarcode.Response response = EarnPointOrderByBarcode.Response.builder()
                    .result("Illegal Argument requested.").build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (OptimisticLockException ole) {
            log.error("OptimisticLockException occurs request=" + request.toString());
            EarnPointOrderByBarcode.Response response = EarnPointOrderByBarcode.Response.builder()
                    .result("Concurrent point access occurred.").build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            log.error("Unknown Exception occurs request=" + request.toString());
            EarnPointOrderByBarcode.Response response = EarnPointOrderByBarcode.Response.builder()
                    .result("Unknown Exception occurs request.").build();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
