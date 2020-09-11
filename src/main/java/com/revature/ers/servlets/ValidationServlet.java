package com.revature.ers.servlets;

import com.revature.ers.services.ValidationHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


//I don't think I'll handle form validation here.
//bootstrap ensures forms are valid, and if not, the sql constraints should prevent bad data
@WebServlet("*.validate")
public class ValidationServlet extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        boolean isValid = new ValidationHelper().process(req);
//
//        if (isValid) {
//            resp.setStatus(204);
//        } else {
//            resp.setStatus(409);
//        }
//
//    }
}
