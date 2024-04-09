package com.app.business.repository.ofservice;

import com.app.business.model.ofservice.ProxyForwarder;
import com.app.business.model.ofservice.ProxyTargetRev;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProxyForwarderRepository extends JpaRepository<ProxyForwarder, UUID> {
    List<ProxyForwarder> findByTargetRev(ProxyTargetRev targetRev);
}

