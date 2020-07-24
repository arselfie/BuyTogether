package com.project.demo.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@MappedSuperclass
public class EntityAncestor {

    @PrePersist
    public void prePersist() {
        if(entityStatus == null)
            entityStatus = EntityStatus.ACTIVE;
    }

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT")
    protected Date created;

    @Column(name = "UPDATED_AT")
    protected Date updated;

    @Column(name = "ENTITY_STATUS", nullable = false)
    @Enumerated(EnumType.STRING)
    protected EntityStatus entityStatus;
}
