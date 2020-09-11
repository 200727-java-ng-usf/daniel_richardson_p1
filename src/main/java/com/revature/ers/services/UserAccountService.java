package com.revature.ers.services;
//
//import javax.servlet.http.HttpServletRequest;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
public class UserAccountService {}
//
//
//    public static boolean addUser(HttpServletRequest req){
//
//        String username = req.getParameter("username");
//        String password = req.getParameter("password");
//        String fname = req.getParameter("fname");
//        String lname = req.getParameter("lname");
//        String email = req.getParameter("email");
//        int role = Integer.parseInt(req.getParameter("role"));
//        //could check here for nulls, etc, but might as well let the sql query return a fail
//
//        try (Connection conn = ConnectionService.getInstance().getConnection()) {
//            String sql = "INSERT INTO project1.ers_users (username, password, first_name, last_name, email, user_role_id) " +
//                    "VALUES (?, ?, ?, ?, ?, ?)";
//
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, username);
//            pstmt.setString(2, password);
//            pstmt.setString(3, fname);
//            pstmt.setString(4, lname);
//            pstmt.setString(5, email);
//            pstmt.setInt(6, role);
//            pstmt.executeUpdate();
//
//            return true;
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//            return false;
//        }
//
//    }
//
//}
