package com.goody.utils.jinjunmei.dingding;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import com.taobao.api.ApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class DingTalkClient {

    private static final String DING_TALK_ROBOT_SERVER_URL = "https://oapi.dingtalk.com/robot/send?access_token=%s";

    public boolean sendActionCard(String title, String singleTitle, String content, List<String> phoneList, String dingtalkToken) throws ApiException {
        OapiRobotSendRequest.Actioncard actioncard = new OapiRobotSendRequest.Actioncard();
        actioncard.setTitle(title);
        actioncard.setText(content);
        actioncard.setSingleTitle(singleTitle);

        OapiRobotSendRequest.At at = new OapiRobotSendRequest.At();
        at.setAtMobiles(phoneList);

        OapiRobotSendRequest request = new OapiRobotSendRequest();
        request.setMsgtype("actionCard");
        request.setAt(at);
        request.setActionCard(actioncard);

        DefaultDingTalkClient client = new DefaultDingTalkClient(String.format(DING_TALK_ROBOT_SERVER_URL, dingtalkToken));
        OapiRobotSendResponse response = client.execute(request);
        if (!response.isSuccess()) {
            log.error("ElapsedAlarmer doAlarm send fail, errCode:{}, errMsg:{}", response.getErrcode(), response.getErrmsg());
        }

        return response.isSuccess();
    }
}
