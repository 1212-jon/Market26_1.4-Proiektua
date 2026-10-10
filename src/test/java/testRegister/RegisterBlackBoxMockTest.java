package testRegister;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;

import domain.Seller;

public class RegisterBlackBoxMockTest {

	@Mock
    private EntityManager db;

    @Mock
    private EntityTransaction et;

    private DataAccess sut;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sut = new DataAccess(db);
        when(db.getTransaction()).thenReturn(et);
    }

	
    
	@Test
	public void testRegisterSuccessMock() {
		
		String email = "hodei.mock@gmail.com";
		
		when(db.find(Seller.class, email)).thenReturn(null);
		
		Seller ema = sut.register(email, "Hodei", "Enara1234!");
		
		//Egiaztapenak:
		assertNotNull("Erregistratuak ezin du null-ik izan:", ema);
		assertEquals(email, ema.getEmail());
		assertEquals("Hodei", ema.getName());
		
		// Exekuzioa eta transakzioa ondo joan direla egiaztatu
		verify(db,times(1)).persist(any(Seller.class));
		verify(et, times(1)).commit();
		
	}
	
	@Test
	public void testRegisterExistitzenDaMock() {
		
		String email = "existitu@gmail.com";
		Seller existitu = new Seller(email, "Existitu", "125RTBaaa!!");
		
		when(db.find(Seller.class, email)).thenReturn(existitu);
		
		Seller ema = sut.register(email, "Enara", "125RTBaaa!!");
		
		//Egiaztapenak:
		assertNull("Jada existitzen da norbait email horrekin:", ema);
		
		verify(db,never()).persist(any(Seller.class));
		verify(et, times(1)).commit();
		
	}

}
