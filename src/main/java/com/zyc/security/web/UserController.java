package com.zyc.security.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.zyc.baselibs.ex.ExceptionUtils;
import com.zyc.baselibs.web.BaseController;
import com.zyc.baselibs.web.ResponseResult;
import com.zyc.security.entities.User;
import com.zyc.security.service.UserService;

@RestController
public class UserController extends BaseController {
	
	@Autowired
	private UserService userService;
	
	@GetMapping("/user/all")
	@ResponseBody
	public String all() {
		ResponseResult result = new ResponseResult();
		
		try {
			result.setData(this.userService.selectAll());
		} catch (Exception e) {
			result.setStatus("1");
			result.setMessage(ExceptionUtils.uimessage(e));
			e.printStackTrace();
		}

		return JSON.toJSONString(result);
	}
	
	@GetMapping("/user/details")
	@ResponseBody
	public String details(@RequestParam("id") String id) {
		ResponseResult result = new ResponseResult();
		
		try {
			result.setData(this.userService.selectById(id));
		} catch (Exception e) {
			result.setStatus("1");
			result.setMessage(ExceptionUtils.uimessage(e));
			e.printStackTrace();
		}

		return JSON.toJSONString(result);
	}

	@PostMapping("/user/create")
	@ResponseBody
	public String create(@RequestBody User form) {
		ResponseResult result = new ResponseResult();
		
		try {
			result.setData(this.userService.create(form));
		} catch (Exception e) {
			result.setStatus("1");
			result.setMessage(ExceptionUtils.uimessage(e));
			e.printStackTrace();
		}
		
		return JSON.toJSONString(result);
	}
	
	@PostMapping("/user/modify")
	@ResponseBody
	public String modify(@RequestBody User form) {
		ResponseResult result = new ResponseResult();
		
		try {
			result.setData(this.userService.modify(form));
		} catch (Exception e) {
			result.setStatus("1");
			result.setMessage(ExceptionUtils.uimessage(e));
			e.printStackTrace();
		}

		return JSON.toJSONString(result);
	} 
	
	@PostMapping("/user/delete")
	@ResponseBody
	public String delete(@RequestParam("id") String id) {
		ResponseResult result = new ResponseResult();
		
		try {
			this.userService.delete(id);
		} catch (Exception e) {
			result.setStatus("1");
			result.setMessage(ExceptionUtils.uimessage(e));
			e.printStackTrace();
		}
		
		return JSON.toJSONString(result);
	} 
}
