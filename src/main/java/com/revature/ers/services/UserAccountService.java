package com.revature.ers.services;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserAccountService {


    public static boolean addUser(HttpServletRequest req){

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String fname = req.getParameter("fname");
        String lname = req.getParameter("lname");
        String email = req.getParameter("email");
        int role = Integer.parseInt(req.getParameter("role"));
        //could check here for nulls, etc, but might as well let the sql query return a fail

        try (Connection conn = ConnectionService.getInstance().getConnection()) {
            String sql = "INSERT INTO project0.app_users (email, password) " +
                    "VALUES (?, ?)";

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.executeUpdate();


        } catch (SQLException sqle) {
            sqle.printStackTrace();
            System.err.println("Something went wrong.");
        }

    }

}
