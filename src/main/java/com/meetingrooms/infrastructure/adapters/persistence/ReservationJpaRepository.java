package com.meetingrooms.infrastructure.adapters.persistence;

import com.meetingrooms.domain.model.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface ReservationJpaRepository extends JpaRepository<ReservationJpaEntity, Long> {

    @Query("""
            select case when count(r) > 0 then true else false end
            from ReservationJpaEntity r
            where r.roomId = :roomId
              and r.status = :status
              and r.startAt < :endAt
              and r.endAt > :startAt
            """)
    boolean existsActiveOverlap(
            @Param("roomId") Long roomId,
            @Param("status") ReservationStatus status,
            @Param("startAt") OffsetDateTime startAt,
            @Param("endAt") OffsetDateTime endAt
    );

    @Query("""
            select r from ReservationJpaEntity r
            where (:roomId is null or r.roomId = :roomId)
              and (:startFrom is null or r.startAt >= :startFrom)
              and (:startTo is null or r.startAt < :startTo)
            """)
    Page<ReservationJpaEntity> findFiltered(
            @Param("roomId") Long roomId,
            @Param("startFrom") OffsetDateTime startFrom,
            @Param("startTo") OffsetDateTime startTo,
            Pageable pageable
    );

    @Query("""
            select count(r) from ReservationJpaEntity r
            where (:roomId is null or r.roomId = :roomId)
              and (:startFrom is null or r.startAt >= :startFrom)
              and (:startTo is null or r.startAt < :startTo)
            """)
    long countFiltered(
            @Param("roomId") Long roomId,
            @Param("startFrom") OffsetDateTime startFrom,
            @Param("startTo") OffsetDateTime startTo
    );
}
