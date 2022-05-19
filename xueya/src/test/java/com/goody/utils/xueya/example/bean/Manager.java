package com.goody.utils.xueya.example.bean;

import com.goody.utils.xueya.bean.Autowired;
import com.goody.utils.xueya.bean.Component;
import com.goody.utils.xueya.example.bean.subpath.SubpathManager1;
import com.goody.utils.xueya.example.bean.subpath.SubpathManager2;
import com.goody.utils.xueya.example.bean.subpath.SubpathManager3;

/**
 * the manager in example
 *
 * @author Goody
 * @version 1.0, 2022/5/9
 * @since 1.0.0
 */
@Component("manager")
public class Manager {

    @Autowired
    private SubpathManager1 subpathManager1;
    @Autowired
    private SubpathManager2 subpathManager2;
    @Autowired
    private SubpathManager3 subpathManager3;
    @Autowired
    private Manager manager;

    public Manager() {
        System.out.println("manager init");
    }

    public void toy() {
        System.out.println("manager do something ...");
        this.subpathManager2.toy();
    }
}
