package com.littlecode.scheduler.privates;

import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.scheduler.Schedule;
import com.littlecode.scheduler.Scheduler;
import com.littlecode.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@RequiredArgsConstructor
public class Runner implements Runnable {
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();

    private final Schedule scheduleAnnotation;
    private final Object schedulerInstance;
    private final List<Scheduler.Task> cacheSchedulerTask = new ArrayList<>();

    public static void start(BeanUtil beanUtil) {

        log.debug("Scheduler.Startup: start");

        var beanNames = beanUtil.asAnnotation(Schedule.class);

        beanNames.forEach(
                beanName ->
                {
                    try {
                        log.error("Scheduler.Startup:Bean[{}] initializing", beanName);
                        Object schedulerInstance = beanUtil.getBean(Object.class);
                        if (schedulerInstance == null) {
                            log.error("Scheduler.Startup:Bean[{}] bean object is null", beanName);
                            return;
                        }

                        log.error("Scheduler.Startup:Bean[{}] bean create successful", beanName);
                        Schedule scheduleAnnotation = schedulerInstance.getClass().getAnnotation(Schedule.class);

                        if (scheduleAnnotation == null) {
                            log.error("Scheduler.Startup:Bean[{}] Invalid bean {}", beanName, Schedule.class.getName());
                            return;
                        }

                        if (!scheduleAnnotation.enable()) {
                            log.error("Scheduler.Startup:Bean[{}] skipping", beanName);
                            return;
                        }

                        EXECUTOR_SERVICE.submit(new Runner(scheduleAnnotation, schedulerInstance));
                        log.error("Scheduler.Startup:Bean[{}] successful", beanName);
                    } catch (Exception e) {
                        log.error("Scheduler.Startup:Bean[{}] fail: {}", beanName, e.getMessage());
                    }

                }
        );
        log.debug("Scheduler.Startup: finished");
    }


    private void sleep() {
        try {
            Thread.sleep(1000);//wait 1s to empty messages
        } catch (InterruptedException ignored) {
        }
    }

    @Override
    public void run() {
        log.debug("Scheduler.Runner:[{}] started", this.schedulerInstance.getClass().getName());
        try {
            if (!this.schedulesMaker())
                return;
            while (!Thread.currentThread().isInterrupted()) {
                this.schedulesRunner();
                this.sleep();
            }
        } finally {
            log.debug("Scheduler.Runner:[{}] finished", this.schedulerInstance.getClass().getName());
        }
    }

    public boolean schedulesMaker() {
        var schedulerChecker = Scheduler.Checker.from(this.scheduleAnnotation).orElse(null);
        var methods = List.of(schedulerInstance.getClass().getDeclaredMethods());
        methods.forEach(
                method ->
                {
                    Schedule methodAnnotation = AnnotationUtils.findAnnotation(method, Schedule.class);
                    if (methodAnnotation == null)
                        return;

                    if (methodAnnotation.enable()) {
                        log.debug("Scheduler.Runner:[{}], method:[{}] disabled", this.schedulerInstance.getClass().getName(), method.getName());
                        return;
                    }

                    String expression =
                            PrimitiveUtil.isEmpty(methodAnnotation.expression())
                                    ? this.scheduleAnnotation.expression()
                                    : methodAnnotation.expression();

                    if (PrimitiveUtil.isEmpty(expression)) {
                        log.debug("Scheduler.Runner:[{}], method:[{}] no expression found", this.schedulerInstance.getClass().getName(), method.getName());
                        return;
                    }

                    log.debug("Scheduler.Runner:[{}], method:[{}] is enabled", this.schedulerInstance.getClass().getName(), method.getName());
                    cacheSchedulerTask.add(
                            Scheduler.Task
                                    .builder()
                                    .runner(this)
                                    .expression(expression)
                                    .method(method)
                                    .checker(Scheduler.Checker.from(methodAnnotation).orElse(schedulerChecker))
                                    .execution(null)
                                    .executions(new ArrayList<>())
                                    .order(methodAnnotation.order())
                                    .build()
                    );
                }
        );

        return cacheSchedulerTask.isEmpty();
    }

    public void schedulesRunner() {

        cacheSchedulerTask
                .forEach(
                        schedulerTask ->
                        {
                            switch (schedulerTask.invoke()) {
                                case SUCCESSFUL:
                                    log.error("Scheduler.Runner:[{}], {} successful", this.schedulerInstance.getClass().getName(), schedulerTask.getName());
                                case SKIPPED:
                                    log.debug("Scheduler.Runner:[{}], [{}] skipped", this.schedulerInstance.getClass().getName(), schedulerTask.getName());
                                case FAIL:
                                    log.error("Scheduler.Runner:[{}], {}", this.schedulerInstance.getClass().getName(), schedulerTask.getMessages());
                                default:
                                    log.error("Scheduler.Runner:[{}], invalid response", this.schedulerInstance.getClass().getName());
                            }
                        }
                );

    }
}