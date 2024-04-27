package com.littlecode.tests;

import com.littlecode.config.UtilCoreConfig;
import com.littlecode.scheduler.*;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.ThrowingConsumer;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SchedulerTest {
    private static ApplicationContext STATIC_APPLICATION_CONTEXT=null;
    private static Environment STATIC_ENVIRONMENT=null;

    @BeforeAll
    public static void beforeAll() {
        if(STATIC_APPLICATION_CONTEXT==null){
            STATIC_APPLICATION_CONTEXT=Mockito.mock(ApplicationContext.class);
            STATIC_ENVIRONMENT=Mockito.mock(Environment.class);
            Mockito.when(STATIC_APPLICATION_CONTEXT.getBeanNamesForAnnotation(Schedule.class)).thenReturn(new String[]{});
            UtilCoreConfig.setApplicationContext(STATIC_APPLICATION_CONTEXT);
            UtilCoreConfig.setEnvironment(STATIC_ENVIRONMENT);


        }

    }


    @Test
    public void UT_CHECK_CONSTRUCTOR() {

        var schedulerAutoConfiguration = new SchedulerAutoConfiguration(STATIC_APPLICATION_CONTEXT, STATIC_ENVIRONMENT);
        Assertions.assertDoesNotThrow(schedulerAutoConfiguration::isAutoStart);
        Assertions.assertDoesNotThrow(() -> schedulerAutoConfiguration.start());
        schedulerAutoConfiguration.setAutoStart(true);
        Assertions.assertDoesNotThrow(() -> schedulerAutoConfiguration.start());

        Assertions.assertDoesNotThrow(() -> new SchedulerBeans(Mockito.mock(SchedulerSetting.class)));
        Assertions.assertDoesNotThrow(() -> new SchedulerBeans(Mockito.mock(SchedulerSetting.class)).newSchedulerSetting());
        Assertions.assertNotNull(new SchedulerBeans(Mockito.mock(SchedulerSetting.class)).newSchedulerSetting());

        Assertions.assertDoesNotThrow(SchedulerSetting::new);
        Assertions.assertDoesNotThrow(() -> new SchedulerSetting().isAutoStart());
        Assertions.assertDoesNotThrow(() -> new SchedulerSetting().isLog());

        Assertions.assertNotNull(Scheduler.CRON);
        Assertions.assertFalse(new SchedulerSetting().isAutoStart());
        Assertions.assertFalse(new SchedulerSetting().isLog());
    }

    @Test
    public void UT_CHECK_EXECUTION() {
        Assertions.assertDoesNotThrow(() -> {
            Scheduler.Task.Execution
                    .builder()
                    .counter(0).build();
        });
        var execution = Scheduler.Task.Execution
                .builder()
                .counter(0)
                .build();
        Assertions.assertDoesNotThrow(execution::getExecution);
        Assertions.assertDoesNotThrow(execution::getExecutionLasted);
        Assertions.assertDoesNotThrow(execution::getCounter);
        Assertions.assertDoesNotThrow(execution::getError);
        Assertions.assertDoesNotThrow(()->execution.setHistory("teste"));
        Assertions.assertDoesNotThrow(execution::getHistory);

    }

    @Test
    public void UT_CHECK_ERROR() {
        Assertions.assertDoesNotThrow(() -> {
            Scheduler.Task.Error
                    .builder()
                    .build();
        });
        var error = Scheduler.Task.Error
                .builder()
                .build();
        Assertions.assertDoesNotThrow(error::getException);
        Assertions.assertDoesNotThrow(error::getMessage);

    }

    @Test
    public void UT_CHECK_CHECKER() {
        Assertions.assertDoesNotThrow(() -> {
            var checker=Scheduler.Checker
                    .builder()
                    .checker(new Scheduler.Checker.Func<Boolean, Scheduler.Task>() {
                        @Override
                        public Boolean exec(Scheduler.Task task) {
                            return true;
                        }
                    })
                    .onAfter(new ThrowingConsumer<Scheduler.Task>() {
                        @Override
                        public void acceptThrows(Scheduler.Task task) throws Throwable {

                        }
                    })
                    .onBefore(new ThrowingConsumer<Scheduler.Task>() {
                        @Override
                        public void acceptThrows(Scheduler.Task task) throws Throwable {

                        }
                    })
                    .build();
        });
        var checker=Scheduler.Checker
                .builder()
                .checker(new Scheduler.Checker.Func<Boolean, Scheduler.Task>() {
                    @Override
                    public Boolean exec(Scheduler.Task task) {
                        return true;
                    }
                })
                .onAfter(new ThrowingConsumer<Scheduler.Task>() {
                    @Override
                    public void acceptThrows(Scheduler.Task task) throws Throwable {

                    }
                })
                .onBefore(new ThrowingConsumer<Scheduler.Task>() {
                    @Override
                    public void acceptThrows(Scheduler.Task task) throws Throwable {

                    }
                })
                .build();
        Assertions.assertDoesNotThrow(checker::getChecker);
        Assertions.assertDoesNotThrow(checker::getOnBefore);
        Assertions.assertDoesNotThrow(checker::getOnAfter);

        var schedulerInstance=new PrivateScheduleInstance();
        Schedule scheduleAnnotation=schedulerInstance.getClass().getAnnotation(Schedule.class);
        Assertions.assertDoesNotThrow(()->Scheduler.Checker.from(scheduleAnnotation).orElse(null));
        Assertions.assertDoesNotThrow(()->Scheduler.Checker.from(null).orElse(null));

    }

    @Test
    public void UT_CHECK_TASK() {
        Assertions.assertDoesNotThrow(() -> {
            Scheduler.Task
                    .builder()
                    .runner(null)
                    .expression(null)
                    .method(null)
                    .checker(null)
                    .execution(null)
                    .order(0)
                    .build();
        });

        var schedulerChecker=Scheduler.Checker
                .builder()
                .checker(new Scheduler.Checker.Func<Boolean, Scheduler.Task>() {
                    @Override
                    public Boolean exec(Scheduler.Task task) {
                        return true;
                    }
                })
                .onAfter(new ThrowingConsumer<Scheduler.Task>() {
                    @Override
                    public void acceptThrows(Scheduler.Task task) throws Throwable {

                    }
                })
                .onBefore(new ThrowingConsumer<Scheduler.Task>() {
                    @Override
                    public void acceptThrows(Scheduler.Task task) throws Throwable {

                    }
                })
                .build();
        var method=Mockito.mock(Method.class);
        Mockito.when(method.getName()).thenReturn("run");
        var task=Scheduler.Task
                .builder()
                .runner(null)
                .expression(null)
                .method(method)
                .checker(null)
                .execution(null)
                .order(0)
                .build();

        Assertions.assertDoesNotThrow(task::getName);
        Assertions.assertDoesNotThrow(task::getInstance);
        Assertions.assertDoesNotThrow(()->task.setInstance(null));
        Assertions.assertDoesNotThrow(task::getLastExecution);
        Assertions.assertDoesNotThrow(()->task.setExpression(null));
        Assertions.assertDoesNotThrow(task::getExpression);
        Assertions.assertDoesNotThrow(()->task.setMethod(null));
        Assertions.assertDoesNotThrow(task::getMethod);
        Assertions.assertDoesNotThrow(()->task.setChecker(null));
        Assertions.assertDoesNotThrow(task::getChecker);
        Assertions.assertDoesNotThrow(()->task.setExecution(null));
        Assertions.assertDoesNotThrow(task::getExecution);
        Assertions.assertDoesNotThrow(()->task.setExecutions(new ArrayList<>()));
        Assertions.assertDoesNotThrow(()->task.setExecutions(null));
        Assertions.assertDoesNotThrow(task::getExecutions);
        Assertions.assertDoesNotThrow(()->task.setOrder(0));
        Assertions.assertDoesNotThrow(task::getOrder);

        Assertions.assertDoesNotThrow(task::nextExecution);
        Assertions.assertDoesNotThrow(task::invoke);
        task.setChecker(null);
        Assertions.assertDoesNotThrow(()->task.canExecute(LocalDateTime.now().minusDays(10)));
        Assertions.assertDoesNotThrow(()->task.canExecute());
        task.setChecker(schedulerChecker);
        Assertions.assertDoesNotThrow(()->task.canExecute());
        Assertions.assertFalse(task.canExecute());
        Assertions.assertTrue(()->task.canExecute(LocalDateTime.now().minusDays(10)));


        Assertions.assertDoesNotThrow(task::cleanup);
        task.setExecutions(
                List.of(
                        Scheduler.Task.Execution.builder().counter(0).executionLasted(LocalDateTime.now()).build(),
                        Scheduler.Task.Execution.builder().counter(0).executionLasted(LocalDateTime.now()).build(),
                        Scheduler.Task.Execution.builder().counter(0).executionLasted(LocalDateTime.now()).build(),
                        Scheduler.Task.Execution.builder().counter(0).executionLasted(LocalDateTime.now()).build(),
                        Scheduler.Task.Execution.builder().counter(0).executionLasted(LocalDateTime.now()).build(),
                        Scheduler.Task.Execution.builder().counter(0).executionLasted(LocalDateTime.now()).build(),
                        Scheduler.Task.Execution.builder().counter(0).executionLasted(LocalDateTime.now()).build()
                ));
        Assertions.assertEquals(task.getExecutions().size(),7);
        Assertions.assertDoesNotThrow(task::cleanup);
        Assertions.assertFalse(task.getExecutions().isEmpty());
        Assertions.assertEquals(task.getExecutions().size(),5);


        task.setExecutions(null);
        Assertions.assertDoesNotThrow(task::getLastExecution);
        Assertions.assertNull(task.getLastExecution());
        task.setExecutions(new ArrayList<>());
        Assertions.assertDoesNotThrow(task::getLastExecution);
        Assertions.assertNull(task.getLastExecution());
        task.setExecutions(List.of(Scheduler.Task.Execution.builder().counter(0).executionLasted(LocalDateTime.now()).build()));
        Assertions.assertDoesNotThrow(task::getLastExecution);
        Assertions.assertNotNull(task.getLastExecution());

        Assertions.assertDoesNotThrow(task::nextExecution);
        Assertions.assertNull(task.nextExecution());


        Assertions.assertDoesNotThrow(()->task.canExecute());

        Assertions.assertNotNull(task.getName());
        task.setMethod(null);
        Assertions.assertNotNull(task.getName());

    }

    @Test
    public void UT_CHECK_SchedulerRunner(){
        Assertions.assertThrows(NullPointerException.class,()->new SchedulerRunner(null,null));
        Assertions.assertThrows(NullPointerException.class,()->new SchedulerRunner(Mockito.mock(Schedule.class),null));
        Assertions.assertDoesNotThrow(()->new SchedulerRunner(Mockito.mock(Schedule.class),new Object()));
        Assertions.assertDoesNotThrow(SchedulerRunner::start);


        var schedulerInstance=new PrivateScheduleInstance();
        var scheduleAnnotation=schedulerInstance.getClass().getAnnotation(Schedule.class);
        var schedulerRunner=new SchedulerRunner(scheduleAnnotation,schedulerInstance);
        schedulerRunner.setLimitExecutionTime(null);
        Assertions.assertNotNull(schedulerRunner.getScheduleAnnotation());
        Assertions.assertNotNull(schedulerRunner.getInstanceMethods());
        Assertions.assertNotNull(schedulerRunner.getLimitExecutionTime());
        Assertions.assertNotNull(schedulerRunner.getSchedulerInstance());


        Assertions.assertDoesNotThrow(schedulerRunner::getInstanceMethods);
        Assertions.assertNotNull(schedulerRunner.getInstanceMethods());
        Assertions.assertNotEquals(schedulerRunner.getInstanceMethods().length,0);

        Assertions.assertDoesNotThrow(()->schedulerRunner.createSchedulerTask(null, null));
        Assertions.assertDoesNotThrow(()->schedulerRunner.createSchedulerTask(null, Scheduler.Checker.from(scheduleAnnotation).orElse(null)));
        Assertions.assertNull(schedulerRunner.createSchedulerTask(null, null));
        Assertions.assertNull(schedulerRunner.createSchedulerTask(null, Scheduler.Checker.from(scheduleAnnotation).orElse(null)));

        var checker=Scheduler.Checker.from(scheduleAnnotation).orElse(null);
        for(var method:schedulerRunner.getInstanceMethods()){
            Assertions.assertDoesNotThrow(()->schedulerRunner.createSchedulerTask(method, null));
            Assertions.assertNull(schedulerRunner.createSchedulerTask(method, null));
            Assertions.assertDoesNotThrow(()->schedulerRunner.createSchedulerTask(method, checker));
            Assertions.assertDoesNotThrow(()->schedulerRunner.createSchedulerTask(method, checker));
        }
        Assertions.assertDoesNotThrow(schedulerRunner::schedulesMaker);
        Assertions.assertDoesNotThrow(schedulerRunner::schedulesRunner);
        schedulerRunner.setLimitExecutionTime(LocalDateTime.now().plusSeconds(2));
        Assertions.assertDoesNotThrow(schedulerRunner::run);

    }

    @NoArgsConstructor
    @Schedule(checker = Scheduler.Checker.class)
    private static class PrivateScheduleInstance{
        public void doScheduler(){
            log.info("doScheduler: {}",LocalDateTime.now());
        }

    }



}
