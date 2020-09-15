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


/** TICKET SERVLET  /app/tickets
 * doGet: gets all tickets or just the ones belonging to user (from principal data)
 * doPut: Updates a user's ticket (from employee form) OR resolves a ticket (from manager form)
 * doPost: creates a new ticket (from employee form)
 */

@WebServlet("/app/tickets")
public class TicketServlet extends HttpServlet {

    private final TicketService ticketService = new TicketService();

    /**
     * Gets all the tickets or just the ones by userID
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
        //double check and make sure the role is valid
        if (principal.getRole() < 1 || principal.getRole() > 3) {
            ErrorResponse err = new ErrorResponse(400, "Malformed role ID.");
            respWriter.write(mapper.writeValueAsString(err));
            resp.setStatus(400); // 403 = FORBIDDEN
            return;
        }

        try {
            //check if the user is logged in as an employee,
            //if so, we'll just return that user's tickets
            if (principal.getRole() == 3){
                Set<Ticket> tickets = ticketService.getTicketsByUsername(principal.getUsername()); //get the tickets, adds to set
                String ticketsJSON = mapper.writeValueAsString(tickets); //packaged into json
                System.out.println(ticketsJSON); //breadcrumbs
                respWriter.write(ticketsJSON); //kobe
                resp.setStatus(200); // confirmed
            }

            //if user is an admin or manager, return all the tickets
            if (principal.getRole() == 1 || principal.getRole() == 2) {
                Set<Ticket> tickets = ticketService.getAllTickets(); //get the tickets, adds to set
                String ticketsJSON = mapper.writeValueAsString(tickets); //packaged into json
                System.out.println(ticketsJSON); //breadcrumbs
                respWriter.write(ticketsJSON); //kobe
                resp.setStatus(200); // confirmed
            }
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


    /** doPUT
     * From employee: updates a pending ticket
     * From manager/admin: resolves a ticket
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    //this is a PUT request to follow with REST rules
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("application/json");
        ObjectMapper mapper = new ObjectMapper();
        PrintWriter respWriter = resp.getWriter();

        //===================== validate user=====================
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

        //double check and make sure the role is valid
        if (principal.getRole() < 1 || principal.getRole() > 3) {
            ErrorResponse err = new ErrorResponse(400, "Malformed role ID.");
            respWriter.write(mapper.writeValueAsString(err));
            resp.setStatus(400); // 403 = FORBIDDEN
            return;
        }

        try {
            //check if the user is logged in as an employee,
            //if so, we'll send to edit methods
            if (principal.getRole() == 3){
                Ticket ticket = mapper.readValue(req.getInputStream(), Ticket.class); //map to ticket
                ticket.setAuthorID(principal.getId()); //adding id to the ticket, from session (principal)
                System.out.println(ticket.toString()); //breadcrumb
                ticketService.editPending(ticket, principal); //send to repo, dao to update
                resp.setStatus(201); // 201 = CREATED
            }

            //if user is an admin or manager, send to resolver methods
            if (principal.getRole() == 1 || principal.getRole() == 2) {
                Ticket ticket = mapper.readValue(req.getInputStream(), Ticket.class); //map to ticket
                ticket.setResolverID(principal.getId()); //adding resolver id to the ticket, from session (principal)
                ticket.setResolvedWithCurrentTime(); //adding timestamp
                System.out.println(ticket.toString()); //breadcrumb
                ticketService.resolve(ticket); //send to repo, dao to update
                resp.setStatus(201); // 201 = CREATED
            }

        } catch (MismatchedInputException mie) {

            resp.setStatus(400); // 400 = BAD REQUEST
            ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed ticket object found in request body");
            String errJSON = mapper.writeValueAsString(err);
            respWriter.write(errJSON);

        } catch (Exception e) {

            e.printStackTrace();
            resp.setStatus(500); // 500 = INTERNAL SERVER ERROR
            ErrorResponse err = new ErrorResponse(500, "Mistakes were made.");
            respWriter.write(mapper.writeValueAsString(err));

        }

    }

    /** doPOST
     * From employee: creates a new ticket
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

        //===================== validate user=====================
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

        //double check and make sure the role is valid
        if (principal.getRole() < 1 || principal.getRole() > 3) {
            ErrorResponse err = new ErrorResponse(400, "Malformed role ID.");
            respWriter.write(mapper.writeValueAsString(err));
            resp.setStatus(400); // 403 = FORBIDDEN
            return;
        }

        try {
            Ticket ticket = mapper.readValue(req.getInputStream(), Ticket.class); //map to ticket
            ticket.setSubmittedWithCurrentTime(); //timestamp
            ticket.setAuthorID(principal.getId()); //set the author
            System.out.println(ticket.toString()); //breadcrumb
            ticketService.submit(ticket); //send to repo, dao to update
            resp.setStatus(201); // 201 = CREATED

        } catch (MismatchedInputException mie) {

            resp.setStatus(400); // 400 = BAD REQUEST
            ErrorResponse err = new ErrorResponse(400, "Bad Request: Malformed ticket object found in request body");
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

