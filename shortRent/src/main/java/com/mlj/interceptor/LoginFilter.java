package com.mlj.interceptor;

import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class LoginFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=========进入过滤器");
        // 不过滤的uri
        String[] notFilter = new String[]{"login.jsp", "index.jsp", "adminLogin.jsp",
                "register.jsp", "totleHouse.jsp", "totleUser.jsp", "uncheck.jsp", "user.jsp"};

        // 请求的uri
        String uri = request.getRequestURI();
        System.out.println(uri + "==============");
        boolean doFilter = true;
        for (String s : notFilter) {
            if (uri.indexOf(s) != -1) {
                // 如果uri中包含不过滤的uri，则不进行过滤
                doFilter = false;
                break;
            }
        }

        if (doFilter) {
            //执行过滤
            //从session中获取登录者实体
            Object obj = request.getSession().getAttribute("user");
            if (null == obj) {
                // 如果session中不存在登录者实体，则弹出框提示重新登录
                // 设置request和response的字符集，防止乱码
                response.setCharacterEncoding("UTF-8");
                request.setCharacterEncoding("UTF-8");
                response.setContentType("text/html; charset=UTF-8");
                response.setHeader("Content-Type", "text/html; charset=UTF-8");
                PrintWriter out = response.getWriter();
                String loginPage = "http://localhost:8080/sr/jsp/login.jsp";
                StringBuilder builder = new StringBuilder();
                builder.append("<script type=\"text/javascript\">");
                builder.append("alert('请先进行登录');");
                builder.append("window.top.location.href='");
                builder.append(loginPage);
                builder.append("';");
                builder.append("</script>");
                out.print(builder.toString());
            } else {
                // 如果session中存在登录者实体，则继续
                filterChain.doFilter(request, response);
            }
        } else {
            // 如果不执行过滤，则继续
            filterChain.doFilter(request, response);
        }

    }
}
