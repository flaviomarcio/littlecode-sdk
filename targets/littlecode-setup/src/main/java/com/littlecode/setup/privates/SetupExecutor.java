package com.littlecode.setup.privates;

import com.littlecode.setup.Setup;
import com.littlecode.setup.SetupSetting;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
public class SetupExecutor {
    private final SetupConfig config;
    private final SetupSetting setting;

    public SetupExecutor(SetupSetting setting) {
        this.setting = setting;
        this.config = setting.getConfig();
    }

    private boolean dbExecuteTarget(Connection connection, Setup.ExecutorDataBase executorDataBase) {
        final String logPrefix = "exec::DB::dbExecuteTarget: ";
        try {
            log.debug("{}start", logPrefix);
            var __return =
                    SetupExecutorDbTarget
                            .builder()
                            .setting(this.setting)
                            .connection(connection)
                            .executorDataBase(executorDataBase)
                            .build()
                            .execute();
            if (__return)
                log.debug("{}successful on execute", logPrefix);
            else
                log.debug("{}fail on execute", logPrefix);
            return __return;
        } catch (Exception e) {
            log.error("{}fail: {}", logPrefix, e.getMessage());
            return false;
        } finally {
            log.debug("{}finished", logPrefix);
        }
    }

    private boolean executeDatabase() {
        final String logPrefix = "exec::DB::dbExecuteTarget: ";
        log.debug("{}start", logPrefix);
        try {
            if (!this.setting.getDatabase().getDDL().isAutoStart() && !this.setting.getDatabase().getTarget().isAutoStart()) {
                log.debug("{}auto start ignored", logPrefix);
                return true;
            }

            var executorDataBase = this.setting.getConfig().getBean(Setup.STP_CONFIGURE_BEAN_DATABASE, Setup.ExecutorDataBase.class);
            if (executorDataBase == null) {
                log.debug("{}skipped, executorDataBase is null", logPrefix);
                return true;
            }
            log.debug("{}connection creating", logPrefix);
            try (Connection connection = this.setting.getConfig().createConnection(executorDataBase)) {
                if (connection == null) {
                    log.debug("{}skipped, connection is null", logPrefix);
                    return false;
                } else {
                    log.debug("{}skipped, connection is null", logPrefix);
                    var databaseMetaData = connection.getMetaData();
                    log.debug("{}connection.productName: {}", logPrefix, databaseMetaData.getDatabaseProductName());
                    log.debug("{}connection.productVersion: {}", logPrefix, databaseMetaData.getDatabaseProductVersion());
                    log.debug("{}connection.driverName: {}", logPrefix, databaseMetaData.getDriverName());
                    log.debug("{}connection.driverVersion: {}", logPrefix, databaseMetaData.getDriverVersion());
                }

                if (this.setting.getDatabase().getTarget().isAutoStart()) {
                    if (!this.dbExecuteTarget(connection, executorDataBase)) {
                        log.debug("{}fail on call dbExecuteTarget()", logPrefix);
                        return false;
                    }
                }

                return true;
            } catch (SQLException e) {
                log.error("{}{}", logPrefix, e.getMessage());
                return false;
            }
        } catch (Exception e) {
            log.error("{}fail: {}", logPrefix, e.getMessage());
            return false;
        } finally {
            log.debug("{}finished", logPrefix);
        }
    }

    private boolean executeBusiness() {
        final String logPrefix = "exec::executeBusiness: ";
        log.debug("{}start", logPrefix);
        try {
            if (!this.setting.getBusiness().isAutoStart()) {
                log.debug("{}auto start ignored", logPrefix);
                return true;
            }

            var executorBusiness = this.config.getBean(Setup.STP_CONFIGURE_BEAN_BUSINESS, Setup.ExecutorBusiness.class);
            if (executorBusiness == null) {
                log.debug("{}skipped", logPrefix);
                return true;
            }
            Boolean __return = executorBusiness.executor.call();
            log.debug("{}executed", logPrefix);
            return __return == null || __return;
        } catch (Exception e) {
            log.error("{}fail: {}", logPrefix, e.getMessage());
            return false;
        } finally {
            log.debug("{}finished", logPrefix);
        }
    }

    private Setup.ExecutorNotify executeNotify() {
        final String logPrefix = "execute:executeNotify: ";
        log.debug("{}executorNotify:: started", logPrefix);
        var executorNotify = this.config.getBean(Setup.STP_CONFIGURE_BEAN_NOTIFY, Setup.ExecutorNotify.class);
        if (executorNotify == null)
            log.debug("{}executorNotify:: skipped, executorNotify is null", logPrefix);
        else
            log.debug("{}executorNotify:: continue, executorNotify existing", logPrefix);
        return executorNotify;
    }

    public void execute() {
        final String logPrefix = "execute: ";
        log.debug("{}start", logPrefix);
        try {
            var executorNotify = this.executeNotify();
            if (executorNotify != null) {
                log.debug("{}notify:: start/prepare executing", logPrefix);
                if (executorNotify.started != null)
                    executorNotify.started.call();
                if (executorNotify.prepare != null)
                    executorNotify.prepare.call(this.setting);
                log.debug("{}notify:: start/prepare finished", logPrefix);
            }
            boolean response;
            try {

                try {
                    log.debug("{}:: executeDatabase calling", logPrefix);
                    response = executeDatabase();
                    if (!response)
                        log.error("{}:: executeDatabase fail", logPrefix);
                } finally {
                    log.debug("{}:: executeDatabase finished", logPrefix);
                }

                try {
                    log.debug("{}:: executeBusiness calling", logPrefix);
                    response = response && executeBusiness();
                    if (!response)
                        log.error("{}:: executeBusiness fail", logPrefix);
                } finally {
                    log.debug("{}:: executeBusiness finished", logPrefix);
                }
            } catch (Exception e) {
                response = false;
                log.error("{}:: executeDatabase|executeBusiness fail: {}", logPrefix, e.getMessage());
            } finally {
                log.error("{}:: executeDatabase|executeBusiness finished", logPrefix);
            }

            if (executorNotify != null) {
                log.debug("{}notify:: successful/fail/finished executing", logPrefix);
                var event = (response)
                        ? executorNotify.successful
                        : executorNotify.fail;
                if (event != null)
                    event.call();
                if (executorNotify.finished != null)
                    executorNotify.finished.call();
                log.debug("{}notify:: successful/fail/finished finished", logPrefix);
            }
        } catch (Exception e) {
            log.error("{}fail: {}", logPrefix, e.getMessage());
        } finally {
            log.debug("{}finished", logPrefix);
        }
    }

}
