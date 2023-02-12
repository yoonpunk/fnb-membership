package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.exception.MemberJoinFailedException;
import com.fnb.membership.fnbmembership.exception.NoSuchMemberException;
import com.fnb.membership.fnbmembership.domain.Member;
import com.fnb.membership.fnbmembership.dto.CheckedMemberDto;
import com.fnb.membership.fnbmembership.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * 회원 정보를 관리하기 위한 서비스
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원 가입 요청 API
     * 회원 전화번호를 받아 회원 가입을 진행
     * 회원이 없을 경우, 가입 진행 후 회원 정보를 전달
     * 회원이 있을 경우, 조회된 회원 정보를 전달
     * 회원 생성 실패 시, 예외 발생 처리
     * @param phone
     * @return CheckedMemberDto
     */
    @Transactional(readOnly = false)
    public CheckedMemberDto join(String phone) throws MemberJoinFailedException {

        Optional<Member> member = memberRepository.findByPhone(phone);

        // 기존 회원이 있을 경우, 조회 된 회원 정보를 리턴
        if (member.isPresent()) {
            Member existMember = member.get();
            return CheckedMemberDto.builder()
                    .id(existMember.getId().toString())
                    .phone(existMember.getPhone())
                    .barcode(existMember.getBarcode())
                    .build();
        }

        // 신규 회원일 경우, 회원 가입
        try {
            Member newMember = Member.createMember(phone);
            newMember = memberRepository.save(newMember);

            return CheckedMemberDto.builder()
                    .id(newMember.getId().toString())
                    .phone(newMember.getPhone())
                    .barcode(newMember.getBarcode())
                    .build();

        } catch (Exception e) {
            throw new MemberJoinFailedException(e.getMessage(), e.getCause());
        }
    }

    /**
     * 회원 정보 확인 요청 API
     * 회원 전화번호를 받아 존재하는 회원인지 확인
     * 성공 시, 요청된 회원 전화번호와 생성된 바코드 정보를 전달
     * 실패 시, NoSuchMemberException 예외 발생
     * @param phone
     * @return CheckedMemberDto
     */
    public CheckedMemberDto checkMemberByPhone(String phone) throws NoSuchMemberException {

        // 회원 조회
        Optional<Member> member = memberRepository.findByPhone(phone);

        // 회원 존재 시, 회원 정보 리턴
        if (member.isPresent()) {
            Member existMember = member.get();
            return CheckedMemberDto.builder()
                            .id(existMember.getId().toString())
                            .phone(existMember.getPhone())
                            .barcode(existMember.getBarcode())
                            .build();
        } else { // 없을 경우, 예외 발생
            throw new NoSuchMemberException();
        }
    };

    /**
     * 회원 정보 확인 요청 API
     * 회원 바코드를 받아 존재하는 회원인지 확인
     * 성공 시, 요청된 회원 전화번호와 생성된 바코드 정보를 전달
     * 실패 시, NoSuchMemberException 예외 발생
     * @param barcode
     * @return CheckedMemberDto
     */
    public CheckedMemberDto checkMemberByBarcode(String barcode) throws NoSuchMemberException {

        // 회원 조회
        Optional<Member> member = memberRepository.findByBarcode(barcode);

        // 회원 존재 시, 회원 정보 리턴
        if (member.isPresent()) {
            Member existMember = member.get();
            return CheckedMemberDto.builder()
                            .id(existMember.getId().toString())
                            .phone(existMember.getPhone())
                            .barcode(existMember.getBarcode())
                            .build();
        } else { // 없을 경우, 예외 발생
            throw new NoSuchMemberException();
        }
    };
}
