package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author inta
 * @date 2019/10/24
 * @describe
 */
public interface SpitDao extends MongoRepository<Spit, String> {
    /**
     * 分页形式，根据id返回查询列表
     * @param parentid
     * @param pageable
     * @return
     */
    public Page<Spit> findByParentid(String parentid, Pageable pageable);
}
