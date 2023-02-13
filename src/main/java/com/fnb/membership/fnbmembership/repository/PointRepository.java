package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Point Entity Repository
 */
@Repository
public interface PointRepository extends JpaRepository<Point, UUID> {

//    @Lock(LockModeType.OPTIMISTIC)
    @Query(
            "SELECT p " +
            "FROM Point p " +
            "JOIN FETCH p.member m " +
            "JOIN FETCH p.brand b " +
            "WHERE m.id = :memberId " +
            "AND b.id = :brandId"
    )
    Optional<Point> findByMemberIdAndBrandIdWithOptimisticLock(UUID memberId, UUID brandId);
}
