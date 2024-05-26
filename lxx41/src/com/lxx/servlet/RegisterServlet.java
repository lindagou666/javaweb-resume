package com.lxx.servlet;

import com.lxx.util.JDBCUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        JDBCUtils jdbcUtils = new JDBCUtils();
        Connection connection = JDBCUtils.getConnection();
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String phone = req.getParameter("phone");
        String age = req.getParameter("age");
        String hobby = req.getParameter("hobby");
        boolean rememberMe="on".equals(req.getParameter("rememberMe"));

        String checkUserSql = "SELECT COUNT(*) FROM user_info WHERE username = ?";
        String checkPhoneSql = "SELECT COUNT(*) FROM user_info WHERE phone = ?";
        try {
            PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql);
            checkUserStmt.setString(1, username);
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                // 用户名已存在
                resp.getWriter().write("<script>alert('用户名已存在!');history.back();</script>");
                return;
            }
            PreparedStatement checkPhoneStmt = connection.prepareStatement(checkPhoneSql);
            checkPhoneStmt.setString(1, phone);
            ResultSet rs2 = checkPhoneStmt.executeQuery();
            if (rs2.next() && rs2.getInt(1) > 0) {
                // 手机号已存在
                resp.getWriter().write("<script>alert('手机号已存在!');history.back();</script>");
                return;
            }

            String sql = "INSERT INTO user_info (username, password, phone, age, hobby) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, phone);
            if (age != null && !age.isEmpty()) {
                ps.setString(4, age);
            } else {
                ps.setNull(4, java.sql.Types.INTEGER);
            }
            if (hobby != null && !hobby.isEmpty()) {
                ps.setString(5, hobby);
            } else {
                ps.setNull(5, java.sql.Types.VARCHAR);
            }
            int i = ps.executeUpdate();
            if (i > 0) {
                req.setAttribute("msg","注册成功！将跳转到登录页面！");
                resp.sendRedirect("login.html");
                if(rememberMe){
                    Cookie usernameCookie = new Cookie("username", username);
                    Cookie passwordCookie = new Cookie("password", password);
                    resp.addCookie(usernameCookie);
                    resp.addCookie(passwordCookie);
                }
                else{
                    Cookie[] cookies = req.getCookies();
                    if (cookies != null)
                    {
                        for (Cookie cookie : cookies){
                            if ("username".equals(cookie.getName()) || "password".equals(cookie.getName())){
                                cookie.setMaxAge(0);
                            }
                            resp.addCookie(cookie);
                        }
                    }
                }
            } else {
                resp.getWriter().write("<script>alert('注册失败!');history.back();</script>");
            }

        } catch (SQLException e) {
            resp.getWriter().write("<script>alert('注册失败!');history.back();</script>");
            throw new RuntimeException(e);
        }
    }
}



