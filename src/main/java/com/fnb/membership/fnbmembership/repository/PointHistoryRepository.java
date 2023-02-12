package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * PointHistory Entity Repository
 */
@Repository
public interface PointHistoryRepository extends JpaRepository<PointHistory, UUID> {
}
