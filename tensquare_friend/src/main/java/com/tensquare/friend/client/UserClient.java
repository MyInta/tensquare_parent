package com.tensquare.friend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author inta
 * @date 2019/11/8
 * @describe
 */
@Component
@FeignClient("tensquare-user")
public interface UserClient {

    @RequestMapping(value = "/user/{userid}/{friendid}/{x}", method = RequestMethod.PUT)
    public void updatefanscountandfollowscount(@PathVariable("userid") String userid,
                                               @PathVariable("friendid") String friendid, @PathVariable("x") int x);

}
