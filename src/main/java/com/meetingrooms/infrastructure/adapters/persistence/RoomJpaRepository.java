package com.meetingrooms.infrastructure.adapters.persistence;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, Long> {

    boolean existsByName(String name);

    Page<RoomJpaEntity> findByDeletedFalse(Pageable pageable);

    long countByDeletedFalse();

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select r from RoomJpaEntity r where r.id = :id")
    Optional<RoomJpaEntity> findByIdForUpdate(@Param("id") Long id);
}

