package com.spring.mongo.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.MDC;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;
import com.spring.mongo.db.utils.Constants;
import com.spring.mongo.documents.Address;
import com.spring.mongo.documents.Gender;
import com.spring.mongo.documents.User;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/config/spring-service.xml" })
public class UserRepositoryTest {

	private Gson gson = null;

	@Autowired
	private UserRepository repository;

	@Before
	public void setUp() throws Exception {
		MDC.put(Constants.MONGO_DB_NAME_KEY, Constants.MONGO_DB_NAME_VALUE);
		gson = new Gson();
	}

	@Test
	public void testBulkInsertUsers() throws Exception {
		List<User> users = users(50);
		repository.insert(users);
	}

	@Test
	public void testGetCount() throws Exception {
		long count = repository.count();
		System.out.println(count);
	}

	@Test
	public void testPagination() throws Exception {
		Page<User> users = repository.findAll(new PageRequest(0, 10));
		System.out.println(users.getContent());
		System.out.println(users.getTotalPages());
		System.out.println(users.getTotalElements());
	}

	@Test
	public void testFindUserByName() throws Exception {
		List<User> users = repository.findUserByName("Vikash Kumar Sinha");
		String jsonAsString = gson.toJson(users);
		System.out.println(jsonAsString);
	}

	@After
	public void tearDown() throws Exception {
		MDC.remove(Constants.MONGO_DB_NAME_KEY);
	}

	private List<User> users(int numberOfUsers) {
		List<User> users = new ArrayList<User>();
		for (int i = 0; i < numberOfUsers; i++) {
			User user = new User();
			user.setName("Vikash Kumar Sinha" + "_"
					+ System.currentTimeMillis());
			user.setIc("Yes");
			user.setAge(35);
			user.setGender(Gender.MALE);
			user.setCreatedDate(new Date());
			Address address = new Address();
			address.setCity("Patna");
			address.setState("Bihar");
			address.setCountry("India");
			user.setAddress(address);
			users.add(user);
		}
		return users;

	}

}
