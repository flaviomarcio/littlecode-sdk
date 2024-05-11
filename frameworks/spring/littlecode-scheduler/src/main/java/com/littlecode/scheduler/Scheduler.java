package com.littlecode.scheduler;

import com.littlecode.config.CorePublicConsts;
import com.littlecode.cron.CronUtil;
import com.littlecode.parsers.DateUtil;
import com.littlecode.parsers.ObjectUtil;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Scheduler {

    public static final CronUtil CRON = new CronUtil();

    @Builder
    public static class Task {
        private SchedulerRunner runner;

        @Getter
        @Setter
        private Object instance;

        @Getter
        @Setter
        private Method method;

        @Getter
        @Setter
        private int order;

        @Getter
        @Setter
        private String expression;

        @Getter
        @Setter
        private Checker checker;

        @Getter
        @Setter
        private Execution execution;

        @Getter
        private final List<Execution> executions=new ArrayList<>();

        public void setExecutions(List<Execution> executions) {
            this.executions.clear();
            if(executions!=null)
                this.executions.addAll(executions);
        }

        public LocalDateTime nextExecution() {
            var execution = this.getLastExecution();
            var lastExecution = (execution == null)
                    ? CorePublicConsts.MIN_LOCALDATETIME
                    : execution.getExecutionLasted();

            return CronUtil
                    .builder()
                    .temporal(lastExecution)
                    .expression(this.expression)
                    .build()
                    .next();
        }

        public boolean canExecute(LocalDateTime nextExecution) {
            if(nextExecution!=null){
                return
                        (checker != null && checker.checker != null)
                                ? checker.checker.exec(this)
                                : nextExecution.isBefore(LocalDateTime.now());
            }
            return false;
        }

        public boolean canExecute() {
            return canExecute(nextExecution());
        }

        public Execution getLastExecution() {
            return
                    (this.executions.isEmpty())
                            ?null
                            :this.executions.get(this.executions.size() - 1);
        }

//        public String getMessages() {
//            var execution = getLastExecution();
//            if (execution != null && execution.error != null){
//                var message = new StringBuilder();
//                if (execution.error.exception != null)
//                    message.append(execution.error.exception).append("\n");
//                if (execution.error.message != null)
//                    message.append(execution.error.message);
//                return message.toString();
//            }
//            return "";
//        }

        public String getName() {
            return method == null ? "" : method.getName();
        }

        public Response invoke() {
            if (!this.canExecute())
                return Response.SKIPPED;

            this.checker.onBefore.accept(this);
            var lastExecution =
                    this.executions.isEmpty()
                            ? null
                            : this.executions.get(this.executions.size() - 1);
            var execution =
                    this.execution =
                            Execution
                                    .builder()
                                    .counter(lastExecution == null ? 1 : lastExecution.counter)
                                    .history(lastExecution)
                                    .error(null)
                                    .build();
            try {
                method.invoke(instance);
            } catch (Exception e) {
                execution.error = Error
                        .builder()
                        .exception(e.getClass())
                        .message(e.getMessage())
                        .build();
            } finally {
                this.checker.onAfter.accept(this);
            }
            this.cleanup();
            return (execution.error == null) ? Response.SUCCESSFUL : Response.FAIL;
        }

        public void cleanup() {
            this.executions.add(this.execution);
            this.execution = null;
            while (executions.size() > 5)
                this.executions.remove(this.executions.size() - 1);
        }

        public enum Response {
            SUCCESSFUL, SKIPPED, FAIL
        }

        @Builder
        @Getter
        public static class Execution {
            private int counter;
            private LocalDateTime execution;
            private LocalDateTime executionLasted;
            @Setter
            private Object history;
            private Error error;
        }

        @Getter
        @Builder
        public static class Error {
            private Class<?> exception;
            private String message;
        }
    }

    @Builder
    @Getter
    public static class Checker {
        private Func<Boolean, Task> checker;
        private Consumer<Task> onBefore;
        private Consumer<Task> onAfter;

        public static Optional<Checker> from(Schedule scheduleAnnotation) {
            if (scheduleAnnotation != null)
                return Optional.ofNullable(ObjectUtil.create(scheduleAnnotation.checker()));
            return Optional.empty();
        }

        @FunctionalInterface
        public interface Func<R, T> {
            R exec(T t);
        }

    }
}
