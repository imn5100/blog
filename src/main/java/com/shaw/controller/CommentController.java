package com.shaw.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.shaw.entity.Comment;
import com.shaw.service.CommentService;
import com.shaw.util.ResponseUtil;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/comment")
public class CommentController {

	@Autowired
	private CommentService commentService;

	/**
	 * 添加或者修改评论
	 * 
	 * @param comment
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/save")
	public void save(Comment comment, @RequestParam("imageCode") String imageCode, HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws Exception {
		String sRand = (String) session.getAttribute("sRand"); // 获取系统生成的验证码
		JSONObject result = new JSONObject();
		int resultTotal = 0; // 操作的记录条数
		if (!imageCode.equalsIgnoreCase(sRand)) {
			result.put("success", false);
			result.put("errorInfo", "验证码填写错误！");
		} else {
			String userIp = request.getRemoteAddr(); // 获取用户IP
			comment.setUserIp(userIp);
			if (comment.getId() == null) {
				resultTotal = commentService.add(comment);
			} else {

			}
			if (resultTotal > 0) {
				result.put("success", true);
			} else {
				result.put("success", false);
			}
		}
		ResponseUtil.write(response, result);
	}

}
