package com.littlecode.scheduler.privates;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class Engine {
    private final Factory factory;

    public Setting setting() {
        return this.factory.setting();
    }

    public void start() {
        Runner.start(factory);
    }


}