package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Brand Entity Repository
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, UUID> {
}
