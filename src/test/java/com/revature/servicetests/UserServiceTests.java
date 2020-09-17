package com.revature.servicetests;

import com.revature.ers.dtos.Principal;
import com.revature.ers.exceptions.AuthenticationException;
import com.revature.ers.exceptions.InvalidRequestException;
import com.revature.ers.exceptions.ResourceNotFoundException;
import com.revature.ers.models.AppUser;
import com.revature.ers.repos.UserRepository;
import com.revature.ers.services.UserService;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import java.util.HashSet;
import java.util.Set;

public class UserServiceTests {

    private UserService sut;
    private UserRepository mockUserRepo = Mockito.mock(UserRepository.class);
    Set<AppUser> mockedUsers = new HashSet<>();

    @Before
    public void setup() {
        sut = new UserService();
        mockedUsers.add(new AppUser(5, "fn", "ln", "t1", "pw1", "test1@fake.com", 1));
        mockedUsers.add(new AppUser(9, "fn", "ln", "t2", "pw1", "test2@fake.com", 3));
        mockedUsers.add(new AppUser(99, "fn", "ln", "t3", "pw1", "test2@fake.com", 3));
        mockedUsers.add(new AppUser(500, "fn", "ln", "t4", "pw1", "test2@fake.com", 2));
    }

    @After
    public void tearDown() {
        sut = null;
        mockedUsers.removeAll(mockedUsers);
    }
    //creating
    @Test(expected = InvalidRequestException.class)
    public void loginWithBlankCredentials() {
        sut.authenticate(" ", " ", 1);
    }
    @Test(expected = AuthenticationException.class)
    public void loginWithBadData() {
        sut.authenticate("bad", "bad2", 1);
    }
    //reading
    @Test (expected = InvalidRequestException.class)
    public void registerNullUser() {
        sut.register(null);
    }
    //updating
    @Test (expected = InvalidRequestException.class)
    public void updateNullUser() {
        AppUser nullUser = null;
        sut.update(nullUser);
    }
    //deleting
    @Test (expected = InvalidRequestException.class)
    public void deleteNonUser() {
        AppUser test = new AppUser(500, "fn", "ln", "t4", "pw1", "BLANK@fake.com", 2);
        sut.deleteUserByEmail(test);
    }
}