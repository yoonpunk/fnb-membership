package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Store Entity Repository
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
}
