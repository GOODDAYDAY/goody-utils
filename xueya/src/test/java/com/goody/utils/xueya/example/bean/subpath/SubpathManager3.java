package com.goody.utils.xueya.example.bean.subpath;

import com.goody.utils.xueya.bean.Autowired;
import com.goody.utils.xueya.bean.Component;
import com.goody.utils.xueya.bean.Scope;
import com.goody.utils.xueya.example.aop.Aop1;
import com.goody.utils.xueya.example.bean.Manager;

/**
 * manager in subpath prototype and can not autowired itself
 *
 * @author Goody
 * @version 1.0, 2022/5/10
 * @since 1.0.0
 */
@Aop1
@Component(value = "subpathManager3", scope = Scope.PROTOTYPE)
public class SubpathManager3 {

    @Autowired
    private SubpathManager1 subpathManager1;
    @Autowired
    private SubpathManager2 subpathManager2;
    @Autowired
    private Manager manager;

    public SubpathManager3() {
        // cglib proxy will generate twice
        System.out.println("SubpathManager3 init");
    }

    public void toy() {
        System.out.println("subpathManager3 do something ...");
    }
}
