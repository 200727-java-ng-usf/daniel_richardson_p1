package com.revature.servicetests;
import com.revature.ers.services.ConnectionService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.sql.Connection;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class ConnectionTests {
    private ConnectionService sut;
    @Before
    public void setup() {
        sut = ConnectionService.getInstance();
    }
    @After
    public void tearDown() {
        sut = null;
    }
    @Test
    public void isConnected() {
        Connection conn = ConnectionService.getInstance().getConnection();
        assertNotNull(conn);
    }
    @Test
    public void isSingleton() {
        ConnectionService con1 = ConnectionService.getInstance();
        ConnectionService con2 = ConnectionService.getInstance();
        assertSame(con1, con2);
    }
}