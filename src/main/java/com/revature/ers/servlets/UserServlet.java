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


/** USER SERVLET:        /app/users/*
 * DoGet: Grabs all users, or just by ID
 * DoPut: Updates a user's info
 * DoPost: Creates a new user
 * DoDelete: Deletes a user
 */
@WebServlet("/app/users/*")
public class UserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    /**
     * Gets user by id, or all users if no id is provided
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");
        //principal from session data made from user object data
        String principalJSON = (String) req.getSession().getAttribute("principal");
        //if someone cheated to get here, send 401
        if (principalJSON == null) {
            ErrorResponse err = new ErrorResponse(401, "No principal object found on request.");
            respWriter.write(mapper.writeValueAsString(err));
            resp.setStatus(401); // 401 = UNAUTHORIZED
            return; // necessary so that we do not continue with the rest of this method's logic
        }
        Principal principal = mapper.readValue(principalJSON, Principal.class);
        //double check and make sure the role is valid
        if (principal.getRole() < 1 || principal.getRole() > 3) {
            ErrorResponse err = new ErrorResponse(400, "Malformed role ID.");
            respWriter.write(mapper.writeValueAsString(err));
            resp.setStatus(400); // 403 = FORBIDDEN
            return;
        }
        //if its not an admin, tell them no
        if (principal.getRole() != 1) {
            ErrorResponse err = new ErrorResponse(403, "Forbidden: Your role does not permit you to access this endpoint.");
            respWriter.write(mapper.writeValueAsString(err));
            resp.setStatus(403); // 403 = FORBIDDEN
            return;
        }

        try {
            String idParam = req.getParameter("id");
            if (idParam != null) { //if they gave us an id, we get data just from that id
                int id = Integer.parseInt(idParam);
                AppUser user = userService.getUserById(id);
                String userJSON = mapper.writeValueAsString(user);
                respWriter.write(userJSON);

            } else {    //if we got no id, we just throw all data at them. they asked for it.
                Set<AppUser> users = userService.getAllUsers(); //get the users, adds to set
                String usersJSON = mapper.writeValueAsString(users); //packages into json
                System.out.println(usersJSON); //breadcrumbs
                respWriter.write(usersJSON); //kobe
                resp.setStatus(200); // not required, 200 by default so long as no exceptions/errors are thrown
            }
        } catch (ResourceNotFoundException rnfe) {
            resp.setStatus(404);
            ErrorResponse err = new ErrorResponse(404, rnfe.getMessage());
            respWriter.write(mapper.writeValueAsString(err));
        } catch (NumberFormatException | InvalidRequestException e) {
            resp.setStatus(400);
            ErrorResponse err = new ErrorResponse(400, "Malformed user id parameter value provided.");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);
        }  catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "Mistakes were made.");
            respWriter.write(mapper.writeValueAsString(err));
        }
    }

    /**
     * Delete
     * deletes a user based on email address
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();
        try {
            AppUser targetUser = mapper.readValue(req.getInputStream(), AppUser.class);
            System.out.println(targetUser.toString());
            userService.deleteUserByEmail(targetUser);
            resp.setStatus(204); // 204 = confirmed
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "Mistakes were made.");
            respWriter.write(mapper.writeValueAsString(err));
        }
    }

    /**
     * Update, doPut
     * updates a user based on email address
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();
        try {
            AppUser fixedUser = mapper.readValue(req.getInputStream(), AppUser.class);
            fixedUser.setHashedPassword(fixedUser.getPassword());
            userService.update(fixedUser);
            resp.setStatus(204); // 204 = confirmed
        } catch (MismatchedInputException mie) {
            resp.setStatus(400); // 400 = BAD REQUEST
            ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed user object found in request body");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "Mistakes were made.");
            respWriter.write(mapper.writeValueAsString(err));
        }
    }

    /**
     * Registration
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();

        try {
            //maps received input into a new user, sends to registration
            AppUser newUser = mapper.readValue(req.getInputStream(), AppUser.class);
            //set the new user's password to the hashed version
            newUser.setHashedPassword(newUser.getPassword());
            System.out.println(newUser);
            userService.register(newUser);
            String newUserJSON = mapper.writeValueAsString(newUser);
            respWriter.write(newUserJSON); //
            resp.setStatus(201); // 201 = CREATED

        } catch (MismatchedInputException mie) {

            resp.setStatus(400); // 400 = BAD REQUEST
            ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed user object found in request body");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);

        } catch (Exception e) {

            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "Mistakes were made.");
            respWriter.write(mapper.writeValueAsString(err));

        }

    }
}
