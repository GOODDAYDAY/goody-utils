package com.goody.utils.houkui.ssh;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.sshd.client.SshClient;
import org.apache.sshd.client.session.ClientSession;
import org.apache.sshd.common.config.keys.loader.KeyPairResourceLoader;
import org.apache.sshd.common.keyprovider.KeyIdentityProvider;
import org.apache.sshd.common.util.io.resource.ClassLoaderResource;
import org.apache.sshd.common.util.net.SshdSocketAddress;
import org.apache.sshd.common.util.security.SecurityUtils;
import org.apache.sshd.core.CoreModuleProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.KeyPair;
import java.time.Duration;
import java.util.Collection;

/**
 * sshTunnel
 *
 * @author Goody
 * @version 1.0, 2023/1/11 15:02
 * @since 1.0.0
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SshTunnel {

    private final SshConfig config;

    @PostConstruct
    public void init() throws Exception {
        for (SshConfig.Item item : config.getList()) {
            init(item);
        }
    }

    private void init(SshConfig.Item item) throws Exception {
        SshClient client = SshClient.setUpDefaultClient();
        KeyPairResourceLoader loader = SecurityUtils.getKeyPairResourceParser();
        CoreModuleProperties.IDLE_TIMEOUT.set(client, Duration.ofDays(1000));
        Collection<KeyPair> keys = loader.loadKeyPairs(null, new ClassLoaderResource(SshTunnel.class.getClassLoader(), item.getSshPrivateKeyPath()), null);
        client.setKeyIdentityProvider(KeyIdentityProvider.wrapKeyPairs(keys));
        client.start();
        ClientSession session = client.connect(item.getSshUsername(), item.getSshHost(), item.getSshPort()).verify(item.getTimeout()).getSession();
        session.auth().await(item.getTimeout());
        log.warn("auth: {} , {}", item.getSshHost(), session.isAuthenticated());
        session.createLocalPortForwardingTracker(new SshdSocketAddress("", item.getLocalPortMapping()), new SshdSocketAddress(item.getDbHost(), item.getDbPort()));
    }
}
