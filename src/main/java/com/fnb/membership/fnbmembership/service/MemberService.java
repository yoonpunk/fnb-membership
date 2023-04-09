package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Member;
import com.fnb.membership.fnbmembership.dto.CheckedMemberDto;
import com.fnb.membership.fnbmembership.exception.MemberJoinFailedException;
import com.fnb.membership.fnbmembership.exception.NoSuchMemberException;
import com.fnb.membership.fnbmembership.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * A service to manage members.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * A method to create a new member using their phone number.
     * If the member doesn't exist, create a new member and return it.
     * If the member already exists, return the existing member.
     * @param phone
     * @return
     * @throws MemberJoinFailedException
     */
    @Transactional(readOnly = false)
    public CheckedMemberDto join(String phone) throws MemberJoinFailedException {

        log.info("join requested. phone=" + phone);

        Optional<Member> member = memberRepository.findByPhone(phone);

        // If the member already exists, return the existing member.
        if (member.isPresent()) {
            Member existMember = member.get();

            log.info("member already exists. phone=" + phone);

            return CheckedMemberDto.builder()
                    .id(existMember.getId())
                    .phone(existMember.getPhone())
                    .barcode(existMember.getBarcode())
                    .build();
        }

        // If the member doesn't exist, create a new member and return it.
        try {
            Member newMember = Member.createMember(UUID.randomUUID(), phone, Member.createBarcode(), LocalDateTime.now());
            newMember = memberRepository.save(newMember);

            log.info("creating new member is completed. phone=" + newMember.getPhone());
            return CheckedMemberDto.builder()
                    .id(newMember.getId())
                    .phone(newMember.getPhone())
                    .barcode(newMember.getBarcode())
                    .build();

        } catch (Exception e) {
            log.error("unexpected error occurs. joining a member is failed. phone=" + phone);
            throw new MemberJoinFailedException(e.getMessage(), e.getCause());
        }
    }

    /**
     * A method to check whether a member exists using their phone number.
     * @param phone
     * @return
     * @throws NoSuchMemberException
     */
    public CheckedMemberDto checkMemberByPhone(String phone) throws NoSuchMemberException {

        log.info("checkMemberByPhone is requested. phone=" + phone);

        // Searching the member by phone number.
        Optional<Member> member = memberRepository.findByPhone(phone);

        // If the member already exists, return the existing member.
        if (member.isPresent()) {
            Member existMember = member.get();

            log.info("member is valid. phone=" + phone);

            return CheckedMemberDto.builder()
                            .id(existMember.getId())
                            .phone(existMember.getPhone())
                            .barcode(existMember.getBarcode())
                            .build();
        } else { // If the member doesn't exist, throw a NoSuchMemberException.
            log.error("member is invalid. phone=" + phone);
            throw new NoSuchMemberException();
        }
    };

    /**
     * A method to check whether a member exists using their barcode number.
     * @param barcode
     * @return
     * @throws NoSuchMemberException
     */
    public CheckedMemberDto checkMemberByBarcode(String barcode) throws NoSuchMemberException {

        log.info("checkMemberByBarcode is requested. barcode=" + barcode);

        // Searching the member by barcode number.
        Optional<Member> member = memberRepository.findByBarcode(barcode);

        // If the member already exists, return it.
        if (member.isPresent()) {
            Member existMember = member.get();

            log.info("member is valid. barcode=" + barcode);
            return CheckedMemberDto.builder()
                            .id(existMember.getId())
                            .phone(existMember.getPhone())
                            .barcode(existMember.getBarcode())
                            .build();
        } else { // If the member doesn't exist, throw a NoSuchMemberException.
            log.error("member is invalid. barcode=" + barcode);
            throw new NoSuchMemberException();
        }
    };
}
