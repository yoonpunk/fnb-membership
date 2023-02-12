package com.fnb.membership.fnbmembership.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 회원 가입 시 사용하는 Request/Response 객체
 */
public class Join {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Request {
        @NotBlank(message = "휴대전화번호를 반드시 숫자만 입력해주세요.")
        @Pattern(regexp = "01(0|1|6|7|8|9)([0-9]{3,4})([0-9]{4})",
                message = "휴대전화패턴이 맞지 않습니다. 올바른 번호를 숫자만 입력해주세요.")
        @Schema(description = "회원 가입자 휴대전화번호", defaultValue = "01012345678")
        private String phone;

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Response {
        private String phone;
        private String barcode;

    }
}
