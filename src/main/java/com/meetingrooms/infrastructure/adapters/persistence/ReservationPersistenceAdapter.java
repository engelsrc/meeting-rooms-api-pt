package com.meetingrooms.infrastructure.adapters.persistence;

import com.meetingrooms.domain.model.Reservation;
import com.meetingrooms.domain.model.ReservationStatus;
import com.meetingrooms.domain.model.Room;
import com.meetingrooms.domain.model.port.ReservationCommandPort;
import com.meetingrooms.domain.model.port.ReservationQueryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class ReservationPersistenceAdapter implements ReservationCommandPort, ReservationQueryPort {

    private final ReservationJpaRepository reservationRepository;
    private final RoomJpaRepository roomRepository;

    public ReservationPersistenceAdapter(
            ReservationJpaRepository reservationRepository,
            RoomJpaRepository roomRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.roomRepository = roomRepository;
    }

    @Override
    public Optional<Room> lockRoomForUpdate(Long roomId) {
        return roomRepository.findByIdForUpdate(roomId).map(this::toRoomDomain);
    }

    @Override
    public boolean existsActiveOverlap(Long roomId, OffsetDateTime startAt, OffsetDateTime endAt) {
        return reservationRepository.existsActiveOverlap(roomId, ReservationStatus.ACTIVE, startAt, endAt);
    }

    @Override
    public Reservation save(Reservation reservation) {
        ReservationJpaEntity saved = reservationRepository.save(toEntity(reservation));
        return toDomain(saved);
    }

    @Override
    public Optional<Reservation> findById(Long id) {
        return reservationRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean roomExists(Long roomId) {
        return roomRepository.existsById(roomId);
    }

    @Override
    public List<Reservation> findFiltered(
            Long roomId,
            OffsetDateTime startFromInclusive,
            OffsetDateTime startToExclusive,
            int page,
            int size
    ) {
        return reservationRepository.findFiltered(
                        roomId,
                        startFromInclusive,
                        startToExclusive,
                        PageRequest.of(page, size, Sort.by("startAt").ascending().and(Sort.by("id").ascending()))
                )
                .map(this::toDomain)
                .getContent();
    }

    @Override
    public long countFiltered(
            Long roomId,
            OffsetDateTime startFromInclusive,
            OffsetDateTime startToExclusive
    ) {
        return reservationRepository.countFiltered(roomId, startFromInclusive, startToExclusive);
    }

    private ReservationJpaEntity toEntity(Reservation reservation) {
        ReservationJpaEntity entity = new ReservationJpaEntity();
        entity.setId(reservation.getId());
        entity.setRoomId(reservation.getRoomId());
        entity.setResponsible(reservation.getResponsible());
        entity.setStartAt(reservation.getStartAt());
        entity.setEndAt(reservation.getEndAt());
        entity.setStatus(reservation.getStatus());
        entity.setCancelledBy(reservation.getCancelledBy());
        entity.setCancelledAt(reservation.getCancelledAt());
        entity.setCreatedBy(reservation.getCreatedBy());
        entity.setCreatedAt(reservation.getCreatedAt());
        entity.setUpdatedBy(reservation.getUpdatedBy());
        entity.setUpdatedAt(reservation.getUpdatedAt());
        entity.setDeleted(reservation.isDeleted());
        entity.setDeletedBy(reservation.getDeletedBy());
        entity.setDeletedAt(reservation.getDeletedAt());
        return entity;
    }

    private Reservation toDomain(ReservationJpaEntity entity) {
        Reservation reservation = new Reservation(
                entity.getRoomId(),
                entity.getResponsible(),
                entity.getStartAt(),
                entity.getEndAt(),
                entity.getStatus()
        );
        reservation.setId(entity.getId());
        reservation.setCancelledBy(entity.getCancelledBy());
        reservation.setCancelledAt(entity.getCancelledAt());
        reservation.setCreatedBy(entity.getCreatedBy());
        reservation.setCreatedAt(entity.getCreatedAt());
        reservation.setUpdatedBy(entity.getUpdatedBy());
        reservation.setUpdatedAt(entity.getUpdatedAt());
        reservation.setDeleted(entity.isDeleted());
        reservation.setDeletedBy(entity.getDeletedBy());
        reservation.setDeletedAt(entity.getDeletedAt());
        return reservation;
    }

    private Room toRoomDomain(RoomJpaEntity entity) {
        Room room = new Room(entity.getName(), entity.getCapacity(), entity.getLocation());
        room.setId(entity.getId());
        room.setCreatedBy(entity.getCreatedBy());
        room.setCreatedAt(entity.getCreatedAt());
        room.setUpdatedBy(entity.getUpdatedBy());
        room.setUpdatedAt(entity.getUpdatedAt());
        room.setDeleted(entity.isDeleted());
        room.setDeletedBy(entity.getDeletedBy());
        room.setDeletedAt(entity.getDeletedAt());
        return room;
    }
}
