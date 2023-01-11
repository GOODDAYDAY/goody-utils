package com.goody.utils.houkui.client;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * wechat config
 *
 * @author Goody
 * @version 1.0, 2023/1/11 15:02
 * @since 1.0.0
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "wechat")
@PropertySource(value = "classpath:wechat.properties")
public class WechatConfig {
    private List<String> robots;
}