package com.littlecode.threads;

import com.littlecode.interfaces.calling.CallVoid;
import com.littlecode.interfaces.calling.CallCanCanArg1;
import com.littlecode.interfaces.events.OnExecute;
import com.littlecode.interfaces.events.OnExecuteArg2;
import com.littlecode.interfaces.events.OnFail;
import com.littlecode.parsers.ExceptionBuilder;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Builder
public class StackRunner<T> {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final ConcurrentHashMap<T,Runnable> runners = new ConcurrentHashMap<>();
    private int threadCount;
    private Boolean async;
    private Boolean infinity;
    private CallCanCanArg1<T> canExecute;
    private OnExecute<T> onPrepare;
    private OnExecuteArg2<Runner<T>,T> onExecute;
    private OnExecute<T> onSuccessful;
    private OnFail<T> onFail;
    private OnExecute<T> onFinished;

    private int getThreadCount(){
        return (this.threadCount <= 0)
                ? Runtime.getRuntime().availableProcessors()
                : this.threadCount;
    }
    private boolean getAsync(){
        return this.async == null || this.async;
    }
    private boolean getInfinity(){
        return this.infinity != null && this.infinity;
    };

    private void internalExecute(T target) {
        try {
            var runner=this.runners.get(target);
            if(this.getAsync() && runner==null)
                return;
            if(this.onPrepare!=null)
                this.onPrepare.call(target);
            if(this.onExecute!=null)
                //noinspection unchecked
                this.onExecute.call((Runner<T>) runner, target);
            if(this.onSuccessful!=null)
                this.onSuccessful.call(target);
        } catch (Exception e) {
            if(this.onFail!=null)
                this.onFail.call(target,e);
        }finally {
            if(this.onFinished!=null)
                this.onFinished.call(target);
        }
    }

    public void execute(T target) {
        if (!this.getAsync()) {
            this.internalRunnerAdd(null, target);
            return;
        }
        var runner=this.runAsThread(() -> this.internalExecute(target));
        this.internalRunnerAdd(runner, target);
    }

    public Runner<T> runAsThread(CallVoid aVoid) {
        if (aVoid != null) {
            var runner = new Runner<T>(this,aVoid);
            executorService.submit(runner);
            return runner;
        }
        throw ExceptionBuilder.ofInvalid(CallVoid.class);
    }

    public void stop(){
        synchronized (this.runners){
            //noinspection unchecked
            this.runners
                    .values()
                    .forEach(r -> ((Runner<T>)r).stop());
            this.runners.clear();
        }
    }

    private void internalRunnerAdd(Runner<T> runner, T target){
        if(runner==null || target==null)
            return;
        synchronized (this.runners){
            this.runners.put(target,runner);
        }
    }

    private void internalRunnerRemove(T target){
        if(target==null)
            return;
        synchronized (this.runners){
            this.runners.remove(target);
        }
    }

    private void internalRunnerRemove(Runner<T> runner){
        if(runner==null)
            return;
        var map=Map.copyOf(this.runners);
        map.forEach((k, v) -> {
            if(v==runner)
                this.internalRunnerRemove(k);
        });
    }



    @Slf4j
    @RequiredArgsConstructor
    public static class Runner <T> implements Runnable {
        private final StackRunner<T> executor;
        private final CallVoid aVoid;
        private Thread currentThread;
        @Getter
        private int executions=0;

        public synchronized void stop() {
            if (currentThread != null)
                currentThread.interrupt();
        }
        @Override
        public void run() {
            currentThread = Thread.currentThread();
            while(executor.getInfinity() || (executions ==0)){
                try{
                    ++executions;
                    try {
                        if (aVoid != null)
                            aVoid.call();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }finally {
                    executor.internalRunnerRemove(this);
                }

            }

        }
    }
}
