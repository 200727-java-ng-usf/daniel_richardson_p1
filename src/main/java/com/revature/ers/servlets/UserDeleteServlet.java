package com.revature.ers.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.ers.dtos.ErrorResponse;
import com.revature.ers.dtos.Principal;
import com.revature.ers.exceptions.InvalidRequestException;
import com.revature.ers.exceptions.ResourceNotFoundException;
import com.revature.ers.models.AppUser;
import com.revature.ers.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet("/app/delete/user/*")
public class UserDeleteServlet extends HttpServlet {

    private final UserService userService = new UserService();
//
//
//    /**
//     * Delete, POST only
//     * deletes a user based on email address
//     *
//     * @param req
//     * @param resp
//     * @throws ServletException
//     * @throws IOException
//     */
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        resp.setContentType("application/json");
//
//        ObjectMapper mapper = new ObjectMapper();
//        PrintWriter respWriter = resp.getWriter();
//
//        try {
//            AppUser targetUser = mapper.readValue(req.getInputStream(), AppUser.class);
//            System.out.println(targetUser.toString());
//            userService.deleteUserByEmail(targetUser);
//            resp.setStatus(204); // 204 = confirmed
//
//        } catch (Exception e) {
//
//            e.printStackTrace();
//            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
//            ErrorResponse err = new ErrorResponse(500, "Mistakes were made.");
//            respWriter.write(mapper.writeValueAsString(err));
//
//        }
//
//    }
}
