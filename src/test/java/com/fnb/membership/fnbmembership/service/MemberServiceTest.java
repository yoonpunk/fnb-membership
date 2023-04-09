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

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberServiceTest {

    MemberService sut;

    @Autowired
    MemberRepository memberRepository;

    @BeforeEach
    void setUpSut() {
        sut = new MemberService(memberRepository);
    }

    @Test
    void testJoinUsingNewMember() {

        // arrange
        String expectedPhone = "01012345678";
        Member expectedMember = Member.createMember(UUID.randomUUID(), expectedPhone, Member.createBarcode(), LocalDateTime.now());
        expectedMember = memberRepository.save(expectedMember);

        // act
        CheckedMemberDto checkedMemberDto = sut.join(expectedPhone);

        // assert
        assertThat(checkedMemberDto).usingRecursiveComparison().isEqualTo(expectedMember);
    }

    @Test
    void testCheckMemberByPhoneIfMemberExists() {

        // arrange
        String expectedPhone = "01012345678";
        Member expectedMember = Member.createMember(UUID.randomUUID(), expectedPhone, Member.createBarcode(), LocalDateTime.now());
        expectedMember = memberRepository.save(expectedMember);

        // act
        CheckedMemberDto checkedMemberDto = sut.checkMemberByPhone(expectedPhone);

        // assert
        assertThat(checkedMemberDto).usingRecursiveComparison().isEqualTo(expectedMember);
    }

    @Test
    void testCheckMemberByBarcodeIfMemberExists() {

        // arrange
        String expectedPhone = "01012345678";
        Member expectedMember = Member.createMember(UUID.randomUUID(), expectedPhone, Member.createBarcode(), LocalDateTime.now());
        expectedMember = memberRepository.save(expectedMember);

        // act
        CheckedMemberDto checkedMemberDto = sut.checkMemberByBarcode(expectedMember.getBarcode());

        // assert
        Optional<Member> memberById = memberRepository.findById(checkedMemberDto.getId());
        assertThat(memberById).isPresent();
        assertThat(checkedMemberDto).usingRecursiveComparison().isEqualTo(memberById.get());
    }
}