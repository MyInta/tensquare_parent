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

    /**
     * 更新粉丝和关注数
     * @param userid
     * @param friendid
     * @param x
     */
    @RequestMapping(value = "/user/{userid}/{friendid}/{x}", method = RequestMethod.PUT)
    public void updateFansCountAndFollowCount(@PathVariable("userid") String userid,
                                               @PathVariable("friendid") String friendid, @PathVariable("x") int x);

    /**
     * 增加粉丝
     * @param userid
     * @param x
     */
    @RequestMapping(value = "/incfans/{userid}/{x}", method = RequestMethod.PUT)
    public void updateFansCount(@PathVariable("userid") String userid, @PathVariable("x") int x);

    /**
     * 增加关注数
     * @param userid
     * @param x
     */
    @RequestMapping(value = "/incfol/{userid}/{x}", method = RequestMethod.PUT)
    public void updateFollowCount(@PathVariable("userid") String userid, @PathVariable("x") int x);
}
