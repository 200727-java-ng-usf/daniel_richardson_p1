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



@WebServlet("/app/update/user/*")
public class UserUpdateServlet extends HttpServlet {

    private final UserService userService = new UserService();

//
//    /**
//     * Update, POST only
//     * updates a user based on email address
//     *
//     * @param req
//     * @param resp
//     * @throws ServletException
//     * @throws IOException
//     */
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//
//        resp.setContentType("application/json");
//
//        ObjectMapper mapper = new ObjectMapper();
//        PrintWriter respWriter = resp.getWriter();
//
//        try {
//            AppUser fixedUser = mapper.readValue(req.getInputStream(), AppUser.class);
//            System.out.println(fixedUser.toString());
//            userService.update(fixedUser);
//            resp.setStatus(204); // 204 = confirmed
//
//        } catch (MismatchedInputException mie) {
//
//            resp.setStatus(400); // 400 = BAD REQUEST
//            ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed user object found in request body");
//            String errJSON = mapper.writeValueAsString(err);
//            respWriter.write(errJSON);
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
