package com.littlecode.scheduler;

import com.littlecode.parsers.ObjectUtil;
import com.littlecode.util.CronUtil;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Scheduler {

    public static final CronUtil CRON = new CronUtil();

    @Builder
    public static class Task {
        private SchedulerRunner runner;
        @Getter
        private Object instance;
        @Getter
        private Method method;
        @Getter
        private int order;
        @Getter
        private String expression;
        @Getter
        private Checker checker;
        @Getter
        private Execution execution;
        @Getter
        private List<Execution> executions;

        public LocalDateTime nextExecution() {
            Execution execution = this.getLastExecution();
            LocalDateTime lastExecution = (execution == null)
                    ? LocalDateTime.of(LocalDate.MIN, LocalTime.MIN)
                    : execution.getExecutionLasted();

            return CronUtil
                    .builder()
                    .temporal(lastExecution)
                    .expression(this.expression)
                    .build()
                    .next();
        }

        public boolean canExecute() {
            return
                    (checker != null && checker.checker != null)
                            ? checker.checker.exec(this)
                            : this.nextExecution().isBefore(LocalDateTime.now());
        }

        public Execution getLastExecution() {
            if (this.executions == null || this.executions.isEmpty())
                return null;
            return this.executions.get(this.executions.size() - 1);
        }

        public String getMessages() {
            var execution = getLastExecution();
            if (execution == null || execution.error == null)
                return "";
            var message = new StringBuilder();
            if (execution.error.exception != null)
                message.append(execution.error.exception).append("\n");
            if (execution.error.message != null)
                message.append(execution.error.message);
            return message.toString();
        }

        public String getName() {
            return method == null ? "" : method.getName();
        }

        public Response invoke() {
            if (this.canExecute())
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

        private void cleanup() {
            this.executions.add(this.execution);
            this.execution = null;
            //remove last errors
            while (this.executions.size() > 5)
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
        public Func<Boolean, Task> checker;
        public Consumer<Task> onBefore;
        public Consumer<Task> onAfter;

        public static Optional<Checker> from(Schedule scheduleAnnotation) {
            if (scheduleAnnotation == null || scheduleAnnotation.checker() == null)
                return Optional.empty();
            return Optional.ofNullable(ObjectUtil.create(scheduleAnnotation.checker()));
        }

        @FunctionalInterface
        public interface Func<R, T> {
            R exec(T t);
        }

    }
}