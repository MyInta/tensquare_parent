package com.tensquare.user.service;

import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class UserService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private IdWorker idWorker;

	@Autowired
	private RedisTemplate redisTemplate;

	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Autowired
	private BCryptPasswordEncoder encoder;

	/**
	 * 查询全部列表
	 * @return
	 */
	public List<User> findAll() {
		return userDao.findAll();
	}

	
	/**
	 * 条件查询+分页
	 * @param whereMap
	 * @param page
	 * @param size
	 * @return
	 */
	public Page<User> findSearch(Map whereMap, int page, int size) {
		Specification<User> specification = createSpecification(whereMap);
		PageRequest pageRequest =  PageRequest.of(page-1, size);
		return userDao.findAll(specification, pageRequest);
	}

	
	/**
	 * 条件查询
	 * @param whereMap
	 * @return
	 */
	public List<User> findSearch(Map whereMap) {
		Specification<User> specification = createSpecification(whereMap);
		return userDao.findAll(specification);
	}

	/**
	 * 根据ID查询实体
	 * @param id
	 * @return
	 */
	public User findById(String id) {
		return userDao.findById(id).get();
	}

	/**
	 * 增加
	 * TODO 需要考虑事务了，因为同一号码不能多次注册，数据库操作会出异常
	 * @param user
	 */
	public void add(User user) {
		user.setId( idWorker.nextId()+"" );
		//密码加密
		user.setPassword(encoder.encode(user.getPassword()));
		user.setFollowcount(0);//关注数
		user.setFanscount(0);//粉丝数
		user.setOnline(0L);//在线时长
		user.setRegdate(new Date());//注册日期
		user.setUpdatedate(new Date());//更新日期
		user.setLastdate(new Date());//最后登陆日期
		userDao.save(user);
	}

	/**
	 * 根据验证码注册用户
	 * @param user
	 * @param code
	 */
	public void registUser(User user, String code) {
		String userCheckCode = (String) redisTemplate.opsForValue().get("checkcode_" + user.getMobile());
		if (userCheckCode == null || userCheckCode.isEmpty()) {
			throw new RuntimeException("请先获取手机验证码");
		}
		if (!userCheckCode.equals(code)) {
			throw new RuntimeException("验证码错误");
		}
		userDao.save(user);
	}

	/**
	 * 修改
	 * @param user
	 */
	public void update(User user) {
		userDao.save(user);
	}

	/**
	 * 删除
	 * @param id
	 */
	public void deleteById(String id) {
		userDao.deleteById(id);
	}

	/**
	 * 动态条件构建
	 * @param searchMap
	 * @return
	 */
	private Specification<User> createSpecification(Map searchMap) {

		return new Specification<User>() {

			@Override
			public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
				List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id")!=null && !"".equals(searchMap.get("id"))) {
                	predicateList.add(cb.like(root.get("id").as(String.class), "%"+(String)searchMap.get("id")+"%"));
                }
                // 手机号码
                if (searchMap.get("mobile")!=null && !"".equals(searchMap.get("mobile"))) {
                	predicateList.add(cb.like(root.get("mobile").as(String.class), "%"+(String)searchMap.get("mobile")+"%"));
                }
                // 密码
                if (searchMap.get("password")!=null && !"".equals(searchMap.get("password"))) {
                	predicateList.add(cb.like(root.get("password").as(String.class), "%"+(String)searchMap.get("password")+"%"));
                }
                // 昵称
                if (searchMap.get("nickname")!=null && !"".equals(searchMap.get("nickname"))) {
                	predicateList.add(cb.like(root.get("nickname").as(String.class), "%"+(String)searchMap.get("nickname")+"%"));
                }
                // 性别
                if (searchMap.get("sex")!=null && !"".equals(searchMap.get("sex"))) {
                	predicateList.add(cb.like(root.get("sex").as(String.class), "%"+(String)searchMap.get("sex")+"%"));
                }
                // 头像
                if (searchMap.get("avatar")!=null && !"".equals(searchMap.get("avatar"))) {
                	predicateList.add(cb.like(root.get("avatar").as(String.class), "%"+(String)searchMap.get("avatar")+"%"));
                }
                // E-Mail
                if (searchMap.get("email")!=null && !"".equals(searchMap.get("email"))) {
                	predicateList.add(cb.like(root.get("email").as(String.class), "%"+(String)searchMap.get("email")+"%"));
                }
                // 兴趣
                if (searchMap.get("interest")!=null && !"".equals(searchMap.get("interest"))) {
                	predicateList.add(cb.like(root.get("interest").as(String.class), "%"+(String)searchMap.get("interest")+"%"));
                }
                // 个性
                if (searchMap.get("personality")!=null && !"".equals(searchMap.get("personality"))) {
                	predicateList.add(cb.like(root.get("personality").as(String.class), "%"+(String)searchMap.get("personality")+"%"));
                }
				
				return cb.and( predicateList.toArray(new Predicate[predicateList.size()]));

			}
		};

	}

	/**
	 * 发短信
	 * @param mobile
	 */
    public void sedSms(String mobile) {
		//生成6位数字随机数
		String checkcode = RandomStringUtils.randomNumeric(6);
		//向缓存中存一份 后两个参数指定6小时后过期
		redisTemplate.opsForValue().set("checkcode_" + mobile, checkcode, 6, TimeUnit.HOURS);
		Map<String, String> map = new HashMap<>();
		map.put("mobile", mobile);
		map.put("checkcode", checkcode);
		//向用户发一份 (先注释掉，测试用，以防老是去发短信啊。。。)
//		rabbitTemplate.convertAndSend("sms", map);
		//在控制台显示一份（方便测试）
		System.out.println("验证码为：" + checkcode);
    }

	public User login(String mobile, String password) {
		User user = userDao.findByMobile(mobile);
		if (user != null && encoder.matches(password, user.getPassword())) {
			return user;
		}
		return null;
	}

	/**
	 * 增加粉丝
	 * @param userid
	 * @param x
	 */
	@Transactional
	public void incFanscount(String userid, int x) {
    	userDao.updatefanscount(x, userid);
	}

	/**
	 * 增加粉丝
	 * @param userid
	 * @param x
	 */
	@Transactional
	public void incFollowCount(String userid, int x) {
    	userDao.updatefollowcount(x, userid);
	}

	@Transactional
	public void updateFansCountAndFollowCount(int x, String userid, String friendid) {
		incFanscount(userid, x);
		incFollowCount(friendid, x);
	}
}
