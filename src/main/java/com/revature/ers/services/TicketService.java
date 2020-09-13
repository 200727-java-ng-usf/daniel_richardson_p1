package com.revature.ers.services;

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

}
