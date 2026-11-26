package com.meetingrooms.domain.model;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.Objects;

public class Reservation {

    private Long id;
    private Long roomId;
    private String responsible;
    private OffsetDateTime startAt;
    private OffsetDateTime endAt;
    private ReservationStatus status;
    private String cancelledBy;
    private Instant cancelledAt;
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
    private boolean deleted;
    private String deletedBy;
    private Instant deletedAt;

    public Reservation(
            Long roomId,
            String responsible,
            OffsetDateTime startAt,
            OffsetDateTime endAt,
            ReservationStatus status
    ) {
        this.roomId = Objects.requireNonNull(roomId, "roomId");
        this.responsible = Objects.requireNonNull(responsible, "responsible");
        this.startAt = Objects.requireNonNull(startAt, "startAt");
        this.endAt = Objects.requireNonNull(endAt, "endAt");
        this.status = Objects.requireNonNull(status, "status");
        this.deleted = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoomId() {
        return roomId;
    }

    public String getResponsible() {
        return responsible;
    }

    public OffsetDateTime getStartAt() {
        return startAt;
    }

    public OffsetDateTime getEndAt() {
        return endAt;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = Objects.requireNonNull(status, "status");
    }

    public String getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(String cancelledBy) {
        this.cancelledBy = cancelledBy;
    }

    public Instant getCancelledAt() {
        return cancelledAt;
    }

    public void setCancelledAt(Instant cancelledAt) {
        this.cancelledAt = cancelledAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
