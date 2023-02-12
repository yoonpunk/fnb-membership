package com.fnb.membership.fnbmembership.controller;

import com.fnb.membership.fnbmembership.dto.CheckedMemberDto;
import com.fnb.membership.fnbmembership.dto.Join;
import com.fnb.membership.fnbmembership.exception.MemberJoinFailedException;
import com.fnb.membership.fnbmembership.service.MemberService;
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

import javax.validation.Valid;

@RestController
@RequestMapping("fnb/membership")
@RequiredArgsConstructor
@Slf4j
public class MemberServiceController {

    private final MemberService memberService;

    @Operation(summary = "fnb member join", description = "fnb membership 회원가입 API 입니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK"),
    })
    @PostMapping("/join")
    public ResponseEntity<Join.Response> join(@Valid @RequestBody Join.Request request) {
        log.info("member join requested. request=" + request.toString());

        try {
            // 회원 가입 성공 시, 결과 리턴
            CheckedMemberDto checkedMemberDto = memberService.join(request.getPhone());
            log.info("member join completed. checkedMemberDto=" + checkedMemberDto.toString());

            return ResponseEntity.status(HttpStatus.OK).body(
                    Join.Response.builder()
                            .phone(checkedMemberDto.getPhone())
                            .barcode(checkedMemberDto.getBarcode())
                            .build()
            );

        } catch (MemberJoinFailedException mje) { // 회원 가입 실패 시, 실패 로그
            log.error("member join failed. request=" + request.toString());
        } catch (Exception e) { // 회원 가입 실패 시(예상치 못한 에러), 실패 로그
            log.error("member join failed. unexpected error. request=" + request.toString(), e);
        }

        // 실패 결과 리턴
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                Join.Response.builder()
                        .phone(request.getPhone())
                        .barcode(null)
                        .build()
        );
    }
}
