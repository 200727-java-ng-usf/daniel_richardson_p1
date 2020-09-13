package com.revature.ers.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.revature.ers.dtos.ErrorResponse;
import com.revature.ers.dtos.Principal;
import com.revature.ers.exceptions.InvalidRequestException;
import com.revature.ers.exceptions.ResourceNotFoundException;
import com.revature.ers.models.AppUser;
import com.revature.ers.models.Ticket;
import com.revature.ers.services.TicketService;
import com.revature.ers.services.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@WebServlet("/app/tickets/*")
public class TicketServlet extends HttpServlet {

    private final TicketService ticketService = new TicketService();

    /**
     * Gets tickets
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Tickets GET invoked!");

        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();
        resp.setContentType("application/json");

        //principal from session data made from user object data
        String principalJSON = (String) req.getSession().getAttribute("principal");
        System.out.println(principalJSON);

        //if someone cheated to get here, send 401
        if (principalJSON == null) {
            ErrorResponse err = new ErrorResponse(401, "No principal object found on request.");
            respWriter.write(mapper.writeValueAsString(err));
            resp.setStatus(401); // 401 = UNAUTHORIZED
            return; // necessary so that we do not continue with the rest of this method's logic
        }

        Principal principal = mapper.readValue(principalJSON, Principal.class);

        //if its not an admin OR manager, tell them no
        //employees can only get their own tickets; we'll use something else for that
        if (principal.getRole() == 3) {
            ErrorResponse err = new ErrorResponse(403, "Forbidden: Your role does not permit you to access this endpoint.");
            respWriter.write(mapper.writeValueAsString(err));
            resp.setStatus(403); // 403 = FORBIDDEN
            return;
        }

        try {
                Set<Ticket> tickets = ticketService.getAllTickets(); //get the tickets, adds to set
                String ticketsJSON = mapper.writeValueAsString(tickets); //packaged into json
                System.out.println(ticketsJSON); //breadcrumbs
                respWriter.write(ticketsJSON); //kobe
                resp.setStatus(200); // confirmed

        } catch (ResourceNotFoundException rnfe) {
            resp.setStatus(404);
            ErrorResponse err = new ErrorResponse(404, rnfe.getMessage());
            respWriter.write(mapper.writeValueAsString(err));
        } catch (NumberFormatException | InvalidRequestException e) {
            resp.setStatus(400);
            ErrorResponse err = new ErrorResponse(400, "Something happened.");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);
        }  catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "Mistakes were made.");
            respWriter.write(mapper.writeValueAsString(err));
        }
    }
}

