package com.app.business.model.ofservice;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notify_group")
public class ProxyGroup {
    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dtCreate;

    @Column(nullable = false)
    private LocalDateTime dtChange;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(length = 200)
    private String description;

    @Column(nullable = false)
    private boolean enabled;
}
