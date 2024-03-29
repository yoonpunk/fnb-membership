package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Point Entity Repository
 */
@Repository
public interface PointRepository extends JpaRepository<Point, Long> {

    @Lock(LockModeType.OPTIMISTIC)
    @Query(
            "SELECT p " +
            "FROM Point p " +
            "JOIN FETCH p.member m " +
            "JOIN FETCH p.brand b " +
            "WHERE m.id = :memberId " +
            "AND b.id = :brandId"
    )
    Optional<Point> findByMemberIdAndBrandIdWithOptimisticLock(Long memberId, Long brandId);
}
