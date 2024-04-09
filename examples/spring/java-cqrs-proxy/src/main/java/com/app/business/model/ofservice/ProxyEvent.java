package com.app.business.model.ofservice;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "notify_event")
public class ProxyEvent {

    @Id
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime dtCreate;

    @Column(nullable = false)
    private UUID producerId;

    @Column()
    private UUID targetId;

    @Column()
    private UUID forwarderId;

    @Column(nullable = false)
    private Status status;

    @Column(nullable = false)
    private String payload;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn
    private List<ProxyEventItem> eventItems;

    public List<ProxyEventItem> getItems() {
        if (eventItems == null)
            eventItems = new ArrayList<>();
        return this.eventItems;
    }

    public enum Status {
        WAIT, FAIL, EXECUTING, FINISHED
    }
}
