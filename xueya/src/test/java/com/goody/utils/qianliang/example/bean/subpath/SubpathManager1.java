package com.goody.utils.qianliang.example.bean.subpath;

import com.goody.utils.qianliang.example.bean.Manager;
import com.goody.utils.xueya.bean.Autowired;
import com.goody.utils.xueya.bean.Component;

/**
 * manager in subpath
 *
 * @author Goody
 * @version 1.0, 2022/5/10
 * @since 1.0.0
 */
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
}
