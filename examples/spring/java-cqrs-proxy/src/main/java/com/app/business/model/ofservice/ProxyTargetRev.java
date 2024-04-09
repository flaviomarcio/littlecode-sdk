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
@Table(name = "proxy_notify_rev")
public class ProxyTargetRev {
    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dt;

    @Column()//nullable=true
    private UUID rev;

    @Column()//nullable=true
    private boolean enabled;
}
