package com.books;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        String userName = req.getParameter("userName");
        String password = req.getParameter("password");

        PrintWriter out = res.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql:///book", "root", "root");

            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM USERDATA WHERE USERNAME=? AND PASSWORD=?"
            );
            ps.setString(1, userName);
            ps.setString(2, password);

            
            System.out.println("Received username: " + userName);
            System.out.println("Received password: " + password);

            ResultSet rs = ps.executeQuery();
            

            if (rs.next()) {
                out.print("{\"status\":\"success\",\"message\":\"Login successful!\"}");
            } else {
                out.print("{\"status\":\"error\",\"message\":\"Invalid credentials\"}");
            }

        } catch (Exception e) {
            out.print("{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}");
        }
    }
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res) throws ServletException, IOException {
        // Either forward to doPost:
        // doPost(req, res);

        // …or return 405 Method Not Allowed:
     
    }
}

