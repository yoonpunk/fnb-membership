package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Store Entity Repository
 */
@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {

    @Query("SELECT s FROM Store s JOIN FETCH s.brand b WHERE s.id = :id")
    Optional<Store> findByIdWithBrand(UUID id);
}
