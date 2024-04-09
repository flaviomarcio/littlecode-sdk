package com.app.business.config;

import com.app.business.model.ofservice.ProxyForwarder;
import com.app.business.repository.ofservice.ProxyForwarderRepository;
import com.littlecode.setup.Setup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.vendor.Database;

import java.util.List;
import java.util.UUID;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SetupConfig {
    private final ProxyForwarderRepository accountRepository;

    @Bean(name = Setup.STP_CONFIGURE_BEAN_NOTIFY)
    public Setup.ExecutorNotify configureNotify() {
        return
                Setup.ExecutorNotify
                        .builder()
                        .started(() -> log.debug("setup: started"))
                        .prepare(target -> {
                            var ddl = target.getDatabase().getDDL();
                            ddl.setPackageNames(ProxyForwarder.class.getPackageName());
                            target
                                    .getDatabase()
                                    .getTarget()
                                    .setDatabases(List.of(Database.POSTGRESQL));
                        })
                        .successful(() -> {
                        })
                        .fail(() -> {
                        })
                        .finished(() -> log.debug("setup: completed"))
                        .build();
    }

    @Bean(name = Setup.STP_CONFIGURE_BEAN_DATABASE)
    public Setup.ExecutorDataBase configureDatabase() {
        log.debug("configureDatabase: executing");
        return Setup.ExecutorDataBase
                .builder()
                .before(() -> log.debug("db: executing"))
                .checker(() -> {
                    try {
                        accountRepository.existsById(UUID.randomUUID());
                        return false;
                    } catch (Exception e) {
                        return true;
                    }
                })
                .sourceList(() -> List.of("easy.setup.ddl"))
                .sourceExecute(statementItem -> log.debug(statementItem.toString()))
                .after(() -> log.debug("db: finished"))
                .build();
    }

    @Bean(name = Setup.STP_CONFIGURE_BEAN_BUSINESS)
    public Setup.ExecutorBusiness configureBusiness() {
        log.debug("configureDatabase: executing");
        return
                Setup.ExecutorBusiness
                        .builder()
                        .before(() -> log.debug("business: executing"))
                        .executor(() -> true)
                        .after(() -> log.debug("business: finished"))
                        .build();

    }
}
