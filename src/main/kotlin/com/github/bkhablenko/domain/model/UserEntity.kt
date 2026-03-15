package com.github.bkhablenko.domain.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.UUID

@Entity(name = "User")
@Table(name = "`user`")
class UserEntity(

    @NaturalId(mutable = true)
    var username: String,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    lateinit var id: UUID

    @CreationTimestamp
    @Column(updatable = false)
    lateinit var createdAt: Instant

    @UpdateTimestamp
    lateinit var updatedAt: Instant
}
