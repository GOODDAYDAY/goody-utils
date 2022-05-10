package com.goody.utils.qianliang;

import com.goody.utils.qianliang.example.bean.Manager;
import com.goody.utils.xueya.bean.ComponentScan;
import com.goody.utils.xueya.bean.XueyaApplicationContext;

/**
 * main class
 *
 * @author Goody
 * @version 1.0, 2022/5/9
 * @since 1.0.0
 */
@ComponentScan(path = {"com.goody.utils.qianliang.example.bean"})
public class Application {
    public static void main(String[] args) {
        final XueyaApplicationContext context = new XueyaApplicationContext(Application.class);

        final Manager manager = context.getBean("manager");
        manager.toy();
    }
}
