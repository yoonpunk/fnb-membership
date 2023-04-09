package com.fnb.membership.fnbmembership.repository.jpa;

import com.fnb.membership.fnbmembership.domain.Member;
import com.fnb.membership.fnbmembership.domain.PointOrder;
import com.fnb.membership.fnbmembership.domain.PointOrderType;
import com.fnb.membership.fnbmembership.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PointOrderJpaRepositoryTest {

    // To use JPA's auto implementation, Declare @Autowired.
    @Autowired
    PointOrderJpaRepository sut;

    @Autowired
    MemberRepository memberRepository;

    // This is a test to verify the results of the first retrieved page sorted by approvedAt DESC
    @Test
    void testFindByMemberIdAndTimeWithPageableVerifyingFirstPage() {

        // arrange
        // Called the setup method directly in the test code to explicitly show it, without using @BeforeEach.
        List<PointOrder> expectedPointOrders = setUpFivePointOrdersForSearchingTest();
        Member member = memberRepository.findByPhone("01012345678").get();

        Pageable pageable = PageRequest.of(0, 3, Sort.by("approvedAt").descending());
        LocalDateTime startTime = LocalDateTime.of(2023, 3, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 3, 1, 10, 7, 0);

        // act
        List<PointOrder> actual = sut.findByMemberIdAndTimeWithPageable(member.getId(), startTime, endTime, pageable);

        // assert
        assertThat(actual.size()).isEqualTo(3);

        // Sort expectedPointOrders by DESC and Check expectedPointOrders.
        Collections.sort(expectedPointOrders, (o1, o2) -> o2.getApprovedAt().compareTo(o1.getApprovedAt()));
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedPointOrders.subList(0, 3));
    }

    // This is a test to verify the results of the second(actually last) retrieved page sorted by approvedAt DESC
    @Test
    void testFindByMemberIdAndTimeWithPageableVerifyingSecondPage() {

        // arrange
        // Called the setup method directly in the test code to explicitly show it, without using @BeforeEach.
        List<PointOrder> expectedPointOrders = setUpFivePointOrdersForSearchingTest();
        Member member = memberRepository.findByPhone("01012345678").get();

        Pageable pageable = PageRequest.of(1, 3, Sort.by("approvedAt").descending());
        LocalDateTime startTime = LocalDateTime.of(2023, 3, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 3, 1, 10, 7, 0);

        // act
        List<PointOrder> actual = sut.findByMemberIdAndTimeWithPageable(member.getId(), startTime, endTime, pageable);

        // assert
        assertThat(actual.size()).isEqualTo(2);

        // Sort expectedPointOrders by DESC and Check expectedPointOrders.
        Collections.sort(expectedPointOrders, (o1, o2) -> o2.getApprovedAt().compareTo(o1.getApprovedAt()));
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedPointOrders.subList(3, 5));
    }

    // This is a test to verify the results of the first retrieved page sorted by approvedAt DESC
    @Test
    void testFindByMemberIdAndTimeOrderByApprovedAtDescWithPagingVerifyingFirstPage() {

        // arrange
        // Called the setup method directly in the test code to explicitly show it, without using @BeforeEach.
        List<PointOrder> expectedPointOrders = setUpFivePointOrdersForSearchingTest();
        Member member = memberRepository.findByPhone("01012345678").get();

        LocalDateTime startTime = LocalDateTime.of(2023, 3, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 3, 1, 10, 7, 0);

        // act
        List<PointOrder> actual = sut.findByMemberIdAndTimeOrderByApprovedAtDescWithPaging(
                member.getId(), startTime, endTime, 0, 3);

        // assert
        assertThat(actual.size()).isEqualTo(3);

        // Sort expectedPointOrders by DESC and Check expectedPointOrders.
        Collections.sort(expectedPointOrders, (o1, o2) -> o2.getApprovedAt().compareTo(o1.getApprovedAt()));
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedPointOrders.subList(0, 3));
    }

    // This is a test to verify the results of the second(actually last) retrieved page sorted by approvedAt DESC
    @Test
    void testFindByMemberIdAndTimeOrderByApprovedAtDescWithPagingVerifyingSecondPage() {

        // arrange
        // Called the setup method directly in the test code to explicitly show it, without using @BeforeEach.
        List<PointOrder> expectedPointOrders = setUpFivePointOrdersForSearchingTest();
        Member member = memberRepository.findByPhone("01012345678").get();

        LocalDateTime startTime = LocalDateTime.of(2023, 3, 1, 10, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2023, 3, 1, 10, 7, 0);

        // act
        List<PointOrder> actual = sut.findByMemberIdAndTimeOrderByApprovedAtDescWithPaging(
                member.getId(), startTime, endTime, 1, 3);

        // assert
        assertThat(actual.size()).isEqualTo(2);

        // Sort expectedPointOrders by DESC and Check expectedPointOrders.
        Collections.sort(expectedPointOrders, (o1, o2) -> o2.getApprovedAt().compareTo(o1.getApprovedAt()));
        assertThat(actual).usingRecursiveComparison().isEqualTo(expectedPointOrders.subList(3, 5));
    }

    /**
     * A Setup method.
     * Create five PointOrder objects at 2023-03-01 10:00:01 ~ 2023-03-01 10:00:06
     * Return created five pointOrders.
     */
    private List<PointOrder> setUpFivePointOrdersForSearchingTest() {

        Member member = memberRepository.save(Member.createMember(
                UUID.randomUUID(),
                "01012345678",
                Member.createBarcode(),
                LocalDateTime.of(2023, 3, 1, 10, 1, 0))
        );

        PointOrder pointOrder1 = sut.save(
                PointOrder.createPointOrder(
                        UUID.randomUUID(),
                        member.getId(),
                        "MOONBUCKS",
                        "MOONBUCKS 매탄점",
                        PointOrderType.EARN,
                        5000L,
                        LocalDateTime.of(2023, 3, 1, 10, 2, 0)
                )
        );

        PointOrder pointOrder2 = sut.save(
                PointOrder.createPointOrder(
                        UUID.randomUUID(),
                        member.getId(),
                        "MOONBUCKS",
                        "MOONBUCKS 매탄점",
                        PointOrderType.EARN,
                        4000L,
                        LocalDateTime.of(2023, 3, 1, 10, 3, 0)
                )
        );

        PointOrder pointOrder3 = sut.save(
                PointOrder.createPointOrder(
                        UUID.randomUUID(),
                        member.getId(),
                        "MOONBUCKS",
                        "MOONBUCKS 매탄점",
                        PointOrderType.EARN,
                        3000L,
                        LocalDateTime.of(2023, 3, 1, 10, 4, 0)
                )
        );

        PointOrder pointOrder4 = sut.save(
                PointOrder.createPointOrder(
                        UUID.randomUUID(),
                        member.getId(),
                        "MOONBUCKS",
                        "MOONBUCKS 매탄점",
                        PointOrderType.EARN,
                        2000L,
                        LocalDateTime.of(2023, 3, 1, 10, 5, 0)
                )
        );

        PointOrder pointOrder5 = sut.save(
                PointOrder.createPointOrder(
                        UUID.randomUUID(),
                        member.getId(),
                        "MOONBUCKS",
                        "MOONBUCKS 매탄점",
                        PointOrderType.EARN,
                        1000L,
                        LocalDateTime.of(2023, 3, 1, 10, 6, 0)
                )
        );

        return new ArrayList<PointOrder>(Arrays.asList(pointOrder1, pointOrder2, pointOrder3, pointOrder4, pointOrder5));
    }
}