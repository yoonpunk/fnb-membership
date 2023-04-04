package com.fnb.membership.fnbmembership.repository.jpa;

import com.fnb.membership.fnbmembership.domain.PointOrder;
import com.fnb.membership.fnbmembership.repository.PointOrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * PointOrder Entity JpaRepository
 */
@Repository
public interface PointOrderJpaRepository extends JpaRepository<PointOrder, UUID>, PointOrderRepository {

    // Override methods area for PointOrderRepository
    @Override
    <S extends PointOrder> S save(S entity);

    @Override
    Optional<PointOrder> findById(UUID id);

    @Override
    default List<PointOrder> findByMemberIdAndTimeOrderByApprovedAtDescWithPaging(UUID memberId, LocalDateTime startTime, LocalDateTime endTime, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("approvedAt").descending());
        return findByMemberIdAndTimeWithPageable(memberId, startTime, endTime, pageable);
    }

    // Query Methods area for automatic implementation by JPA
    @Query("SELECT po " +
            "FROM PointOrder po " +
            "WHERE po.member.id = :memberId " +
            "AND po.approvedAt " +
            "BETWEEN :startTime AND :endTime")
    List<PointOrder> findByMemberIdAndTimeWithPageable(UUID memberId, LocalDateTime startTime, LocalDateTime endTime, Pageable pageable);
}
