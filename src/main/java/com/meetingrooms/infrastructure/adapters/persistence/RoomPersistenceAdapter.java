package com.meetingrooms.infrastructure.adapters.persistence;

import com.meetingrooms.domain.model.Room;
import com.meetingrooms.domain.model.port.RoomCommandPort;
import com.meetingrooms.domain.model.port.RoomQueryPort;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomPersistenceAdapter implements RoomCommandPort, RoomQueryPort {

    private final RoomJpaRepository repository;

    public RoomPersistenceAdapter(RoomJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public Room save(Room room) {
        RoomJpaEntity entity = toEntity(room);
        RoomJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public List<Room> findActive(int page, int size) {
        return repository.findByDeletedFalse(PageRequest.of(page, size, Sort.by("id").ascending()))
                .map(this::toDomain)
                .getContent();
    }

    @Override
    public long countActive() {
        return repository.countByDeletedFalse();
    }

    private RoomJpaEntity toEntity(Room room) {
        RoomJpaEntity entity = new RoomJpaEntity();
        entity.setId(room.getId());
        entity.setName(room.getName());
        entity.setCapacity(room.getCapacity());
        entity.setLocation(room.getLocation());
        entity.setCreatedBy(room.getCreatedBy());
        entity.setCreatedAt(room.getCreatedAt());
        entity.setUpdatedBy(room.getUpdatedBy());
        entity.setUpdatedAt(room.getUpdatedAt());
        entity.setDeleted(room.isDeleted());
        entity.setDeletedBy(room.getDeletedBy());
        entity.setDeletedAt(room.getDeletedAt());
        return entity;
    }

    private Room toDomain(RoomJpaEntity entity) {
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
