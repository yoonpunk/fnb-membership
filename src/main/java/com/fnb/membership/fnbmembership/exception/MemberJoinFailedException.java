package com.fnb.membership.fnbmembership.exception;

/**
 * 신규 회원 정보 생성 실패 시 발생하는 예외
 */
public class MemberJoinFailedException extends RuntimeException {
    public MemberJoinFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
