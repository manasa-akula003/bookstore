package com.books;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

@SuppressWarnings("serial")
@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private static final String QUERY =
        "INSERT INTO USERDATA(SNO,USERNAME,EMAIL,PASSWORD) VALUES(?,?,?,?)";

    /** Handle POST – this is what fetch() calls */
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");
        PrintWriter pw = res.getWriter();

        // 1. Read form parameters
        String sNo       = req.getParameter("sNo");
        String userName  = req.getParameter("userName");
        String email     = req.getParameter("email");
        String password  = req.getParameter("password");

        // 2. Basic validation (optional but recommended)
        if (userName == null || email == null || password == null) {
            pw.print("{\"status\":\"error\",\"message\":\"Missing required fields.\"}");
            return;
        }

        try { Class.forName("com.mysql.cj.jdbc.Driver"); }
        catch (ClassNotFoundException e) {
            pw.print("{\"status\":\"error\",\"message\":\"JDBC driver not found.\"}");
            return;
        }

        try (Connection con = DriverManager.getConnection(
                 "jdbc:mysql:///book", "root", "root");
             PreparedStatement ps = con.prepareStatement(QUERY)) {

            ps.setString(1, sNo);
            ps.setString(2, userName);
            ps.setString(3, email);
            ps.setString(4, password);

            int count = ps.executeUpdate();
            if (count == 1) {
                pw.print("{\"status\":\"success\",\"message\":\"Registration successful!\"}");
            } else {
                pw.print("{\"status\":\"error\",\"message\":\"Registration failed.\"}");
            }

        } catch (SQLException se) {
            pw.print("{\"status\":\"error\",\"message\":\"" + se.getMessage() + "\"}");
        }
    }

    /** Optional: make GET just delegate or reject */
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse res) throws ServletException, IOException {
        // Either forward to doPost:
        // doPost(req, res);

        // …or return 405 Method Not Allowed:
        res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED,
                      "GET not supported for /register");
    }
}
