package com.zyc.security;

import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.alibaba.fastjson.JSON;
import com.zyc.baselibs.commons.CollectionUtils;
import com.zyc.baselibs.commons.StringUtils;
import com.zyc.baselibs.entities.DataStatus;
import com.zyc.baselibs.web.ResponseResult;
import com.zyc.security.entities.User;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
//@MapperScan(basePackages = { "com.zyc.baselibs.dao", "com.zyc.security.dao" })
public class UserControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;
    
    private static final Map<String, User> CACHE_USER = new HashMap<String, User>();
    
	@Before
    public void init() {
    	URI uri = URI.create("http://127.0.0.1:8081/user/all");
    	ResponseEntity<String> response = this.testRestTemplate.getForEntity(uri, String.class);
    	ResponseResult result = JSON.parseObject(response.getBody(), ResponseResult.class);

    	assertEquals(result.getStatus(), "0");

    	List<User> users = JSON.parseArray(JSON.toJSONString(result.getData()), User.class);
    	
    	if(CollectionUtils.hasElement(users)) {
    		for (User user : users) {
    			CACHE_USER.put(user.getId(), user);
			}
    	}
    }
    
    @Test
    public void createTest() {
    	URI uri = URI.create("http://127.0.0.1:8081/user/create");
    	
    	User user = new User();
    	
    	ResponseEntity<String> response = this.testRestTemplate.postForEntity(uri, user, String.class);
    	ResponseResult result = JSON.parseObject(response.getBody(), ResponseResult.class);
    	//断言：待创建用户的必填字段没有值，此次用户创建失败（status=1）
    	assertEquals(result.getStatus(), "1");
    	
    	user.setId(UUID.randomUUID().toString());
    	user.setUsername(StringUtils.randomAlphabets(10));
    	user.setNickname(user.getUsername().toLowerCase());
    	user.init();
    	response = this.testRestTemplate.postForEntity(uri, user, String.class);
    	result = JSON.parseObject(response.getBody(), ResponseResult.class);
    	//断言：待创建用户的必填字段有值，此次用户创建成功（status=0）
    	assertEquals(result.getStatus(), "0");
    	
    	User cuser = JSON.parseObject(JSON.toJSONString(result.getData()), User.class);
    	
    	//断言：用户创建成功（status=0），返回的最新用户对象与待创建用户对象必然数据相等
    	assertEquals(user.equals(cuser), true);
    	
    	CACHE_USER.put(cuser.getId(), cuser);

    	response = this.testRestTemplate.postForEntity(uri, cuser, String.class);
    	result = JSON.parseObject(response.getBody(), ResponseResult.class);
    	//断言：将已经创建的用户再创建一次，此次用户创建失败（status=1）
    	assertEquals(result.getStatus(), "1");
    	
    	User u2 = new User();
		BeanUtils.copyProperties(cuser, u2);
		u2.setUsername(StringUtils.randomAlphabets(10));
    	response = this.testRestTemplate.postForEntity(uri, u2, String.class);
    	result = JSON.parseObject(response.getBody(), ResponseResult.class);
    	//断言：复制已创建用户，保持id不变，仅改变username，此次用户创建失败（status=1）
    	assertEquals(result.getStatus(), "1");

    	User u3 = new User();
		BeanUtils.copyProperties(cuser, u3);
		u3.setId(UUID.randomUUID().toString());
    	response = this.testRestTemplate.postForEntity(uri, u3, String.class);
    	result = JSON.parseObject(response.getBody(), ResponseResult.class);
    	//断言：复制已创建用户，保持username不变，仅改变id，此次用户创建失败（status=1）
    	assertEquals(result.getStatus(), "1");
    }
    
    @Test
    public void modifyTest() {
    	URI uri = URI.create("http://127.0.0.1:8081/user/modify");
    	
    	ResponseEntity<String> response = null;
    	ResponseResult result = null;
    	User user = null, nuser = null;
    	
    	for (Map.Entry<String, User> entry : CACHE_USER.entrySet()) {
    		user = new User();
    		user.setId(entry.getValue().getId());
    		user.setUsername(entry.getValue().getUsername());
    		user.setNickname(entry.getValue().getUsername());
        	user.setDatastatus(DataStatus.ENABLED.toString());
    		
    		response = this.testRestTemplate.postForEntity(uri, user, String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	nuser = JSON.parseObject(JSON.toJSONString(result.getData()), User.class);

        	CACHE_USER.put(nuser.getId(), nuser);
        	
        	//断言：用户修改成功（staus=0）
        	assertEquals(result.getStatus(), "0");
        	//断言：用户nickname发生改变
        	assertEquals(nuser.getNickname().equals(user.getNickname()), true);

    		user.setUsername(entry.getValue().getId());
    		response = this.testRestTemplate.postForEntity(uri, user, String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	//断言：用户username是禁止修改的，用户修改失败（staus=1）
        	assertEquals(result.getStatus(), "1");
		}
    }
    
    @Test
    public void deleteAndModifyAndDetailsTest() {
    	String url = "http://127.0.0.1:8081/user/delete";
    	URI uri = URI.create(url);
    	
    	ResponseEntity<String> response = null;
    	ResponseResult result = null;
    	User user = null;
    	int count = 0;
    	for (Map.Entry<String, User> entry : CACHE_USER.entrySet()) {
    		entry.getValue().setDatastatus(count % 2 == 0 ? DataStatus.LOCKED.toString() : DataStatus.DELETED.toString());
    		response = this.testRestTemplate.postForEntity(URI.create("http://127.0.0.1:8081/user/modify"), entry.getValue(), String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	//断言：用户修改成功（staus=0）
        	assertEquals(result.getStatus(), "0"); 

        	MultiValueMap<String, Object> request = new LinkedMultiValueMap<String, Object>();
        	request.add("id", entry.getKey());
        	HttpHeaders headers = new HttpHeaders();
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(request, headers);
            
            response = this.testRestTemplate.postForEntity(uri, httpEntity, String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	//断言：对已删除或已锁定的数据进行删除，用户删除失败（staus=1）
        	assertEquals(result.getStatus(), "1"); 

    		/*
    		this.testRestTemplate.delete(url, request);
    		response = this.testRestTemplate.getForEntity(URI.create("http://127.0.0.1:8081/user/details?id=" + entry.getKey()), String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	user = JSON.parseObject(JSON.toJSONString(result.getData()), User.class);
        	assertEquals(user.getDatastatus().equals(DataStatus.DELETED.toString()), false); 
        	*/
             
    		entry.getValue().setDatastatus(DataStatus.ENABLED.toString());
    		response = this.testRestTemplate.postForEntity(URI.create("http://127.0.0.1:8081/user/modify"), entry.getValue(), String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	//断言：用户修改成功（staus=0）
        	assertEquals(result.getStatus(), "0"); 

        	/*
        	this.testRestTemplate.delete(url, request);
    		response = this.testRestTemplate.getForEntity(URI.create("http://127.0.0.1:8081/user/details?id=" + entry.getKey()), String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	user = JSON.parseObject(JSON.toJSONString(result.getData()), User.class);
        	assertEquals(user.getDatastatus().equals(DataStatus.DELETED.toString()), true); 
        	*/

            response = this.testRestTemplate.postForEntity(url, httpEntity, String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	//断言：对已删除或已锁定的数据进行删除，用户删除失败（staus=1）
        	assertEquals(result.getStatus(), "0"); 
        	entry.getValue().setDatastatus(DataStatus.DELETED.toString());
    		
    		response = this.testRestTemplate.getForEntity(URI.create("http://127.0.0.1:8081/user/details?id=" + entry.getKey()), String.class);
        	result = JSON.parseObject(response.getBody(), ResponseResult.class);
        	user = JSON.parseObject(JSON.toJSONString(result.getData()), User.class);
        	//断言：已删除的数据，状态为deleted
        	assertEquals(user.getDatastatus(), entry.getValue().getDatastatus()); 
        	
			if(++count >= 5) {
    			break;
    		}
		}
    	
    	
    }

}
