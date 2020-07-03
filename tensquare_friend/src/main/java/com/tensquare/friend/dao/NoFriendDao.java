package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author inta
 * @date 2019/11/8
 * @describe
 */
public interface NoFriendDao extends JpaRepository<NoFriend, String> {
    public NoFriend findByUseridAndFriendid(String userid, String friendid);
}
