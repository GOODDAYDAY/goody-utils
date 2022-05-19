package com.goody.utils.xueya;

import com.goody.utils.xueya.bean.ComponentScan;
import com.goody.utils.xueya.bean.XueyaApplicationContext;
import com.goody.utils.xueya.example.bean.Manager;
import com.goody.utils.xueya.example.bean.subpath.SubpathManager3;

/**
 * main class
 *
 * @author Goody
 * @version 1.0, 2022/5/9
 * @since 1.0.0
 */
@ComponentScan(path = {
        "com.goody.utils.xueya.example.bean",
        "com.goody.utils.xueya.example.aop"
})
public class Application {
    public static void main(String[] args) {
        // manager init
        // SubpathManager2 init
        // SubpathManager1 init
        // SubpathManager2 init
        // SubpathManager3 init
        // SubpathManager3 init
        // SubpathManager3 init
        // SubpathManager3 init
        // SubpathManager3 init
        // SubpathManager3 init
        new XueyaApplicationContext(Application.class);

        // ++++++++++++++++++++++
        // manager do something ...
        // aspect do something ...
        // subpathManager2 do something ...
        // subpathManager1 do something ...
        // aspect do something ...
        System.out.println("++++++++++++++++++++++");
        final Manager manager = XueyaApplicationContext.getBean("manager");
        manager.toy();

        // ----------------------
        // SubpathManager3 init
        // SubpathManager3 init
        // aspect do something ...
        // subpathManager3 do something ...
        // aspect do something ...
        System.out.println("----------------------");
        final SubpathManager3 subpathManager3 = XueyaApplicationContext.getBean("subpathManager3");
        subpathManager3.toy();
    }
}
