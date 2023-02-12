package com.fnb.membership.fnbmembership.service;

import com.fnb.membership.fnbmembership.domain.Member;
import com.fnb.membership.fnbmembership.dto.CheckedMemberDto;
import com.fnb.membership.fnbmembership.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;

    MemberService memberService;

    String existBarcode;

    @BeforeEach
    void init() {
        memberService = new MemberService(memberRepository);

        // 초기 데이터 생성
        Member newMember = Member.createMember("01000000000");
        memberRepository.save(newMember);
        existBarcode = newMember.getBarcode();
    }

    @Test
    void join_신규_생성_성공() {

        // given
        String phone = "01012345678";

        // when
        CheckedMemberDto checkedMemberDto = memberService.join(phone);

        // then
        Optional<Member> memberByPhone = memberRepository.findByPhone(phone);
        assertThat(memberByPhone).isPresent();
        assertThat(memberByPhone.get().getPhone()).isEqualTo(phone);

        assertThat(checkedMemberDto.getId()).isEqualTo(memberByPhone.get().getId().toString());
        assertThat(checkedMemberDto.getPhone()).isEqualTo(memberByPhone.get().getPhone());
        assertThat(checkedMemberDto.getBarcode()).isEqualTo(memberByPhone.get().getBarcode());
    }

//    @Test
//    void join_회원_정보_존재() {
//        // 추후 구현
//    }

//    @Test
//    void join_회원_신규_생성_실패() {
//        // 추후 구현
//    }

    @Test
    void checkMemberByPhone_회원_정보_존재() {

        // given
        String phone = "01000000000";

        // when
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByPhone(phone);

        // then
        Optional<Member> memberById = memberRepository.findById(UUID.fromString(checkedMemberDto.getId()));
        assertThat(memberById).isPresent();
        assertThat(checkedMemberDto.getId()).isEqualTo(memberById.get().getId().toString());
        assertThat(checkedMemberDto.getPhone()).isEqualTo(memberById.get().getPhone());
        assertThat(checkedMemberDto.getBarcode()).isEqualTo(memberById.get().getBarcode());
    }

    @Test
    void checkMemberByBarcode_회원_정보_존재() {

        // given
        //init method에서 생성한 멤버의 바코드정보 사용
        String barcode = existBarcode;

        // when
        CheckedMemberDto checkedMemberDto = memberService.checkMemberByBarcode(barcode);

        // then
        Optional<Member> memberById = memberRepository.findById(UUID.fromString(checkedMemberDto.getId()));
        assertThat(memberById).isPresent();
        assertThat(checkedMemberDto.getId()).isEqualTo(memberById.get().getId().toString());
        assertThat(checkedMemberDto.getPhone()).isEqualTo(memberById.get().getPhone());
        assertThat(checkedMemberDto.getBarcode()).isEqualTo(memberById.get().getBarcode());
    }
}