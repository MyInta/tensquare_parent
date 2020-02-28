package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author inta
 * @date 2019/11/8
 * @describe
 */
@Service
@Transactional
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;

    public int addFriend(String userid, String friendid) {
        //先判断userid到friendid是否有数据，有就是重复添加好友了
        Friend friend = friendDao.findByUseridAndFriendid(userid, friendid);
        if (friend != null) {
            return 0;
        }
        //直接添加好友，让用户好友列表中的userid在friendid方向的type为0
        friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");
        friendDao.save(friend);

        userClient.updateFollowCount(userid, 1);//增加自己的关注数
        userClient.updateFansCount(friendid, 1);//增加对方的粉丝数

        //判断从friendid到userid是否有数据，有就将双方的状态都改为1
        if (friendDao.findByUseridAndFriendid(friendid, userid) != null) {
            //把双方的islike都改为1
            friendDao.updateIsLike("1", userid, friendid);
            friendDao.updateIsLike("1", friendid, userid);
        }
        return 1;
    }

    public int addNoFriend(String userid, String friendid) {
        //先判断是否已经是非好友
        NoFriend noFriend = noFriendDao.findByUseridAndFriendid(userid, friendid);
        if (noFriend != null) {
            return 0;
        }
        noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
        return 1;
    }


    public void deleteFriend(String userid, String friendid) {
        //删除表中userid到friendid的数据
        friendDao.deleteFriend(userid, friendid);
        //更新friendid到userid的islike为0
        friendDao.updateIsLike("0", friendid, userid);
        //非好友中添加数据
        addNoFriend(userid, friendid);

        userClient.updateFollowCount(userid, -1);//减少自己的关注数
        userClient.updateFansCount(friendid, -1);//减少对方的粉丝数
    }
}
