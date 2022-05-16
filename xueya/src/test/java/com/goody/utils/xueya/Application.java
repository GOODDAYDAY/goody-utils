package com.goody.utils.xueya;

import com.goody.utils.xueya.bean.ComponentScan;
import com.goody.utils.xueya.bean.XueyaApplicationContext;
import com.goody.utils.xueya.example.bean.Manager;

/**
 * main class
 *
 * @author Goody
 * @version 1.0, 2022/5/9
 * @since 1.0.0
 */
@ComponentScan(path = {
        "com.goody.utils.xueya.example.bean"
})
public class Application {
    public static void main(String[] args) {
        final XueyaApplicationContext context = new XueyaApplicationContext(Application.class);

        final Manager manager = context.getBean("manager");
        manager.toy();
    }
}
