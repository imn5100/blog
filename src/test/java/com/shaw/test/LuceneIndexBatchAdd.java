package com.shaw.test;

import com.shaw.entity.Blog;
import com.shaw.lucene.BlogIndex;
import com.shaw.service.BlogService;
import com.shaw.util.DateUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 进行服务器迁移时
 * Lucene 建立的索引丢失或者不可用，再次重新建立所有的索引
 * Blog 太多可能有性能问题。可以考虑分批添加index
 * */
public class LuceneIndexBatchAdd extends SpringTestCase {
	private BlogIndex blogIndex = new BlogIndex();

	@Autowired
	private BlogService blogService;

	@Test
	public void setAllIndex() throws Exception {
		List<Blog> blogs = blogService.list(null);
		IndexWriter writer = blogIndex.getWriter();
		for (Blog blog : blogs) {
			Document doc = new Document();
			doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
			doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
			doc.add(new StringField("releaseDate", DateUtil.formatDate(blog.getReleaseDate(), "yyyy-MM-dd"), Field.Store.YES));
			doc.add(new TextField("content", Jsoup.parse(blog.getContent()).text(), Field.Store.YES));
			writer.addDocument(doc);
		}
		writer.close();
	}

}
