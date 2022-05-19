package com.goody.utils.xueya.example.aop;

import com.goody.utils.xueya.aop.After;
import com.goody.utils.xueya.aop.Aspect;
import com.goody.utils.xueya.aop.Before;
import com.goody.utils.xueya.bean.Component;

/**
 * {@link Aop1} aspect
 *
 * @author Goody
 * @version 1.0, 2022/5/19
 * @since 1.0.0
 */
@Aspect(cut = Aop1.class)
@Component("aopAspect")
public class AopAspect {

    @Before
    @After
    public void toy1() {
        System.out.println("aspect do something ...");
    }
}
