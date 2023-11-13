package com.littlecode.tests;

import com.littlecode.threads.TaskRunner;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class ThreadsTaskRunnerTest {

//    private TaskRunner<Integer, Integer> runner;
//
//    private TaskRunner<Integer, Integer> makeRunnerError() {
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++)
//            list.add(i);
//        return TaskRunner.of(
//                TaskRunner.Target.<Integer, Integer>builder()
//                        .threadCount(0)
//                        .beakOnException(true)
//                        .onStarted(runner -> log.debug("runner started"))
//                        .onFail(results -> results.forEach(result -> log.error(result.getException().getMessage())))
//                        .onSuccess(results -> results.forEach(result -> log.debug(result.getResult().toString())))
//                        .onFinished(runner -> log.debug("runner finished"))
//                        .getTaskGetArg(() -> {
//                            var arg = list.get(0);
//                            list.remove(0);
//                            return arg;
//                        })
//                        .onTaskExecute(arg -> {
//                            log.debug("task started: {}", arg);
//                            return arg;
//                        })
//                        .onTaskSuccess(result -> {
//                        })
//                        .onTaskFail(result -> {
//                        })
//                        .build()
//        );
//    }
//
//    private TaskRunner<Integer, Integer> makeRunnerSuccess() {
//        List<Integer> list = new ArrayList<>();
//        for (int i = 0; i < 5; i++)
//            list.add(i);
//        return TaskRunner.of(
//                TaskRunner.Target.<Integer, Integer>builder()
//                        .threadCount(0)
//                        .beakOnException(true)
//                        .onStarted(runner -> log.debug("runner started"))
//                        .onFail(results -> results.forEach(result -> log.error(result.getException().getMessage())))
//                        .onSuccess(results -> results.forEach(result -> log.debug(result.getResult().toString())))
//                        .onFinished(runner -> log.debug("runner finished"))
//                        .getTaskGetArg(() -> {
//                            if (list.isEmpty())
//                                return null;
//                            var arg = list.get(0);
//                            list.remove(0);
//                            return arg;
//                        })
//                        .onTaskExecute(arg -> {
//                            log.debug("task started: {}", arg);
//                            return arg;
//                        })
//                        .onTaskSuccess(result -> {
//                        })
//                        .onTaskFail(result -> {
//                        })
//                        .build()
//        );
//    }

    @Test
    public void UT_CHECK_RUNNER() {
//        runner = this.makeRunnerError();
//        Assertions.assertDoesNotThrow(() -> runner.start().waitToFinished());
//        Assertions.assertTrue(runner.isFail());
//
//        runner = this.makeRunnerSuccess();
//        Assertions.assertDoesNotThrow(() -> runner.start().waitToFinished());
//        Assertions.assertTrue(runner.isSuccessful());
    }


}
