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
    }
}
