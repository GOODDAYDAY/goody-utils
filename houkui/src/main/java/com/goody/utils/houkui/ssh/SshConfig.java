package com.goody.utils.houkui.ssh;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

/**
 * ssh config
 *
 * @author Goody
 * @version 1.0, 2023/1/11 15:02
 * @since 1.0.0
 */
@Data
@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "ssh")
@PropertySource(value = "classpath:ssh.properties")
public class SshConfig {

    private List<Item> list;

    @Data
    public static class Item {
        private String sshHost;
        private Integer sshPort;
        private String sshPrivateKeyPath;
        private String sshUsername;

        private String dbHost;
        private Integer dbPort;
        private String dbUsername;
        private String dbPassword;

        private Integer localPortMapping;
        private Duration timeout;
    }
}
