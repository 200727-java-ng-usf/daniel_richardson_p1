package com.revature.ers.services;

import javax.servlet.http.HttpServletRequest;

public class RequestViewHelper {

    public String process(HttpServletRequest req) {

        switch (req.getRequestURI()) {

            case "/revabooks/login.view":
                return "partials/login.html";

            case "/revabooks/register.view":
                return "partials/register.html";

            default:
                return null;

        }

    }
}
