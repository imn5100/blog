package com.shaw.test;

import com.shaw.bo.Blog;
import com.shaw.bo.Link;
import com.shaw.util.CodecUtils;
import com.shaw.util.DesUtils;
import com.shaw.util.EmailUtils;
import com.shaw.util.PropertiesUtil;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    public static void main(String[] args) throws Exception {
//        String encrpt = DesUtils.getDefaultInstance().encrypt("112");
//        System.out.println(encrpt);
//        System.out.println(DesUtils.getDefaultInstance().decrypt(encrpt));

        // 创建一个ExpressionParser对象，用于解析表达式
        ExpressionParser parser = new SpelExpressionParser();
        List<Blog> list = new ArrayList<Blog>();
        Blog b1 = new Blog();
        b1.setId(1);
        Blog b2 = new Blog();
        b2.setId(2);
        Blog b3 = new Blog();
        b3.setId(3);
        list.add(b1);
        list.add(b2);
        list.add(b3);
        // 创建一个EvaluationContext对象，作为SpEL解析变量的上下文
        EvaluationContext ctx = new StandardEvaluationContext();
        // 设置变量
        ctx.setVariable("blogList", list);
        //------------在SpEL中对集合进行投影-----------
        //将每个集合元素进行截取，组成新的集合
        Expression expr = parser.parseExpression("#blogList.!['Blog_Search_'+#this.getId()]");
        String[] keyList = expr.getValue(ctx, String[].class);
        System.out.println("投影后的新集合为：" + Arrays.toString(keyList));


//        List<Link> list2 = new ArrayList<Link>();
//        list2.add(new Link());
//        list2.add(new Link());
//        ctx.setVariable("mylist2", list2);
//        expr = parser.parseExpression("#mylist2");
//        System.out.println("投影前的集合为：" + expr.getValue(ctx));
//        投影条件是 只要name属性
//        expr = parser.parseExpression("#mylist2.![linkName]");
//        System.out.println("投影后的新集合为" + expr.getValue(ctx));


    }

    public static void simpleDecode() throws Exception {
        String encodeStr = CodecUtils.getEncodeId(1154);
        System.out.println(encodeStr);
        int decodeId = CodecUtils.getDecodeId(encodeStr);
        System.out.println(decodeId);
    }

    public static void sendEmail() {
        try {
            DesUtils.getDefaultInstance().decrypt("OOOOOOO");
        } catch (Exception ex) {
            String[] emails = PropertiesUtil.getConfiguration().getStringArray("email.subscriber");
            System.out.println(emails);
            System.out.println(EmailUtils.sendEmail("Blog system exception", ExceptionUtils.getFullStackTrace(ex), emails));
        }


    }

}

