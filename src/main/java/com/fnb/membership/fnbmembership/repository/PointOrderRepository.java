package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.PointOrder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PointOrder Entity Repository
 */
@Repository
public interface PointOrderRepository extends JpaRepository<PointOrder, UUID> {

    @Query("SELECT po " +
            "FROM PointOrder po " +
            "WHERE po.member.id = :memberId " +
            "AND po.approvedAt " +
            "BETWEEN :startTime AND :endTime")
    Slice<PointOrder> findSliceByMemberIdAndTime(UUID memberId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
