package com.fnb.membership.fnbmembership.repository;

import com.fnb.membership.fnbmembership.domain.PointOrder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface PointOrderRepository {

    /**
     * saves a given entity.
     * @param entity an entity of PointOrder class
     * @return the saved entity.
     */
    <S extends PointOrder> S save(S entity);

    /**
     * Retrives an entity by its id.
     * @param id
     * @return the entity with the given id or empty Optional if none found.
     */
    Optional<PointOrder> findById(UUID id);

    /**
     * finds PointOrder entities with the given parameters via paging.
     * @param memberId
     * @param startTime
     * @param endTime
     * @param page
     * @param size
     * @return
     */
    List<PointOrder> findByMemberIdAndTimeOrderByApprovedAtDescWithPaging(UUID memberId, LocalDateTime startTime, LocalDateTime endTime, int page, int size);
}
