//package com.shaw.controller.admin;
//
//import com.alibaba.fastjson.JSONObject;
//import com.shaw.entity.Comment;
//import com.shaw.service.CommentService;
//import com.shaw.util.PageBean;
//import com.shaw.util.ResponseUtil;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletResponse;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
/**
*
* 使用多说后不需要评论维护
* **/
//@Controller
//@RequestMapping("/admin/comment")
//public class CommentAdminController {
//
//	@Resource
//	private CommentService commentService;
//
//	/**
//	 * 分页查询评论信息
//	 * @param page
//	 * @param rows
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/list")
//	public String list(@RequestParam(value="page",required=false)String page,@RequestParam(value="rows",required=false)String rows,@RequestParam(value="state",required=false)String state,HttpServletResponse response)throws Exception{
//		PageBean pageBean=new PageBean(Integer.parseInt(page),Integer.parseInt(rows));
//		Map<String,Object> map=new HashMap<String,Object>();
//		map.put("start", pageBean.getStart());
//		map.put("size", pageBean.getPageSize());
//		map.put("state", state); // 评论状态
//		List<Comment> commentList=commentService.list(map);
//		Long total=commentService.getTotal(map);
//		JSONObject result=new JSONObject();
//		result.put("rows", commentList);
//		result.put("total", total);
//		ResponseUtil.write(response, result);
//		return null;
//	}
//
//	/**
//	 * 删除评论信息
//	 * @param ids
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/delete")
//	public String delete(@RequestParam(value="ids")String ids,HttpServletResponse response)throws Exception{
//		String []idsStr=ids.split(",");
//		for(int i=0;i<idsStr.length;i++){
//			commentService.delete(Integer.parseInt(idsStr[i]));
//		}
//		JSONObject result=new JSONObject();
//		result.put("success", true);
//		ResponseUtil.write(response, result);
//		return null;
//	}
//
//	/**
//	 * 评论审核
//	 * @param ids
//	 * @param response
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping("/review")
//	public String review(@RequestParam(value="ids")String ids,@RequestParam(value="state")Integer state,HttpServletResponse response)throws Exception{
//		String []idsStr=ids.split(",");
//		for(int i=0;i<idsStr.length;i++){
//			Comment comment=new Comment();
//			comment.setState(state);
//			comment.setId(Integer.parseInt(idsStr[i]));
//			commentService.update(comment);
//		}
//		JSONObject result=new JSONObject();
//		result.put("success", true);
//		ResponseUtil.write(response, result);
//		return null;
//	}
//}
