package com.meetingrooms.infrastructure.adapters.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomJpaRepository extends JpaRepository<RoomJpaEntity, Long> {

    boolean existsByName(String name);

    Page<RoomJpaEntity> findByDeletedFalse(Pageable pageable);

    long countByDeletedFalse();
}
