package com.fnb.membership.fnbmembership.dto;

import lombok.Builder;
import lombok.Data;

/**
 * 회원 가입 및 체크 결과를 전달하기 위한 DTO
 * MemberService -> MemberServiceController
 */
@Data
@Builder
public class CheckedMemberDto {

    private String id;
    private String phone;
    private String barcode;
}
