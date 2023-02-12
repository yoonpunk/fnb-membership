package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Point Entity Repository
 */
@Repository
public interface PointRepository extends JpaRepository<Point, UUID> {
}
