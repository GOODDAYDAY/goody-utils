package com.goody.utils.baihao.valuechecker;

import com.goody.utils.baihao.TestApplication;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * {@link ValueCheckerAspect} test
 *
 * @author Goody
 * @version 1.0, 2021/3/26
 * @since 1.0.0
 */
@SpringBootTest(classes = TestApplication.class)
@ContextConfiguration
public class ValueCheckerAspectTest {

    @Autowired
    private TargetService targetService;

    @Test
    public void check_success() {
        Assertions.assertDoesNotThrow(() -> targetService.checker(SampleCheckerHandlerImpl.CORRECT_ID, SampleCheckerHandlerImpl.CORRECT_NAME));
        Assertions.assertDoesNotThrow(() -> targetService.checker(new TargetService.Node(SampleCheckerHandlerImpl.CORRECT_ID, SampleCheckerHandlerImpl.CORRECT_NAME)));
        Assertions.assertDoesNotThrow(() -> targetService.checker(SampleCheckerHandlerImpl.CORRECT_NAME));
        Assertions.assertDoesNotThrow(() -> targetService.checker(SampleCheckerHandlerImpl.CORRECT_ID));
        Assertions.assertDoesNotThrow(() -> targetService.checker());
        Assertions.assertDoesNotThrow(() -> targetService.checkerGetThreadValue(SampleCheckerHandlerImpl.CORRECT_NAME));
        Assertions.assertDoesNotThrow(() -> targetService.checkerPutThreadValue(SampleCheckerHandlerImpl.CORRECT_NAME));
    }

    @Test
    public void check_fail() {
        Assertions.assertThrows(ValueIllegalException.class,
            () -> targetService.checker(SampleCheckerHandlerImpl.ERROR_ID, SampleCheckerHandlerImpl.ERROR_NAME));

        Assertions.assertThrows(ValueIllegalException.class,
            () -> targetService.checker(SampleCheckerHandlerImpl.ERROR_NAME));

        Assertions.assertThrows(ValueIllegalException.class,
            () -> targetService.checker(SampleCheckerHandlerImpl.ERROR_ID));

        Assertions.assertThrows(ValueIllegalException.class,
            () -> targetService.checkerError());

        Assertions.assertThrows(ValueIllegalException.class,
            () -> targetService.checker(new TargetService.Node(SampleCheckerHandlerImpl.ERROR_ID, SampleCheckerHandlerImpl.ERROR_NAME)));

        // reentrant login
        // aop1 - 1.1 counter = 0    init ThreadLocal
        // aop1 - 1.2 counter = 0    set RIGHT_VALUE to ThreadLocal

        // aop2 - 2.1 counter = 1    init ThreadLocal
        // aop2 - 2.2 counter = 1    try to set RIGHT_VALUE to ThreadLocal (success)
        // aop2 - 2.3 counter = 0    clear ThreadLocal (if not reentrant, RIGHT_VALUE will be clear)

        // aop3 - 3.1 counter = 1    init ThreadLocal
        // aop3 - 3.2 counter = 1    try to set WRONG_VALUE to ThreadLocal (fail) (if not reentrant, WRONG_VALUE will be put into ThreadLocal)
        // aop3 - 3.3 counter = 0    clear ThreadLocal

        // aop1 - 1.3 counter = null clear ThreadLocal
        Assertions.assertThrows(ValueIllegalException.class, () -> targetService.checkerReentrant(SampleCheckerHandlerImpl.CORRECT_NAME));
    }
}
