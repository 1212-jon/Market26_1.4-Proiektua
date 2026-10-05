package testRegister;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Seller;

public class RegisterWhiteBoxBDTest {

    static DataAccess sut;

    @BeforeClass
    public static void setUpClass() {
        sut = new DataAccess();
        sut.open();
    }

    @AfterClass
    public static void tearDownClass() {
        sut.close();
    }

    // Test bakoitzaren aurretik eta ondoren datu-basea garbitzen dugu
    @Before
    @After
    public void cleanUp() {
        if (sut != null) {
            sut.removeSeller("jonelortegi@gmail.com");
            sut.removeSeller("jonhodeienara@gmail.com");
            sut.removeSeller("jonhodei@gmail.com");
        }
    }

    @Test
    public void testRegisterEmailNull() {
        Seller s = sut.register(null, "Enara", "Abcde1234!");
        assertNull(s);
    }

    @Test
    public void testRegisterIzenaNull() {
        Seller s = sut.register("jonelortegi@gmail.com", null, "Abcde1234!");
        assertNull(s);
    }

    @Test
    public void testRegisterPasahitzaNull() {
        Seller s = sut.register("jonhodeienara@gmail.com", "Enara", null);
        assertNull(s);
    }

    @Test
    public void testRegisterEmailaDBan() {
        // Sortu datu basean egoteko.:
        sut.register("jonhodeienara@gmail.com", "Enara", "Abcde1234!");
        
        // Berriro saiatu (dagoeneko badago)
        Seller s = sut.register("jonhodeienara@gmail.com", "Enara", "Abcde1234!");
        
        // Badago, orduan null itzuli
        assertNull(s);
    }

    @Test
    public void testRegisterEmailaEzDagoDBan() {
        Seller s = sut.register("jonhodei@gmail.com", "Hodei", "Abcde1234!");
        
        assertNotNull(s);
        assertEquals("jonhodei@gmail.com", s.getEmail());
        assertEquals("Hodei", s.getName());
    }
}