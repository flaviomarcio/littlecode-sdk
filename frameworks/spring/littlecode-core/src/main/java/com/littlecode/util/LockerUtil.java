package com.littlecode.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * classe para gestao de semáforo por scopo
 */
@Slf4j
@NoArgsConstructor
public class LockerUtil {
    /**
     * Controle de concorrencia
     */
    private final Object __locker = new Object();
    /**
     * Cache dos targets
     */
    private final ConcurrentMap<String, Target> targetCollection = new ConcurrentHashMap<String, Target>();

    /**
     * Reset a coleção
     */
    public void reset() {
        synchronized (__locker) {
            log.debug("reset");
            targetCollection.clear();
        }
    }

    /**
     * recupera o target com valor default
     *
     * @return
     */
    public Target target() {
        return this.target("");
    }

    /**
     * recupera o target por nome
     *
     * @param target
     * @return
     */
    public Target target(String target) {
        if (target == null)
            throw new IllegalArgumentException("target cannot be null");
        Target lock;
        synchronized (__locker) {
            log.debug("target get:{}", target);
            lock = targetCollection.get(target);
            if (lock == null) {
                this.targetCollection.put(target, lock = new Target(target, false));
                log.debug("target create:{}", lock.name);
            }
        }
        return lock;
    }

    /**
     * executa lock com valor default
     *
     * @return
     */
    public boolean lock() {
        return this.lock("");
    }

    /**
     * executa lock com target
     *
     * @param target
     * @return
     */
    public boolean lock(String target) {
        synchronized (__locker) {
            var locker = this.target(target);
            if (locker.locked) {
                log.debug("is locked:{}", locker.name);
                return false;
            }
            locker.locked = true;
            log.debug("locked:{}", locker.name);
        }
        return true;
    }

    /**
     * desaloca target com valor default
     *
     * @return
     */
    public boolean unlock() {
        return this.unlock("");
    }

    /**
     * desaloca target com valor
     *
     * @param target
     * @return
     */
    public boolean unlock(String target) {
        synchronized (__locker) {
            var locker = this.target(target);
            if (locker.locked) {
                locker.locked = false;
                log.debug("unlocked:{}", locker.name);
                return true;
            }
            log.debug("no locked:{}", locker.name);
        }
        return false;
    }

    /**
     * verifica se está locado com valor default
     *
     * @return
     */
    public boolean isLocked() {
        return this.isLocked("");
    }

    /**
     * verifica se está locado com valor
     *
     * @param target
     * @return
     */
    public boolean isLocked(String target) {
        synchronized (__locker) {
            if (this.target(target).locked)
                return true;
        }
        return false;
    }

    /**
     * Gera maker para execução de metodo com valor default
     *
     * @return
     */
    public ExecutorMaker executor() {
        return this.executor("");
    }

    /**
     * Gera maker para execução de metodo com valor
     *
     * @param target
     * @return
     */
    public ExecutorMaker executor(String target) {
        return new ExecutorMaker(this, target);
    }

    @FunctionalInterface
    public interface ExecutorOnExecute {
        void exec();
    }

    @FunctionalInterface
    public interface ExecutorOnError {
        void exec(Exception e);
    }

    /**
     * Classe para representar o alvo do lock
     */
    @Getter
    @Setter
    public static class Target {
        private final String name;
        private Boolean locked;

        public Target(String name, boolean locked) {
            this.name = name;
            this.locked = locked;
        }
    }

    /**
     * Classe para execução de metodo
     */
    public static class ExecutorMaker {
        private final LockerUtil util;
        private final String target;
        private ExecutorOnExecute onExecute;
        private ExecutorOnError onError;


        public ExecutorMaker(LockerUtil util, String target) {
            this.util = util;
            this.target = target;
        }

        /**
         * rotina para receber metodo
         *
         * @param execute
         * @return
         */
        public ExecutorMaker onExecute(ExecutorOnExecute execute) {
            this.onExecute = execute;
            return this;
        }

        public ExecutorMaker onError(ExecutorOnError error) {
            this.onError = error;
            return this;
        }

        /**
         * executa metodo
         *
         * @return
         */
        public boolean exec() {
            log.debug("execute[{}]: started", target);
            boolean __return = false;
            if (!util.lock(target))
                log.debug("execute[{}]: skipped", target);
            else {
                try {
                    log.debug("execute[{}]: executing", target);
                    onExecute.exec();
                    __return = true;
                } catch (Exception e) {
                    if (this.onError == null)
                        log.error("execute[{}]: fail, {}", target, e.getMessage());
                    else {
                        try {
                            this.onError.exec(e);
                        } catch (Exception ignored) {
                        }
                    }
                } finally {
                    util.unlock(target);
                    log.debug("execute[{}]: finished", target);
                }
            }
            return __return;
        }
    }
}
