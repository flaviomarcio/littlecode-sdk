package com.app.business.model.ofservice;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notify_event_item")
public class ProxyEventItem {

    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dtCreate;

    @Column(nullable = false)
    private LocalDateTime dtChange;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProxyEvent event;

    @ManyToOne
    @JoinColumn(nullable = false)
    private ProxyForwarder forwarder;

    @Column(nullable = false, length = 100)
    private String destination;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private int attempts;

    @Getter
    public enum Status {
        WAITING,
        WARNING,
        FAIL,
        CRITICAL,
        CONFLICT,
        SKIPPED,
        SENT
    }

}
