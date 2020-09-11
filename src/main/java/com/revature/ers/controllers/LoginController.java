package com.revature.ers.controllers;

import com.revature.ers.services.LoginService;

import javax.servlet.http.HttpServletRequest;

/*
 * The purpose:
 *
 * Controllers handle the business logic of an endpoint
 */
public class LoginController {

//    public static String login(HttpServletRequest req) {
//
//        //ensure that the method is a POST http method, else send them back to the login page
//        if(!req.getMethod().equals("POST")) {
//            return "/ers/home";
//        }
//
//        //acquire the form data
//        String username = req.getParameter("username");
//        String password = req.getParameter("password");
//        int role = Integer.parseInt(req.getParameter("requestedrole"));
//
//        if(!LoginService.getInstance().login(username, password, role, req)) {
//            //attempts login, returns boolean, adds data to session
//            System.out.println("LOG - FAILED LOGIN");
//            return "/ers/loginretry";
//        }else {
//            System.out.println("LOG - SUCCESSFUL LOGIN");
//            switch (role){
//                case 1:
//                    return "/ers/dashboard1";
//                case 2:
//                    return "/ers/dashboard2";
//                case 3:
//                    return "/ers/dashboard3";
//                default:
//                    return "/ers/home";
//            }
//
//        }

//    }

}
