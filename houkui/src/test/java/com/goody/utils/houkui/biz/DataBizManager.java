package com.goody.utils.houkui.biz;

import com.goody.utils.houkui.client.WeChatRobotClient;
import com.goody.utils.houkui.client.WechatConfig;
import com.goody.utils.houkui.datahandler.DataQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * data send manager
 *
 * @author Goody
 * @version 1.0, 2023/1/11 15:02
 * @since 1.0.0
 */
@Component
@RequiredArgsConstructor
@ConditionalOnBean(WechatConfig.class)
public class DataBizManager {
    private final WeChatRobotClient client;
    private final DataQueryMapper mapper;

    @PostConstruct
    public void init() {
        client.send(mapper.query().toString());
    }
}
