package com.shaw.bo;

import com.alibaba.fastjson.annotation.JSONField;
import com.shaw.util.CodecUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author imn5100
 */
public class Blog implements Serializable {

    private static final long serialVersionUID = 4186161649311309864L;
    private Integer id;
    private String title;
    private String summary;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date releaseDate;
    private Integer clickHit;
    private Integer replyHit;
    private String content;
    private String contentNoTag;
    private BlogType blogType;
    private Integer blogCount;
    private String releaseDateStr;
    private String keyWord;
    private List<String> keywordList;
    private List<String> imagesList = new LinkedList<String>();

    private String encodeId;

    public List<String> getKeywordList() {
        if (StringUtils.isNotEmpty(keyWord) && keywordList == null) {
            keywordList = new ArrayList<String>();
            String[] keywords = keyWord.split(",");
            if (keywords.length > 0) {
                for (String keyword : keywords) {
                    keywordList.add(keyword);
                }
            }
        }
        return keywordList;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getClickHit() {
        return clickHit;
    }

    public void setClickHit(Integer clickHit) {
        this.clickHit = clickHit;
    }

    public Integer getReplyHit() {
        return replyHit;
    }

    public void setReplyHit(Integer replyHit) {
        this.replyHit = replyHit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentNoTag() {
        return contentNoTag;
    }

    public void setContentNoTag(String contentNoTag) {
        this.contentNoTag = contentNoTag;
    }

    public BlogType getBlogType() {
        return blogType;
    }

    public void setBlogType(BlogType blogType) {
        this.blogType = blogType;
    }

    public Integer getBlogCount() {
        return blogCount == null ? 0 : blogCount;
    }

    public void setBlogCount(Integer blogCount) {
        this.blogCount = blogCount;
    }

    public String getReleaseDateStr() {
        return releaseDateStr;
    }

    public void setReleaseDateStr(String releaseDateStr) {
        this.releaseDateStr = releaseDateStr;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public List<String> getImagesList() {
        return imagesList;
    }

    public void setImagesList(List<String> imagesList) {
        this.imagesList = imagesList;
    }

    public String getBlogTypeName() {
        return blogType == null ? null : blogType.getTypeName();
    }

    public String getEncodeId() throws Exception {
        return CodecUtils.getEncodeId(id);
    }

    public void setEncodeId(String encodeId) {
        this.encodeId = encodeId;
    }
}
