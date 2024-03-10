package com.littlecode.threads;

import com.littlecode.util.SystemUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RequiredArgsConstructor
@Slf4j
public class TaskRunner<ARG, RETURN> {
    private static final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ConcurrentHashMap<Integer, Future<?>> futures = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, Task<ARG, RETURN>> tasks = new ConcurrentHashMap<>();
    @Getter
    private final List<Result<ARG, RETURN>> results = new ArrayList<>();
    @Getter
    private final Target<ARG, RETURN> args;

    public static <ARG, RETURN> TaskRunner<ARG, RETURN> of(Target<ARG, RETURN> args) {
        return new TaskRunner<>(args);
    }

    public boolean isSuccessful() {
        for (var result : this.results)
            if (result.exception != null)
                return false;
        return true;
    }

    public boolean isFail() {
        for (var result : this.results)
            if (result.exception != null)
                return true;
        return false;
    }

    public TaskRunner<ARG, RETURN> start() {
        synchronized (args) {
            if (args.onStarted != null)
                args.onStarted.call(this);
        }

        var idealThreadCount = (args.threadCount <= 0)
                ? Runtime.getRuntime().availableProcessors()
                : args.threadCount;
        for (int i = 0; i < idealThreadCount; i++) {
            var task = new Task<>(i, this);
            var future = executorService.submit(task);
            this.futures.put(i, future);
            this.tasks.put(i, task);
        }
        //.... start code
        return this;
    }

    @SuppressWarnings("UnusedReturnValue")
    public TaskRunner<ARG, RETURN> waitToFinished() {
        while (true) {
            var doneAll = true;
            synchronized (this.futures) {
                for (var e : this.futures.entrySet()) {
                    var future = e.getValue();
                    try {
                        synchronized (future) {
                            //noinspection WaitWhileHoldingTwoLocks
                            future.wait(100);
                        }
                    } catch (InterruptedException ignored) {
                    }
                    if (!future.isDone()) {
                        doneAll = false;
                        break;
                    }
                }
            }
            if (doneAll)
                break;
            SystemUtil.sleep(100);
        }
        return this;
    }


    @SuppressWarnings("UnusedReturnValue")
    public TaskRunner<ARG, RETURN> stop() {
        synchronized (this.futures) {
            var futures = Map.copyOf(this.futures);
            futures.forEach((taskId, future) -> stopInternal(taskId));
            this.futures.clear();
        }
        return this;
    }

    private TaskRunner<ARG, RETURN> stopInternal(Integer taskId) {
        if (!this.futures.containsKey(taskId))
            return this;

        var future = this.futures.get(taskId);
        var task = this.tasks.get(taskId);
        future.cancel(true);
        if (!this.futures.contains(future))
            return this;
        this.futures.remove(taskId);
        this.tasks.remove(taskId);
        this.results.addAll(task.getResults());
        return this;
    }

    private void checkFinish() {
        if (this.futures.isEmpty())
            return;

        List<Result<ARG, RETURN>> failList = new ArrayList<>();

        this.results
                .forEach(
                        result ->
                        {
                            if (result.getException() != null) {
                                failList.add(result);
                            }
                        });


        if (!failList.isEmpty()) {
            if (this.args.onFail != null)
                this.args.onFail.call(failList);
        } else {
            if (this.args.onSuccess != null)
                this.args.onSuccess.call(this.results);
        }

        if (this.args.onFinished != null)
            this.args.onFinished.call(this);

    }

    @SuppressWarnings("UnusedReturnValue")
    private TaskRunner<ARG, RETURN> appendResults(Integer taskId) {
        synchronized (this.tasks) {
            return this.stopInternal(taskId);
        }
    }

    @FunctionalInterface
    public interface TaskExecute<ARG, RETURN> {
        RETURN call(ARG arg);
    }

    @FunctionalInterface
    public interface TaskResult<ARG, RETURN> {
        void call(Result<ARG, RETURN> args);
    }

    @FunctionalInterface
    public interface RunnerResults<ARG, RETURN> {
        void call(List<Result<ARG, RETURN>> results);
    }

    @FunctionalInterface
    public interface RunnerEvent<ARG, RETURN> {
        void call(TaskRunner<ARG, RETURN> runner);
    }

    @FunctionalInterface
    public interface TaskFail<ARG, RETURN> {
        void call(Result<ARG, RETURN> result);
    }

    @FunctionalInterface
    public interface TaskArg<RETURN> {
        RETURN call();
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Target<ARG, RETURN> {
        public RunnerEvent<ARG, RETURN> onStarted;
        public RunnerResults<ARG, RETURN> onFail;
        public RunnerResults<ARG, RETURN> onSuccess;
        public RunnerEvent<ARG, RETURN> onFinished;
        public TaskArg<ARG> getTaskGetArg;
        public TaskExecute<ARG, RETURN> onTaskExecute;
        public TaskResult<ARG, RETURN> onTaskSuccess;
        public TaskFail<ARG, RETURN> onTaskFail;
        @Getter
        @Setter
        private int threadCount;
        @Getter
        @Setter
        private boolean beakOnException;
    }

    @Getter
    @RequiredArgsConstructor
    public static class Result<ARG, RETURN> {
        private ARG arg;
        private RETURN result;
        private Exception exception;

        public Result(ARG arg, RETURN result, Exception e) {
            this.arg = arg;
            this.result = result;
            this.exception = e;
        }
    }

    @RequiredArgsConstructor
    public static class Task<ARG, RETURN> implements Runnable {
        private final Integer taskId;
        private final TaskRunner<ARG, RETURN> runner;
        @Getter
        private final List<Result<ARG, RETURN>> results = new ArrayList<>();

        private ARG getTaskArg() {
            if (this.runner == null)
                return null;
            var args = this.runner.args;
            if (args == null || args.getTaskGetArg == null)
                return null;
            synchronized (args) {
                var arg = args.getTaskGetArg.call();
                if (arg == null) {
                    log.debug("args.getTaskGet.call() is null");
                }

                return arg;
            }
        }

        public boolean runInternal(Thread currentThread) {
            if (currentThread == null || currentThread.isInterrupted())
                return false;
            boolean __return;
            Result<ARG, RETURN> result = null;
            try {
                ARG arg;
                try {
                    arg = getTaskArg();
                    if (arg == null)
                        return false;
                } catch (Exception e) {
                    result = new Result<>(null, null, e);
                    return false;
                }

                try {
                    var value = this.runner.args.onTaskExecute.call(arg);
                    if (value == null)
                        __return = false;
                    else {
                        result = new Result<>(arg, value, null);
                        __return = true;
                    }
                } catch (Exception e) {
                    result = new Result<>(arg, null, e);
                    __return = false;
                }

            } finally {
                if (result == null)
                    __return = false;
                else {
                    this.results.add(result);
                    if (result.exception != null) {
                        if (this.runner.args.onTaskFail != null)
                            this.runner.args.onTaskFail.call(result);
                        if (this.runner.args.isBeakOnException()) {
                            this.runner.stop();
                            __return = false;
                        }
                    } else {
                        if (this.runner.args.onTaskSuccess != null)
                            this.runner.args.onTaskSuccess.call(result);
                    }
                }
            }
            return __return;
        }

        @Override
        public void run() {
            var currentThread = Thread.currentThread();
            while (runInternal(currentThread))
                SystemUtil.sleep(1);
            this.runner
                    .appendResults(this.taskId)
                    .checkFinish();
        }
    }

}