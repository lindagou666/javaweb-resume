package com.lxx.servlet;

import com.lxx.util.JDBCUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/recover")
public class RecoverServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        resp.setContentType("text/html;charset=utf-8");
        JDBCUtils jdbcUtils = new JDBCUtils();
        Connection connection = JDBCUtils.getConnection();
        String username = req.getParameter("username");
        String phone = req.getParameter("phone");
        String password = req.getParameter("password");
        String checkUserSql = "SELECT COUNT(*) FROM user_info WHERE username = ?";
        String checkPhoneSql = "SELECT COUNT(*) FROM user_info WHERE phone = ?";
        try {
            PreparedStatement checkUserStmt = connection.prepareStatement(checkUserSql);
            checkUserStmt.setString(1, username);
            ResultSet rs = checkUserStmt.executeQuery();
            if (rs.next() && rs.getInt(1) == 0){
                resp.getWriter().write("<script>alert('用户名不存在!');history.back();</script>");
                return;
            }
            PreparedStatement checkPhoneStmt = connection.prepareStatement(checkPhoneSql);
            checkPhoneStmt.setString(1, phone);
            ResultSet rs2 = checkPhoneStmt.executeQuery();
            if (rs2.next() && rs2.getInt(1) == 0) {
                // 手机号不存在
                resp.getWriter().write("<script>alert('手机号不存在!');history.back();</script>");
                return;
            }
            String updatePasswordSql = "UPDATE user_info SET password = ? WHERE username = ? AND phone = ?";
            PreparedStatement updatePasswordStmt = connection.prepareStatement(updatePasswordSql);
            updatePasswordStmt.setString(1, password);
            updatePasswordStmt.setString(2, username);
            updatePasswordStmt.setString(3, phone);
            int rowsAffected = updatePasswordStmt.executeUpdate();
            if (rowsAffected > 0) {
                resp.getWriter().write("密码修改成功");
                resp.sendRedirect("login.html");//跳转到登录页面
                ·需要添加Session 自动填充登陆页面表单
            } else {
                resp.getWriter().write("密码修改失败");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
