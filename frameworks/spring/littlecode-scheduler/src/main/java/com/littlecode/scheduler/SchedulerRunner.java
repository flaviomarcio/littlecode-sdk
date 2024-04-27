package com.littlecode.scheduler;

import com.littlecode.exceptions.FrameworkException;
import com.littlecode.parsers.PrimitiveUtil;
import com.littlecode.util.BeanUtil;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Data
public class SchedulerRunner implements Runnable {
    private static final LocalDateTime MAX_EXECUTION_TIME=LocalDateTime.of(LocalDate.of(2500,1,1), LocalTime.of(0,0,0));
    private static final ExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadExecutor();
    private final List<Scheduler.Task> cacheSchedulerTask = new ArrayList<>();
    private final Schedule scheduleAnnotation;
    private final Object schedulerInstance;
    private LocalDateTime limitExecutionTime=MAX_EXECUTION_TIME;

    public SchedulerRunner(Schedule scheduleAnnotation, Object schedulerInstance) {
        if (scheduleAnnotation == null)
            throw new NullPointerException("scheduleAnnotation is null");
        if (schedulerInstance == null)
            throw new NullPointerException("schedulerInstance is null");
        this.scheduleAnnotation = scheduleAnnotation;
        this.schedulerInstance = schedulerInstance;
    }

    public LocalDateTime getLimitExecutionTime(){
        if(this.limitExecutionTime==null)
            return this.limitExecutionTime=MAX_EXECUTION_TIME;
        return this.limitExecutionTime;
    }

    public static void start() {
        var beanUtil = new BeanUtil();
        var beanNames = beanUtil.asAnnotation(Schedule.class);
        for (var beanName : beanNames) {
            try {
                Object schedulerInstance = beanUtil.bean(beanName).getBean(Object.class);
                if (schedulerInstance != null) {
                    Schedule scheduleAnnotation = schedulerInstance.getClass().getAnnotation(Schedule.class);
                    if (scheduleAnnotation != null && !scheduleAnnotation.enable())
                        EXECUTOR_SERVICE.submit(new SchedulerRunner(scheduleAnnotation, schedulerInstance));
                }
            } catch (Exception ignored) {
            }
        }
    }

    @SneakyThrows
    @Override
    public void run() {
        final var currentThread=Thread.currentThread();
        if (this.schedulesMaker()){
            while (!currentThread.isInterrupted()) {
                this.schedulesRunner();
                if(!this.getLimitExecutionTime().isBefore(LocalDateTime.now()))
                    Thread.sleep(1);//wait 1s to empty messages
                break;
            }
        }
    }

    public Scheduler.Task createSchedulerTask(Method method, Scheduler.Checker checker){
        if(method!=null && checker!=null){
            Schedule methodAnnotation = AnnotationUtils.findAnnotation(method, Schedule.class);
            if (methodAnnotation != null && methodAnnotation.enable()){
                final var expression =
                        (methodAnnotation.expression()!=null && !methodAnnotation.expression().trim().isEmpty())
                                ? this.scheduleAnnotation.expression()
                                : methodAnnotation.expression();
                return Scheduler.Task
                        .builder()
                        .runner(this)
                        .expression(expression)
                        .method(method)
                        .checker(checker)
                        .execution(null)
                        .order(methodAnnotation.order())
                        .build();
            }
        }
        return null;
    }

    public Method[] getInstanceMethods(){
        return schedulerInstance.getClass().getDeclaredMethods();
    }

    public boolean schedulesMaker() {
        cacheSchedulerTask.clear();
        var schedulerChecker = Scheduler.Checker.from(this.scheduleAnnotation).orElse(null);
        for (var method : this.getInstanceMethods()) {
            var schedulerTask=createSchedulerTask(method,schedulerChecker);
            if(schedulerTask!=null)
                cacheSchedulerTask.add(schedulerTask);
        }
        return cacheSchedulerTask.isEmpty();
    }

    public void schedulesRunner() {
        for (Scheduler.Task schedulerTask : cacheSchedulerTask)
            schedulerTask.invoke();
    }
}