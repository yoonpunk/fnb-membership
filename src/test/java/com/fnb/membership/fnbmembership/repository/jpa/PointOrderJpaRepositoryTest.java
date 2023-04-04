package com.fnb.membership.fnbmembership.repository.jpa;

import com.fnb.membership.fnbmembership.domain.Member;
import com.fnb.membership.fnbmembership.domain.PointOrder;
import com.fnb.membership.fnbmembership.domain.PointOrderType;
import com.fnb.membership.fnbmembership.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
class PointOrderJpaRepositoryTest {

    @Autowired
    PointOrderJpaRepository sut;

    @Autowired
    MemberRepository memberRepository;

    // These variables are used to retrieve data.
    LocalDateTime startTime;
    LocalDateTime endTime;
    Member member;

    // This list is used to verify retrieved data.
    List<PointOrder> expectedPointOrders;

    @Test
    @DisplayName("This is a test to verify the results of the first retrieved page sorted by approvedAt DESC")
    void testFindByMemberIdAndTimeWithPageableVerifyingFirstPage() {

        // arrange
        // Called the setup method directly in the test code to explicitly show it, without using @BeforeEach.
        init();
        Pageable pageable = PageRequest.of(0, 3, Sort.by("approvedAt").descending());

        // act
        List<PointOrder> actual = sut.findByMemberIdAndTimeWithPageable(member.getId(), startTime, endTime, pageable);

        // assert
        assertThat(actual.size()).isEqualTo(3);
        // Check descending of expectedPointOrders
        assertThat(actual.get(0)).isEqualTo(expectedPointOrders.get(4));
        assertThat(actual.get(1)).isEqualTo(expectedPointOrders.get(3));
        assertThat(actual.get(2)).isEqualTo(expectedPointOrders.get(2));
    }

    /*
     * setup method.
     */
    private void init() {

        // Assign the time when initial data preparation started to a variable.
        startTime = LocalDateTime.now();

        member = memberRepository.save(Member.createMember("01012345678"));

        expectedPointOrders = new ArrayList<PointOrder>();

        PointOrder pointOrder1 = sut.save(PointOrder.createPointOrder(
                member, "MOONBUCKS", "MOONBUCKS 매탄점", PointOrderType.EARN, 5000L));
        PointOrder pointOrder2 = sut.save(PointOrder.createPointOrder(
                member, "MOONBUCKS", "MOONBUCKS 매탄점", PointOrderType.EARN, 4000L));
        PointOrder pointOrder3 = sut.save(PointOrder.createPointOrder(
                member, "MOONBUCKS", "MOONBUCKS 매탄점", PointOrderType.EARN, 3000L));
        PointOrder pointOrder4 = sut.save(PointOrder.createPointOrder(
                member, "MOONBUCKS", "MOONBUCKS 매탄점", PointOrderType.EARN, 2000L));
        PointOrder pointOrder5 = sut.save(PointOrder.createPointOrder(
                member, "MOONBUCKS", "MOONBUCKS 매탄점", PointOrderType.EARN, 1000L));

        expectedPointOrders.add(pointOrder1);
        expectedPointOrders.add(pointOrder2);
        expectedPointOrders.add(pointOrder3);
        expectedPointOrders.add(pointOrder4);
        expectedPointOrders.add(pointOrder5);

        // Assign the time when initial data preparation was completed to a variable.
        endTime = LocalDateTime.now();
    }
}