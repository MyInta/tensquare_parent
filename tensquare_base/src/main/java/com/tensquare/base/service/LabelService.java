package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author inta
 * @date 2019/10/18
 * @describe
 */
@Service
@Transactional
public class LabelService {

    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 查询全部标签
     * @return
     */
    public List<Label> findAll() {
        return labelDao.findAll();
    }

    /**
     * 根据id查询标签
     * @param id
     * @return
     */
    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    /**
     * 增加标签
     * @param label
     */
    public void save(Label label) {
        label.setId(idWorker.nextId() + "");
        labelDao.save(label);
    }

    /**
     * 更新标签
     * @param label
     */
    public void update(Label label) {
        labelDao.save(label);
    }

    /**
     * 根据id删除标签
     * @param id
     */
    public void deleteById(String id) {
        labelDao.deleteById(id);
    }

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Specification<Label> createSpecification(Map searchMap) {
        return new Specification<Label>() {
            /**
             *
             * @param root 根对象，也就是要把条件封装到哪个对象中 where类名=labe.getid
             * @param query 封装的都是查询关键字，比如group by order by等
             * @param cb 用来封装条件对象的 如果直接返回null表示不需要任何条件
             * @return
             */
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                //new 一个集合来存放所有的条件
                List<Predicate> predicateList = new ArrayList<>();
                if (searchMap.get("labelname") != null && !"".equals(searchMap.get("labelname"))) {
                    //相当于where labelname like "%小明%"
                    Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + searchMap.get("labelname") + "%");
                    predicateList.add(predicate);
                }
                if (searchMap.get("state") != null && !"".equals(searchMap.get("state"))) {
                    //相当于state = "1"
                    Predicate predicate = cb.like(root.get("state").as(String.class), (String) searchMap.get("state"));
                    predicateList.add(predicate);
                }
                if (searchMap.get("recommend") != null && !"".equals(searchMap.get("recommend"))) {
                    //相当于state = "1"
                    Predicate predicate = cb.like(root.get("recommend").as(String.class), (String) searchMap.get("recommend"));
                    predicateList.add(predicate);
                }
                //new 一个数组作为最终返回值的条件
                Predicate[] parr = new Predicate[predicateList.size()];
                parr = predicateList.toArray(parr);
                return cb.and(parr);
            }
        };
    }

    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    public List<Label> findSearch(Map searchMap) {
        Specification specification = createSpecification(searchMap);
        return labelDao.findAll(specification);
    }

    public Page<Label> findSearch(Map searchMap, int page, int size) {
        //封装一个分页对象 page - 1 是因为框架需要page从0开始
        Pageable pageable = PageRequest.of(page - 1, size);
        Specification specification = createSpecification(searchMap);
        return labelDao.findAll(specification, pageable);
    }
}
