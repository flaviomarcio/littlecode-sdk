package com.app.business.dto;

import com.app.business.model.ofservice.ProxyForwarder;
import com.app.business.model.ofservice.ProxyTarget;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProxyContainer {
    private ProxyTarget target;
    private List<ProxyForwarder> forwarders;

    public List<ProxyForwarder> getForwarders() {
        if (this.forwarders == null)
            this.forwarders = new ArrayList<>();
        return this.forwarders;
    }
}
