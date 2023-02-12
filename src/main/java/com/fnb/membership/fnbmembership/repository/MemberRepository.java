package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Member Entity Repository
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, UUID> {
}
