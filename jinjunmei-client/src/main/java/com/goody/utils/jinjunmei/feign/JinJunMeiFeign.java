package com.goody.utils.jinjunmei.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * TODO
 *
 * @author Goody
 * @version 1.0, 2023/5/12
 * @since 1.0.0
 */
@FeignClient(value = "${jinjunmei.server}", configuration = LoadBalancerConfig.class, path = "/goody")
public interface JinJunMeiFeign {

    @RequestMapping(method = RequestMethod.GET, path = "/1")
    String query(String form);

    @RequestMapping(method = RequestMethod.GET, path = "/2")
    String query2(String form);
}
