package com.littlecode.tests;

import com.littlecode.util.LockerUtil;
import com.littlecode.util.TestsUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
class LockerUtilTest {

    @Test
    @DisplayName("Deve validar Target Getter/Setter")
    void deveValidarTargetGetterSetter() {
        TestsUtil.checkObject(new LockerUtil.Target("",false));
    }

    @Test
    @DisplayName("Deve validar target")
    void deveValidarTarget() {
        var lockerUtil=new LockerUtil();
        Assertions.assertNotNull(lockerUtil.target("locker-1"));
    }

    @Test
    @DisplayName("Deve validar reset")
    void deveValidarReset() {
        var lockerUtil=new LockerUtil();
        lockerUtil.reset();
    }

    @Test
    @DisplayName("Deve validar lock e unlock")
    void deveValidarLock() {
        var lockerUtil=new LockerUtil();
        Assertions.assertTrue(lockerUtil.lock("locker-1"));
        Assertions.assertTrue(lockerUtil.unlock("locker-1"));

        Assertions.assertTrue(lockerUtil.lock());
        Assertions.assertTrue(lockerUtil.unlock());
    }

    @Test
    @DisplayName("Deve validar falha no lock")
    void deveValidarFalhaNoLock() {
        var lockerUtil=new LockerUtil();
        Assertions.assertTrue(lockerUtil.lock("locker-1"));
        Assertions.assertFalse(lockerUtil.lock("locker-1"));

        Assertions.assertTrue(lockerUtil.lock(""));
        Assertions.assertFalse(lockerUtil.lock(""));
        Assertions.assertThrows(IllegalArgumentException.class, () -> lockerUtil.lock(null));
    }

    @Test
    @DisplayName("Deve validar falha no unlock")
    void deveValidarFalhaNoUnLock() {
        var lockerUtil=new LockerUtil();
        Assertions.assertTrue(lockerUtil.lock());
        Assertions.assertTrue(lockerUtil.unlock());
        Assertions.assertFalse(lockerUtil.unlock());
    }

    @Test
    @DisplayName("Deve validar isLocked e unlock")
    void deveValidarIsLocked() {
        var lockerUtil=new LockerUtil();
        {
            Assertions.assertTrue(lockerUtil.lock("locker-1"));
            Assertions.assertTrue(lockerUtil.isLocked("locker-1"));
            Assertions.assertTrue(lockerUtil.unlock("locker-1"));
            Assertions.assertFalse(lockerUtil.isLocked("locker-1"));
        }

        {
            Assertions.assertTrue(lockerUtil.lock());
            Assertions.assertTrue(lockerUtil.isLocked());
            Assertions.assertTrue(lockerUtil.unlock());
            Assertions.assertFalse(lockerUtil.isLocked());
        }
    }

    @Test
    @DisplayName("Deve validar execute com sucesso")
    void deveValidarExecutorComSucesso() {
        var lockerUtil=new LockerUtil();

        Assertions.assertFalse(
                lockerUtil.executor()
                        .onExecute(new LockerUtil.ExecutorOnExecute() {
                            @Override
                            public void exec() {
                                throw new RuntimeException("Test exception");
                            }
                        })
                        .onError(null)
                        .exec()
        );

        Assertions.assertFalse(
                lockerUtil.executor()
                        .onExecute(new LockerUtil.ExecutorOnExecute() {
                            @Override
                            public void exec() {
                                throw new RuntimeException("Test exception");
                            }
                        })
                        .onError(new LockerUtil.ExecutorOnError() {
                            @Override
                            public void exec(Exception e) {
                                log.error(e.getMessage());
                            }
                        })
                        .exec()
        );

        Assertions.assertTrue(
                lockerUtil.executor()
                        .onExecute(new LockerUtil.ExecutorOnExecute() {
                            @Override
                            public void exec() {
                                log.debug("Executando com sucesso-1");
                            }
                        })
                        .exec()
        );

        {
            lockerUtil.target().setLocked(true);
            Assertions.assertFalse(
                    lockerUtil.executor()
                            .onExecute(new LockerUtil.ExecutorOnExecute() {
                                @Override
                                public void exec() {
                                    log.debug("Executando com sucesso-2");
                                }
                            })
                            .exec()
            );
        }


    }
}
