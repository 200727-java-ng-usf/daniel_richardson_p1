package com.revature.ers.services;

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
    public void resolve(Ticket ticket) {
        //make sure updated value isn't bad


        if (!resolveValidate(ticket)) {
            throw new InvalidRequestException("Invalid user field values provided during update!");
        }
        ticketRepo.resolve(ticket);

    }
    public boolean resolveValidate(Ticket ticket){
        if (ticket.getId()==0) return false;
//        if (ticket.getResolverID()==0) return false; //resolver id is given by principal, there's already a check for that
        if (ticket.getStatusID() == 0) return false;
        return true;
    }


}
