package com.revature.ers.servlets;

import com.revature.ers.controllers.LoginController;
import com.revature.ers.services.UserAccountService;

import javax.servlet.http.HttpServletRequest;

public class Helper {

    //acts as an html router
    public static String process(HttpServletRequest req) {

        System.out.println("THIS is the current URI active: " + req.getRequestURI());

        switch(req.getRequestURI()) {
            //====login handling=================================\\
            case "/daniel_richardson_p1/ers/login":
                System.out.println("in login case");
                return LoginController.login(req);

            case "/daniel_richardson_p1/ers/loginretry":
                System.out.println("in loginretry case");
                return "/html/loginretry.html";

            //====dashboard handling==============================\\
            case "/daniel_richardson_p1/ers/dashboard1":
                return "/html/dashboard1.html";

            //====admin handling==============================\\
            case "/daniel_richardson_p1/ers/adduser":
                UserAccountService.addUser(req);
                //returns back to admin dashboard
                return "/html/dashboard1.html";


            //====home handling ==================================\\
            case "/daniel_richardson_p1/ers/home":
                System.out.println("in home case");
                return "/html/index.html";

            default:
                System.out.println("in default");
                return "/html/index.html";
        }

    }
}
