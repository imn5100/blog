package com.shaw.lucene;

import com.shaw.annotation.SetCache;
import com.shaw.bo.Blog;
import com.shaw.util.StringUtil;
import com.shaw.util.TimeUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.StringReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 博客索引类
 *
 * @author shaw
 */
public class BlogIndex {

    private Directory dir;
    private Analyzer analyzer;
    public static final String DEAFULT_PATH = "/lucene/blog";

    public BlogIndex() throws Exception {
        this.analyzer = new SmartChineseAnalyzer();
        this.dir = FSDirectory.open(Paths.get(DEAFULT_PATH));
    }

    public IndexWriter getWriter() throws Exception {
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(dir, config);
    }

    public IndexReader getReader() throws Exception {
        return DirectoryReader.open(dir);
    }

    public static final FieldType TIME_TYPE = new FieldType();

    static {
        TIME_TYPE.setTokenized(true);
        TIME_TYPE.setOmitNorms(true);
        TIME_TYPE.setStored(true);
        TIME_TYPE.setIndexOptions(IndexOptions.DOCS);
        TIME_TYPE.setNumericType(FieldType.NumericType.LONG);
        TIME_TYPE.setDocValuesType(DocValuesType.NUMERIC);
        TIME_TYPE.freeze();
    }

    /**
     * 添加博客索引
     *
     * @param blog
     */
    public void addIndex(Blog blog) throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        //存储且索引  id 用于维护索引数据
        doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
        doc.add(new LongField("time", blog.getReleaseDate().getTime(), TIME_TYPE));
        writer.addDocument(doc);
        writer.close();
    }

    /**
     * 更新博客索引
     *
     * @param blog
     * @throws Exception
     */
    public void updateIndex(Blog blog) throws Exception {
        IndexWriter writer = getWriter();
        Document doc = new Document();
        //存储且索引  id 用于维护索引数据
        doc.add(new StringField("id", String.valueOf(blog.getId()), Field.Store.YES));
        doc.add(new TextField("title", blog.getTitle(), Field.Store.YES));
        doc.add(new TextField("content", blog.getContentNoTag(), Field.Store.YES));
        doc.add(new LongField("time", blog.getReleaseDate().getTime(), TIME_TYPE));
        writer.updateDocument(new Term("id", String.valueOf(blog.getId())), doc);
        writer.close();
    }

    /**
     * 删除指定博客的索引
     *
     * @param blogId
     * @throws Exception
     */
    public void deleteIndex(String blogId) throws Exception {
        IndexWriter writer = getWriter();
        writer.deleteDocuments(new Term("id", blogId));
        writer.forceMergeDeletes(); // 强制删除
        writer.commit();
        writer.close();
    }

    /**
     * 查询博客信息
     *
     * @param q 查询关键字
     * @return
     * @throws Exception
     */
    @SetCache(expire = 300L)
    public List<Blog> searchBlog(String q) throws Exception {
        IndexReader reader = getReader();
        // 创建索引Searcher
        IndexSearcher is = new IndexSearcher(reader);
        // 构建搜索条件
        BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
        // 创建中文分词解析
        SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
        QueryParser parser = new QueryParser("title", analyzer);
        Query query = parser.parse(q);
        QueryParser parser2 = new QueryParser("content", analyzer);
        Query query2 = parser2.parse(q);
        // or 作为并集查询。s
        booleanQuery.add(query, BooleanClause.Occur.SHOULD);
        booleanQuery.add(query2, BooleanClause.Occur.SHOULD);

        //(title,content)关联度+时间 排序
        Sort sort = new Sort(new SortField("title", SortField.Type.SCORE), new SortField("content", SortField.Type.SCORE), new SortField("time", SortField.Type.LONG, true));

        // 查询100条记录
        TopDocs hits = is.search(booleanQuery.build(), 100, sort);

        QueryScorer scorer = new QueryScorer(query);
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer,400);
        SimpleHTMLFormatter simpleHTMLFormatter = new SimpleHTMLFormatter("<code color='red'>", "</code>");
        Highlighter highlighter = new Highlighter(simpleHTMLFormatter, scorer);
        highlighter.setTextFragmenter(fragmenter);
        List<Blog> blogList = new ArrayList<Blog>();

        // 遍历搜索结果，构建blog
        for (ScoreDoc scoreDoc : hits.scoreDocs) {
            Document doc = is.doc(scoreDoc.doc);
            Blog blog = new Blog();
            blog.setId(Integer.parseInt(doc.get(("id"))));
            String time = doc.get(("time"));
            if (StringUtils.isNotBlank(time)) {
                try {
                    Long timeStamp = Long.valueOf(time);
                    blog.setReleaseDate(new Date(timeStamp));
                    blog.setReleaseDateStr(TimeUtils.getFormatDay(timeStamp));
                } catch (Exception e) {
                    blog.setReleaseDateStr("");
                }
            }
            String title = doc.get("title");
            String content = StringEscapeUtils.escapeHtml(doc.get("content"));
            if (title != null) {
                TokenStream tokenStream = analyzer.tokenStream("title", new StringReader(title));
                String hTitle = highlighter.getBestFragment(tokenStream, title);
                if (StringUtil.isEmpty(hTitle)) {
                    blog.setTitle(title);
                } else {
                    blog.setTitle(hTitle);
                }
            }
            if (content != null) {
                content = StringUtil.replaceStr(content);
                TokenStream tokenStream = analyzer.tokenStream("content", new StringReader(content));
                String hContent = highlighter.getBestFragment(tokenStream, content);
                if (StringUtil.isEmpty(hContent)) {
                    if (content.length() <= 500) {
                        blog.setContent(content);
                    } else {
                        blog.setContent(content.substring(0, 500));
                    }
                } else {
                    blog.setContent(hContent);
                }
            }
            blogList.add(blog);
        }
        return blogList;
    }
}
