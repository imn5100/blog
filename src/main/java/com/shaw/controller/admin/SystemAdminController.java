package com.shaw.controller.admin;

import com.shaw.constants.CacheKey;
import com.shaw.entity.Blog;
import com.shaw.entity.BlogType;
import com.shaw.entity.Blogger;
import com.shaw.entity.Link;
import com.shaw.service.BlogService;
import com.shaw.service.BlogTypeService;
import com.shaw.service.BloggerService;
import com.shaw.service.LinkService;
import com.shaw.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Controller
@RequestMapping("/admin/system")
public class SystemAdminController {

	@Autowired
	private BloggerService bloggerService;

	@Autowired
	private BlogTypeService blogTypeService;

	@Autowired
	private BlogService blogService;

	@Autowired
	private LinkService linkService;

	/**
	 * 刷新系统缓存
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/refreshSystem")
	public String refreshSystem(HttpServletResponse response, HttpServletRequest request) throws Exception {
		ServletContext application = RequestContextUtils.getWebApplicationContext(request).getServletContext();
		Blogger blogger = bloggerService.find(); // 查询博主信息
		blogger.setPassword(null);
		application.setAttribute(CacheKey.BLOGGER, blogger);

		List<BlogType> blogTypeCountList = blogTypeService.countList(); // 查询博客类别以及博客的数量
		application.setAttribute(CacheKey.BLOG_TYPE_LIST, blogTypeCountList);

		List<Blog> blogCountList = blogService.countList(); // 根据日期分组查询博客
		application.setAttribute(CacheKey.BLOG_COUNT_LIST, blogCountList);

		List<Link> linkList = linkService.list(null); // 获取所有友情链接
		application.setAttribute(CacheKey.LINK_LIST, linkList);

		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}
