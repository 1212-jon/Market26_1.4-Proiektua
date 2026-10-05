package testRegister;

import static org.junit.Assert.*;

import org.junit.AfterClass;
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
	
	@Test
	public void testRegisterSuccessBD() {
		Seller s = sut.register("jonsalegi@gmail.com", "Hodei", "EnaraANª123");
		
		//Egiaztapena:
		assertNotNull("Erregistratakoak ezin du null-ik izan:", s);
		assertEquals("jonsalegi@gmail.com",s.getEmail());
		assertEquals("Hodei",s.getName());
	}
	
	@Test
	public void testRegisterExistituBD() {
		//Datu basean sartu:
		sut.register("jonelor@gmail.com", "Hodeiiii", "EnaraANª123");
		
		//Erregistratzen saiatu (email bera, beraz dagoeneko egon)
		Seller s = sut.register("jonelor@gmail.com", "Hodeiiii", "EnaraANª123");
		
		//Egiaztapena:
		assertNull("Dagoeneko badaho email hau:",s);
	}
	
	@Test
	public void testRegisterNullParametroak() {
		//3 seller ta bakoitzak parametro desberdin bat null
		Seller s1 = sut.register(null, "Hodei", "Enara1234!");
		Seller s2 = sut.register("jon@gmail.com", null, "Enara1234!");
		Seller s3 = sut.register("jonpa@gmail.com", "Hodei_Sal", null);
		
		//Egiaztapena:
		assertNull("Emaila null",s1);
		assertNull("Izena null",s2);
		assertNull("Pasahitza null",s3);
	}
}
