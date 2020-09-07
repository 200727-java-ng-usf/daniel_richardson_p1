package com.revature.ers.servlets;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JSONHelper {


    public static void process(HttpServletRequest req, HttpServletResponse res)
            throws JsonProcessingException, IOException {

        System.out.println(req.getRequestURI());

        //prune ending of URI string

        switch(req.getRequestURI()) {
            case "/daniel_richardson_p1/json/adduser":
//                DannyBoyController.dannyFinder(req,res);
                break;
            case "/daniel_richardson_p1/json/finduser":
//                MarsController.marsFinder(req,res);
                break;
            case "/daniel_richardson_p1/json/updateuser":
//                MarsController.marsFinder(req,res);
                break;
            case "/daniel_richardson_p1/json/deleteuser":
//                MarsController.marsFinder(req,res);
                break;
            default:
//                SuperVillain nullVill = new SuperVillain(null, null, 0);
//                res.getWriter().write(new ObjectMapper().writeValueAsString(nullVill));

        }
    }
}
