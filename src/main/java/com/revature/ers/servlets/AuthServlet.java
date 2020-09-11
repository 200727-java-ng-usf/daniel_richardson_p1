package com.revature.ers.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.ers.dtos.Credentials;
import com.revature.ers.dtos.ErrorResponse;
import com.revature.ers.dtos.Principal;
import com.revature.ers.exceptions.AuthenticationException;
import com.revature.ers.exceptions.InvalidRequestException;
import com.revature.ers.models.AppUser;
import com.revature.ers.services.UserService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


/**
 * doGet: User Logout
 *  --invalidates session
 * doPost: User Authentication
 *  --gets JSON
 *  --
 */

@WebServlet("/app/auth")
public class AuthServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override   //logout method
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().invalidate();
        resp.setStatus(204); //204 = confirmed
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("AUTH invoked");
        ObjectMapper mapper = new ObjectMapper(); //from jackson for json stuff
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        try { // User Jackson to read the request body and map the provided JSON to a Java POJO

            //grabs request json, puts it into a credentials object that we then send to userService authentication
            Credentials creds = mapper.readValue(req.getInputStream(), Credentials.class);
            System.out.println(creds.toString());

            //authenticating
            AppUser authUser = userService.authenticate(creds.getUsername(), creds.getPassword(), creds.getRole());

            Principal principal = new Principal(authUser); //now assigning data to principal object
            //principal:
            // -user object
            // -ID, username, role
            HttpSession session = req.getSession();
            session.setAttribute("principal", principal.stringify()); //adding principal to session data

            resp.setStatus(204); // 204 = NO CONTENT
            //========instead forwarding depending on role id and role selected
//            resp.sendRedirect("app/admin.html");

        } catch (MismatchedInputException | InvalidRequestException e) {

            resp.setStatus(400);
            ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed credentials object found in request body");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);

        } catch (AuthenticationException ae) {
            //wraps up the authentication error msg into the response Json, mails it back

            resp.setStatus(401);
            ErrorResponse err = new ErrorResponse(401, ae.getMessage());
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);

        } catch (Exception e) {
            //generic error message

            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "Mistakes were made.");
            respWriter.write(mapper.writeValueAsString(err));

        }

    }
}
