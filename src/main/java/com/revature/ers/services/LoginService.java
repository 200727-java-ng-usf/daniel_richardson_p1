//package com.revature.ers.services;
//
//import javax.servlet.http.HttpServletRequest;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//
////import com.revature.ers.models.AppUser;
//
//
///**
// * Handles User Login Authentication
// * Requires ConnectionService, ConsoleService
// * Will send to dashboard
// */
//public class LoginService{
//    //eager singleton
//    private static LoginService loginService = new LoginService();
//
//    private LoginService() {
//        super();
//    }
//
//    public static LoginService getInstance() {
//        return loginService;
//    }
//    @Override
//    protected Object clone() throws CloneNotSupportedException {
//        throw new CloneNotSupportedException();
//    }
//
//    /**
//     *
//     * @param username Should equal db email
//     * @param password on the app_user table
//     * @param req for adding data to session: role
//     * @return Returns user object with updated fields
//     *
//     */
//
//    public boolean login(String username, String password, int reqRole, HttpServletRequest req){
//
//        try (Connection conn = ConnectionService.getInstance().getConnection()) {
//            String sql = "SELECT * FROM project1.ers_users au " +
//                    "WHERE username = ? AND password = ?";
//
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, username);
//            pstmt.setString(2, password);
//
//            ResultSet rs = pstmt.executeQuery();
//            if(rs.next()){
//                //first to check if anything came back, if not, failed login
//
//                //now check to verify role w/requested login type
//                //admin 1, manager 2, employee 3
//                int dbRole = rs.getInt("user_role_id");
//
//                //if dbRole==1, admin does whatever they want
//                if(dbRole == 2 && reqRole == 1){
//                    return false; //manager can't login as admin
//                } else if(dbRole == 3){
//                    if(reqRole==1 || reqRole==2){
//                        return false; //employee can't login as manager or admin
//                    }
//                }
//
//                //add some data to session
//                req.getSession().setAttribute("username", rs.getString("username"));
//                req.getSession().setAttribute("role", dbRole);
//
//                System.out.println("USER SESSION DATA: ");
//                System.out.println(req.getSession().getAttribute("username"));
//                System.out.println(req.getSession().getAttribute("role"));
//                return true;
//            } else {
//                return false;
//            }
//        } catch (SQLException sqle) {
//            sqle.printStackTrace();
//        }
//        return false;
//    }
//}
