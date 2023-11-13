package com.littlecode.scheduler.privates;

import com.littlecode.util.BeanUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Component
@RequiredArgsConstructor
public class Engine {
    private final Setting setting;
    private final BeanUtil beanUtil;

    public Setting setting() {
        return setting;
    }

    public void start() {
        Runner.start(beanUtil);
    }


}