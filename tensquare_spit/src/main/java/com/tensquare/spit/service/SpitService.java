package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.YamlProcessor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import java.util.Date;
import java.util.List;

/**
 * @author inta
 * @date 2019/10/24
 * @describe
 */
@Service
@Transactional
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 查询全部记录
     * @return
     */
    public List<Spit> findAll() {
        return spitDao.findAll();
    }

    /**
     * 根据id查询记录
     * @param id
     * @return
     */
    public Spit findById(String id) {
        return spitDao.findById(id).get();
    }

    /**
     * 增加
     * @param spit
     */
    public void save(Spit spit) {
        spit.set_id(idWorker.nextId() + "");
        spit.setPublishtime(new Date());//发布日期
        spit.setVisits(0);//浏览量
        spit.setShare(0);//分享数
        spit.setThumbup(0);//点赞数
        spit.setComment(0);//回复数
        spit.setState("1");//状态
        //如果当前添加的吐槽有父节点，吐槽的回复数+1
        if (spit.getParentid() != null && !"".equals(spit.getParentid())) {
            Query query = new Query();
            //找到父表
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));
            Update update = new Update();
            //回复数+1
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "spit");
        }
        spitDao.save(spit);
    }

    /**
     * 更新修改
     * @param spit
     */
    public void update(Spit spit) {
        spitDao.save(spit);
    }

    /**
     * 根据id删除
     * @param id
     */
    public void deleteById(String id) {
        spitDao.deleteById(id);
    }

    public Page<Spit> findByParentid(String parentid, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        return spitDao.findByParentid(parentid, pageable);
    }

    /**
     * 根据id点赞
     * @param spitId
     */
    public void thumbup(String spitId) {
        //方式一 效率低 进行了两次数据库交互
        //        Spit spit = spitDao.findById(spitId).get();
//        spit.setThumbup((spit.getThumbup() == null ? 0 : spit.getThumbup()) + 1);
//        spitDao.save(spit);
        //方法二，使用原生mongo命令来执行实现自增 与数据库只进行一次交互，效率高
        // 等同于底层的db.spit.update({"_id":"1"}, {$inc:{thumbup:NumberInt(1)}})
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup", 1);
        mongoTemplate.updateFirst(query, update, "spit");

    }
}
