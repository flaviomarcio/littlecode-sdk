package com.app.business.repository.ofservice;

import com.app.business.model.ofservice.ProxyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProxyGroupRepository extends JpaRepository<ProxyGroup, UUID> {

}

