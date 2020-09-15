package com.revature.ers.services;

import com.revature.ers.dtos.Principal;
import com.revature.ers.exceptions.InvalidRequestException;
import com.revature.ers.exceptions.ResourceNotFoundException;
import com.revature.ers.models.AppUser;
import com.revature.ers.models.Ticket;
import com.revature.ers.repos.TicketRepository;
import com.revature.ers.repos.UserRepository;

import java.util.Set;

public class TicketService {

    private TicketRepository ticketRepo = new TicketRepository();

    public Set<Ticket> getAllTickets() {

        Set<Ticket> tickets = ticketRepo.findAllTickets();

        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return tickets;

    }
    public Set<Ticket> getTicketsByUsername(String username) {
        Set<Ticket> tickets = ticketRepo.findTicketsByUsername(username);
        if (tickets.isEmpty()) {
            throw new ResourceNotFoundException();
        }
        return tickets;
    }


    public void resolve(Ticket ticket) {
        //make sure updated value isn't bad


        if (!resolveValidate(ticket)) {
            throw new InvalidRequestException("Invalid user field values provided during update!");
        }
        ticketRepo.resolve(ticket);

    }
    public boolean resolveValidate(Ticket ticket){
        if (ticket.getId()==0) return false;
        if (ticket.getStatusID() == 0) return false;
        return true;
    }
    public void editPending(Ticket ticket, Principal principal) {
        //make sure updated value isn't bad and that user is permitted to edit the ticket
        if (!editValid(ticket)){ //todo shred out these into separate exceptions
            throw new InvalidRequestException("Invalid input, ticket not pending, or no such ticket!");
        }
        ticketRepo.editPending(ticket);

    }
    public boolean editValid(Ticket ticket){
        if(!ticketRepo.validateTicketWithUser(ticket)) return false; //is the user editing own ticket?
        if(!submitValidate(ticket)) return false;
        return true;
    }

    public void submit(Ticket ticket){
        if (!submitValidate(ticket)) {
            throw new InvalidRequestException("Invalid user field values provided during registration!");
        }
        ticketRepo.submit(ticket);
    }
    public boolean submitValidate(Ticket ticket){
        System.out.println("Validating ticket...");
        if (ticket == null) return false;
        if (ticket.getAmount() <= 0) return false;
        if (ticket.getDescription() == null || ticket.getDescription().trim().equals("")) return false;
        if (ticket.getTypeID() < 0 || ticket.getTypeID() > 5) return false;
        return true;
    }


}
