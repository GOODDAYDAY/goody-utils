package com.goody.utils.jinjunmei.service;

import com.goody.utils.jinjunmei.feign.JinJunMeiFeign;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * TODO
 *
 * @author Goody
 * @version 1.0, 2023/5/12
 * @since 1.0.0
 */
@Service
@RequiredArgsConstructor
public class GoodyManager {
    private final JinJunMeiFeign jinJunMeiFeign;

    @Scheduled(fixedDelay = 1000L)
    public void toy() {
        System.out.println(jinJunMeiFeign.query2("wuhu"));
    }
}
