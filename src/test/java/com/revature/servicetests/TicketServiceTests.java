package com.revature.servicetests;

import com.revature.ers.dtos.Principal;
import com.revature.ers.exceptions.AuthenticationException;
import com.revature.ers.exceptions.InvalidRequestException;
import com.revature.ers.exceptions.ResourceNotFoundException;
import com.revature.ers.models.AppUser;
import com.revature.ers.models.Ticket;
import com.revature.ers.repos.TicketRepository;
import com.revature.ers.repos.UserRepository;
import com.revature.ers.services.TicketService;
import com.revature.ers.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.HashSet;
import java.util.Set;

public class TicketServiceTests {

    private TicketService sut;
    private TicketRepository mockUserRepo = Mockito.mock(TicketRepository.class);
    Set<Ticket> mockedTickets = new HashSet<>();

    @Before
    public void setup() {
        sut = new TicketService();
        mockedTickets.add(new Ticket(1, 200, "desc", 1, 9, 1, 1));
        mockedTickets.add(new Ticket(2, 300, "desc", 2, 8, 2, 2));
        mockedTickets.add(new Ticket(3, 400, "desc", 3, 7, 3, 3));
        mockedTickets.add(new Ticket(4, 500, "desc", 4, 6, 1, 4));
    }

    @After
    public void tearDown() {
        sut = null;
        mockedTickets.removeAll(mockedTickets);
    }
    //creating
    @Test(expected = InvalidRequestException.class)
    public void createNullTicket() {
//        Ticket test = mockedTickets.iterator().next();
        Ticket test = null;
        sut.submit(test);
    }
    @Test(expected = InvalidRequestException.class)
    public void createTicketNegAmount() {
        Ticket test = mockedTickets.iterator().next();
        test.setAmount(-50);
        System.out.println(test.toString());
        sut.submit(test);
    }
    @Test(expected = InvalidRequestException.class)
    public void createTicketZeroAmount() {
        Ticket test = mockedTickets.iterator().next();
        test.setAmount(0);
        System.out.println(test.toString());
        sut.submit(test);
    }
    @Test(expected = InvalidRequestException.class)
    public void createTicketBadDescription() {
        Ticket test = mockedTickets.iterator().next();
        test.setDescription(" ");
        System.out.println(test.toString());
        sut.submit(test);
    }
    @Test(expected = InvalidRequestException.class)
    public void createTicketBadTypeId() {
        Ticket test = mockedTickets.iterator().next();
        test.setTypeID(-9);
        System.out.println(test.toString());
        sut.submit(test);
    }
    //reading
//    @Test(expected = InvalidRequestException.class)
//    public void getTickets() {
//        mockedTickets.remove(mockedTickets.iterator().next());
//        mockedTickets.remove(mockedTickets.iterator().next());
//        mockedTickets.remove(mockedTickets.iterator().next());
//        Set<Ticket> sutResult = sut.getAllTickets();
//        Assert.assertEquals(mockedTickets.iterator().next().hashCode(), sutResult.iterator().next().hashCode());
//    }
    //updating
    @Test(expected = InvalidRequestException.class)
    public void updateTicketBadTypeId() {
        Ticket test = mockedTickets.iterator().next();
        test.setId(-9);
        Principal testp = new Principal(new AppUser(1, "n", "l", "a", "a", "q", 3));
        System.out.println(test.toString());
        sut.editPending(test, testp);
    }
    //deleting
    //tickets can't be deleted


}