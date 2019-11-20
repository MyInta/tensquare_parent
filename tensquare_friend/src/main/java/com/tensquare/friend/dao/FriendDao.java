package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * @author inta
 * @date 2019/11/8
 * @describe
 */
public interface FriendDao extends JpaRepository<Friend, String> {
    public Friend findByUseridAndFriendid(String userid, String friendid);

    @Modifying
    @Query(value = "UPDATE `tb_friend` SET islike = ? WHERE userid = ? AND friendid = ?", nativeQuery = true)
    public void updateIsLike(String islike, String userid, String friendid);

    @Modifying
    @Query(value = "DELETE from `tb_friend` WHERE userid = ? AND friendid = ?", nativeQuery = true)
    void deleteFriend(String userid, String friendid);
}
