package com.shaw.util;

/**
 * 分页工具类
 *
 * @author Administrator
 */
public class PageUtil {

    /**
     * 生成分页代码
     *
     * @param targetUrl   目标地址
     * @param totalNum    总记录数
     * @param currentPage 当前页
     * @param pageSize    每页大小
     * @return
     */
    public static String genPagination(String targetUrl, long totalNum, int currentPage, int pageSize, String param) {
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        if (totalPage == 0) {
            return "未查询到数据";
        } else {
            StringBuffer pageCode = new StringBuffer();
            if (StringUtil.isNotEmpty(param)) {
                param = "&" + param;
            }
            pageCode.append("<li class='page-item'><a class='page-link' href='" + targetUrl + "?page=1" + param + "'>首页</a></li>");
            if (currentPage > 1) {
                pageCode.append("<li class='page-item'><a  class='page-link' href='" + targetUrl + "?page=" + (currentPage - 1) + param + "'>上一页</a></li>");
            } else {
                pageCode.append("<li class='page-item disabled'><a class='page-link' href='#'>上一页</a></li>");
            }
            for (int i = currentPage - 2; i <= currentPage + 2; i++) {
                if (i < 1 || i > totalPage) {
                    continue;
                }
                if (i == currentPage) {
                    pageCode.append("<li class='page-item active'><a class='page-link' href='" + targetUrl + "?page=" + i + param + "'>" + i + "</a></li>");
                } else {
                    pageCode.append("<li class='page-item' ><a  class='page-link' href='" + targetUrl + "?page=" + i + param + "'>" + i + "</a></li>");
                }
            }
            if (currentPage < totalPage) {
                pageCode.append("<li class='page-item'><a  class='page-link' href='" + targetUrl + "?page=" + (currentPage + 1) + param + "'>下一页</a></li>");
            } else {
                pageCode.append("<li class='page-item disabled'><a  class='page-link' href='#'>下一页</a></li>");
            }
            pageCode.append("<li class='page-item'><a class='page-link' href='" + targetUrl + "?page=" + totalPage + param + "'>尾页</a></li>");
            return pageCode.toString();
        }
    }

    @Deprecated
    public static String genPaginationOld(String targetUrl, long totalNum, int currentPage, int pageSize, String param) {
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        if (totalPage == 0) {
            return "未查询到数据";
        } else {
            StringBuffer pageCode = new StringBuffer();
            pageCode.append("<li><a href='" + targetUrl + "?page=1&" + param + "'>首页</a></li>");
            if (currentPage > 1) {
                pageCode.append("<li><a href='" + targetUrl + "?page=" + (currentPage - 1) + "&" + param + "'>上一页</a></li>");
            } else {
                pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
            }
            for (int i = currentPage - 2; i <= currentPage + 2; i++) {
                if (i < 1 || i > totalPage) {
                    continue;
                }
                if (i == currentPage) {
                    pageCode.append("<li class='active'><a href='" + targetUrl + "?page=" + i + "&" + param + "'>" + i + "</a></li>");
                } else {
                    pageCode.append("<li><a href='" + targetUrl + "?page=" + i + "&" + param + "'>" + i + "</a></li>");
                }
            }
            if (currentPage < totalPage) {
                pageCode.append("<li><a href='" + targetUrl + "?page=" + (currentPage + 1) + "&" + param + "'>下一页</a></li>");
            } else {
                pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
            }
            pageCode.append("<li><a href='" + targetUrl + "?page=" + totalPage + "&" + param + "'>尾页</a></li>");
            return pageCode.toString();
        }
    }

    /**
     * 构建分页标签
     */
    public static String genUpAndDownPageCode(Integer page, Integer totalNum, String q, Integer pageSize,
                                              String projectContext) {
        long totalPage = totalNum % pageSize == 0 ? totalNum / pageSize : totalNum / pageSize + 1;
        StringBuffer pageCode = new StringBuffer();
        if (totalPage == 0) {
            return "";
        } else {
            pageCode.append("<nav>");
            pageCode.append("<ul class='pager' >");
            if (page > 1) {
                pageCode.append("<li><a href='" + projectContext + "/blog/search.html?page=" + (page - 1) + "&keyword=" + q
                        + "'>上一页</a></li>");
            } else {
                pageCode.append("<li class='disabled'><a href='#'>上一页</a></li>");
            }
            if (page < totalPage) {
                pageCode.append("<li><a href='" + projectContext + "/blog/search.html?page=" + (page + 1) + "&keyword=" + q
                        + "'>下一页</a></li>");
            } else {
                pageCode.append("<li class='disabled'><a href='#'>下一页</a></li>");
            }
            pageCode.append("</ul>");
            pageCode.append("</nav>");
        }
        return pageCode.toString();
    }


}
