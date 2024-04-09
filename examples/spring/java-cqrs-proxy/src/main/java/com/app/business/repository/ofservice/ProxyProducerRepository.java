package com.app.business.repository.ofservice;

import com.app.business.model.ofservice.ProxyProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProxyProducerRepository extends JpaRepository<ProxyProducer, UUID> {

}

