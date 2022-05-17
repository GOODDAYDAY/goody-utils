package com.goody.utils.xueya.example.bean.subpath;

import com.goody.utils.xueya.aop.After;
import com.goody.utils.xueya.aop.Aspect;
import com.goody.utils.xueya.aop.Before;
import com.goody.utils.xueya.bean.Autowired;
import com.goody.utils.xueya.bean.Component;
import com.goody.utils.xueya.example.aop.Aop1;
import com.goody.utils.xueya.example.bean.Manager;

/**
 * manager in subpath
 *
 * @author Goody
 * @version 1.0, 2022/5/10
 * @since 1.0.0
 */
@Aspect(cut = Aop1.class)
@Component("subpathManager1")
public class SubpathManager1 {

    @Autowired
    private SubpathManager1 subpathManager1;
    @Autowired
    private SubpathManager2 subpathManager2;
    @Autowired
    private Manager manager;

    public SubpathManager1() {
        System.out.println("SubpathManager1 init");
    }

    public void toy() {
        System.out.println("manager");
    }

    @Before
    @After
    public void toy1() {
        System.out.println("do something ...");
    }
}
