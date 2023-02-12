package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.PointOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * PointOrder Entity Repository
 */
@Repository
public interface PointOrderRepository extends JpaRepository<PointOrder, UUID> {
}
