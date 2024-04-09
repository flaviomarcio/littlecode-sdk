package com.app.business.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "tool")
public class ItemModel {
    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dtCreate;

    @Column(nullable = false)
    private LocalDateTime dtChange;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, length = 1000)
    private URI route;

    @Column(length = 1000)
    private URI ico;

    @Column(nullable = false)
    private boolean enabled;
}
