package com.app.business.model.ofservice;

import jakarta.persistence.*;
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
@Table(name = "notify_target_item")
public class ProxyTargetItem {

    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dtCreate;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProxyTarget target;

    @Column(nullable = false, length = 100)
    private String destination;

    public String getDestination() {
        if (this.destination == null)
            this.destination = "";
        return this.destination.trim();
    }


}
