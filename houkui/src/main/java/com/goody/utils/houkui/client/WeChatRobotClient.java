package com.goody.utils.houkui.client;

import lombok.RequiredArgsConstructor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;

/**
 * client for wechat robot
 *
 * @author Goody
 * @version 1.0, 2023/1/11 14:30
 * @since 1.0.1
 */
@Service
@RequiredArgsConstructor
public class WeChatRobotClient {
    public static final okhttp3.MediaType JSON_MEDIA = MediaType.parse("application/json");
    public static final String INPUT_FORMAT = "{\"msgtype\": \"text\",\"text\": {\"content\": \"%s\"}}";

    private final WechatConfig wechatConfig;
    private OkHttpClient okHttpClient;

    @PostConstruct
    public void init() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
            .connectTimeout(Duration.ofMillis(2000L))
            .writeTimeout(Duration.ofMillis(2000L))
            .readTimeout(Duration.ofMillis(2000L));
        OkHttpClient client = clientBuilder.build();
        client.dispatcher().setMaxRequestsPerHost(3);
        client.dispatcher().setMaxRequests(3);
        okHttpClient = clientBuilder.build();
    }

    public void send(String text) {
        final String input = String.format(INPUT_FORMAT, text);
        for (String robot : wechatConfig.getRobots()) {
            final RequestBody jsonBody = RequestBody.create(JSON_MEDIA, input);
            final Request.Builder requestBuilder = new Request.Builder()
                .url(robot)
                .post(jsonBody);
            try {
                this.okHttpClient.newCall(requestBuilder.build()).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
