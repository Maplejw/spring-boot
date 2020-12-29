package com.igg.boot.framework;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.test.context.junit4.SpringRunner;

import com.igg.boot.framework.dao.UserDao;
import com.igg.boot.framework.dao.UserOtherDao;
import com.igg.boot.framework.dao.UserOthersDao;
import com.igg.boot.framework.dao.UserPHPDao;
import com.igg.boot.framework.dao.UserShardDao;
import com.igg.boot.framework.jdbc.persistence.bean.JoinItem;
import com.igg.boot.framework.jdbc.persistence.bean.JoinType;
import com.igg.boot.framework.jdbc.persistence.condition.Condition;
import com.igg.boot.framework.model.UserModel;
import com.igg.boot.framework.model.UserOtherModel;
import com.igg.boot.framework.model.UserOthersModel;
import com.igg.boot.framework.model.UserPhpModel;
import com.igg.boot.framework.model.UserShardModel;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DbTest {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserShardDao userShardDao;
	@Autowired
	private UserPHPDao userPhpDao;
	@Autowired
	private UserOtherDao userOtherDao;
	@Autowired
	private UserOthersDao userOthersDao;
	
	/**
	 * 不存在分表的CURD
	 */
	@Test
	public void testCurd() {
		long id = 1L;
		userDao.delete(id);
		UserModel userModel = new UserModel();
		userModel.setPassword("hell");
		userModel.setStatus(1);
		userModel.setUsername("hell");

		userDao.save(userModel, id);
		Map<String, Object> params = new HashMap<>(2);
		params.put("username", "wan");
		params.put("id", id);
		userDao.update("username=:username where id=:id", params);
		UserModel user = userDao.getUserById(id).get();
		Assert.assertEquals("wan", user.getUsername());
		userDao.delete(id);
		Assert.assertEquals(userDao.getUserById(id).isPresent(), false);
	}
	
	/**
	 * 不存在分表的连接查询
	 */
	@Test
	public void testJoin() {
		UserModel userModel = new UserModel();
		userModel.setPassword("ff");
		userModel.setStatus(1);
		userModel.setUsername("ff");
		userDao.save(userModel);
		UserOtherModel userOtherModel = new UserOtherModel();
		userOtherModel.setUid(userModel.getId());
		userOtherDao.save(userOtherModel);
		
		String[] userFields = new String[1];
		userFields[0] = "username";
		JoinItem userJoinItem = new JoinItem(UserModel.class,userFields);
		JoinItem userOtherJoinItem = new JoinItem(UserOtherModel.class,JoinType.LEFT_JOIN,true);
		Condition condition = Condition.eq("user_other.id", userOtherModel.getId());
		List<UserOtherModel> list = userOtherDao.join(condition, new RowMapper<UserOtherModel>() {

			@Override
			public UserOtherModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserOtherModel userOtherModel = new UserOtherModel();
				userOtherModel.setId(rs.getLong("user_other_id"));
				userOtherModel.setUid(rs.getLong("user_other_uid"));
				userOtherModel.setUsername(rs.getString("users_username"));
				
				return userOtherModel;
			}
			
		},userOtherJoinItem,userJoinItem);
		
		Assert.assertEquals(list.get(0).getUsername(), "ff");
	}
	
	
	/**
	 * 存在分表的join
	 */
	@Test
	public void testJoinShard() {
		long id = 1L;
		userOthersDao.delete(id);
		UserModel userModel = new UserModel();
		userModel.setPassword("ff");
		userModel.setStatus(1);
		userModel.setUsername("ff");
		userDao.save(userModel);
		UserOthersModel userOthersModel = new UserOthersModel();
		userOthersModel.setUid(userModel.getId());
		userOthersDao.save(userOthersModel,id);
		String[] userFields = new String[1];
		userFields[0] = "username";
		JoinItem userJoinItem = new JoinItem(UserModel.class,userFields);
		JoinItem userOthersJoinItem = new JoinItem(UserOthersModel.class,JoinType.LEFT_JOIN,true);
		Condition condition = Condition.eq("user_others.id", userOthersModel.getId(),UserOthersModel.class);
		
		List<UserOtherModel> list = userOthersDao.join(condition, new RowMapper<UserOtherModel>() {
			@Override
			public UserOtherModel mapRow(ResultSet rs, int rowNum) throws SQLException {
				UserOtherModel userOtherModel = new UserOtherModel();
				userOtherModel.setUsername(rs.getString("users_username"));
				
				return userOtherModel;
			}
			
		},userOthersJoinItem,userJoinItem);
		Assert.assertEquals(list.get(0).getUsername(), "ff");
	}
	
	/**
	 * 分表根据主键的CURD
	 */
	@Test
	public void testCurdShard() {
		long id = 1L;
		userShardDao.delete(id);
		UserShardModel userModel = new UserShardModel();
		userModel.setPassword("hell");
		userModel.setStatus(1);
		userModel.setUsername("hell");

		userShardDao.save(userModel, id);
		Map<String, Object> params = new HashMap<>(2);
		params.put("username", "wan");
		params.put("id", 1L);
		userShardDao.update("username=:username where id=:id", params);
		UserShardModel user = userShardDao.getUserById(id).get();
		Assert.assertEquals("wan", user.getUsername());
		long id2 = 2L;
		userShardDao.delete(id2);

		Assert.assertEquals(userShardDao.getUserById(id).isPresent(), true);
	}
	
	/**
	 * 分表根据其他字段的CURD
	 */
	@Test
	public void testOtherCurdShard() {
		long id = 1L;
		Condition condition = Condition.eq("username", "hell");
		userPhpDao.deleteCondition(condition);
		UserPhpModel userModel = new UserPhpModel();
		userModel.setPassword("hell");
		userModel.setStatus(1);
		userModel.setUsername("hell");

		userPhpDao.save(userModel, id);
		Map<String, Object> params = new HashMap<>(2);
		params.put("password", "wan");
		params.put("username", "hell");
		userPhpDao.update("password=:password where username=:username", params);

		List<UserPhpModel> userPhpModel = userPhpDao.query(condition);
		Assert.assertEquals(1, userPhpModel.size());
		Assert.assertEquals("wan", userPhpModel.get(0).getPassword());

		condition = Condition.eq("username", "jsa");
		userPhpDao.deleteCondition(condition);
		UserPhpModel userModel1 = new UserPhpModel();
		userModel1.setPassword("s");
		userModel1.setUsername("jsa");
		userModel1.setStatus(2);
		userPhpDao.save(userModel1);
		Map<String, Object> parms = new HashMap<>(1);
		parms.put("username", "jsa");
		List<UserPhpModel> userPhpModel1 = userPhpDao.query("username=:username", parms);
		Assert.assertEquals(1, userPhpModel1.size());
		Assert.assertEquals("jsa", userPhpModel1.get(0).getUsername());

	}

}
