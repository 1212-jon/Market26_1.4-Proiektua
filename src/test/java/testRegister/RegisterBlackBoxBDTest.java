package testRegister;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Seller;

public class RegisterBlackBoxBDTest {

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

    // Test bakoitzaren aurretik erabili daitezkeen test-helbideak garbitzen ditugu
    @Before
    @After
    public void cleanUp() {
        if (sut != null) {
            sut.removeSeller("jonsalegi@gmail.com");
            sut.removeSeller("jonelor@gmail.com");
        }
    }

    @Test
    public void testRegisterSuccessBD() {
        Seller s = sut.register("jonsalegi@gmail.com", "Hodei", "EnaraANª123");
        
        // Egiaztapena:
        assertNotNull("Erregistratakoak ezin du null-ik izan:", s);
        assertEquals("jonsalegi@gmail.com", s.getEmail());
        assertEquals("Hodei", s.getName());
    }

    @Test
    public void testRegisterExistituBD() {
        // Datu-basean sartu lehenik:
        sut.register("jonelor@gmail.com", "Hodeiiii", "EnaraANª123");
        
        // Erregistratzen saiatu (email bera, beraz dagoeneko badago)
        Seller s = sut.register("jonelor@gmail.com", "Hodeiiii", "EnaraANª123");
        
        // Egiaztapena:
        assertNull("Dagoeneko badago email hau:", s);
    }

    @Test
    public void testRegisterNullParametroak() {
        // Parametro null-ekin saiatu
        Seller s1 = sut.register(null, "Hodei", "Enara1234!");
        Seller s2 = sut.register("jon@gmail.com", null, "Enara1234!");
        Seller s3 = sut.register("jonpa@gmail.com", "Hodei_Sal", null);
        
        // Egiaztapena:
        assertNull("Emaila null", s1);
        assertNull("Izena null", s2);
        assertNull("Pasahitza null", s3);
    }
}