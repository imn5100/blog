package com.shaw.controller.admin;

import com.shaw.entity.Blog;
import com.shaw.lucene.BlogIndex;
import com.shaw.service.BlogService;
import com.shaw.service.SystemService;
import com.shaw.util.ResponseUtil;
import net.sf.json.JSONObject;
import org.apache.lucene.document.*;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
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
	private SystemService systemService;
	@Autowired
	private BlogService blogService;
	@Autowired
	private BlogIndex blogIndex;

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
		systemService.initBlogData(application);
		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}

	/***
	 * 当索引出问题时，需要重写索引 重写索引
	 */
	@RequestMapping("refreshIndex")
	public String refreshLuceneIndex(HttpServletResponse response) throws Exception {
		IndexWriter writer = blogIndex.getWriter();
		writer.deleteAll();
		List<Blog> blogs = blogService.list(null);
		for (Blog blog : blogs) {
			Document doc = new Document();
			doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
			doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
			doc.add(new TextField("content", Jsoup.parse(blog.getContent()).text(), Field.Store.YES));
			doc.add(new LongField("time", blog.getReleaseDate().getTime(), BlogIndex.TIME_TYPE));
			writer.addDocument(doc);
		}
		writer.close();
		JSONObject result = new JSONObject();
		result.put("success", true);
		ResponseUtil.write(response, result);
		return null;
	}
}
