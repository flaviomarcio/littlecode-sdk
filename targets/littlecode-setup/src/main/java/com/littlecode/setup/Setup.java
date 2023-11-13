package com.littlecode.setup;

import com.littlecode.setup.privates.SetupClassesDB;
import com.littlecode.setup.privates.SetupExecutor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.util.List;
import java.util.function.Function;

@Slf4j
public class Setup {
    public static final String STP_CONFIGURE_BEAN_CONTEXT = "beanEasySetupEngineContext";
    public static final String STP_CONFIGURE_BEAN_ENVIRONMENT = "beanEasySetupEngineEnvironment";
    public static final String STP_CONFIGURE_BEAN_CONFIG = "beanEasySetupEngineConfig";
    public static final String STP_CONFIGURE_BEAN_SETTING = "beanEasySetupEngineSetting";
    public static final String STP_CONFIGURE_BEAN_SETUP = "beanEasySetupEngineSetup";
    public static final String STP_CONFIGURE_BEAN_NOTIFY = "beanEasySetupEngineNotify";
    public static final String STP_CONFIGURE_BEAN_DATABASE = "beanEasySetupEngineDatabase";
    public static final String STP_CONFIGURE_BEAN_BUSINESS = "beanEasySetupEngineBusiness";
    @Getter
    private final SetupSetting setting;

    public Setup(SetupSetting setting) {
        this.setting = setting;
    }

    public void execute() {
        final String logPrefix = this.getClass().getName() + ": ";
        log.debug("{}: started", logPrefix);
        try {
            if (!setting.getStarted().isAutoStart()) {
                log.debug("{}: auto start disabled", logPrefix);
                return;
            }
            log.debug("{}: executing", logPrefix);
            (new SetupExecutor(setting)).execute();
            log.debug("{}: executed", logPrefix);
        } catch (Exception e) {
            log.error("{}: fail:{}", logPrefix, e.getMessage());
        } finally {
            log.debug("{}: finished", logPrefix);
        }
    }

    @FunctionalInterface
    public interface SourceExecute {
        void call(SetupClassesDB.StatementItem statementItem);
    }

    @FunctionalInterface
    public interface CallVoid {
        void call();
    }


    @FunctionalInterface
    public interface CallObject<T> {
        void call(final T target);
    }

    @FunctionalInterface
    public interface Callable<V> {
        V call();
    }

    @Builder
    public static class ExecutorNotify {
        public CallVoid successful;
        public CallVoid fail;
        public CallVoid started;
        public CallObject<SetupSetting> prepare;
        public CallVoid finished;
    }

    @Builder
    public static class ExecutorDataBase {
        public CallVoid before;
        public Callable<Connection> connection;
        public Callable<Boolean> checker;
        public Callable<List<String>> sourceList;
        public Function<String, String> sourceParser;
        public SourceExecute sourceExecute;
        public CallVoid after;
    }

    @Builder
    public static class ExecutorBusiness {
        public CallVoid before;
        public Callable<Boolean> executor;
        public CallVoid after;
    }


}
