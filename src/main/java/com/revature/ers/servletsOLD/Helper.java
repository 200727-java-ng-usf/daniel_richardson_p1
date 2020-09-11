package com.revature.ers.servletsOLD;

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
            case "/daniel_richardson_p1/html/dashboard1.html":
                //todo just add authentication class for direct page accessing

                if(req.getSession().getAttribute("role").equals("1")){
                    //equals check works
                    //servlet just isn't catching html route (not in xml),
                    //if all htmls are caught, will be put in endless loop
                    //can only do /html/dashboard/*, or restructure pages into new directories
                    System.out.println("USER ATTEMPTED ADMIN PAGE WITH INCORRECT ROLE: " + req.getSession().getAttribute("role"));
                    return "/html/loginretry.html";
                }
                System.out.println(req.getSession().getAttribute("role"));
                return "/html/dashboard1.html";

            //====admin handling==============================\\
            case "/daniel_richardson_p1/ers/adduser":
                if(UserAccountService.addUser(req)){
                    return "/html/dashboard1.html";
                }
                //todo if else leads to diff html pages
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
