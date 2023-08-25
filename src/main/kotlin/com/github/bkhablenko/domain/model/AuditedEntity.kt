package com.github.bkhablenko.domain.model

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditedEntity {

    @CreatedDate
    @Column(name = "created_at")
    lateinit var createdDate: LocalDateTime

    @LastModifiedDate
    @Column(name = "updated_at")
    lateinit var lastModifiedDate: LocalDateTime
}
