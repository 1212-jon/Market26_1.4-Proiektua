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

public class RegisterWhiteBoxMockTest {

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
	public void testRegisterEmailNull() {
		Seller s = sut.register(null, "Hodei", "Abcde1234!");
		
		assertNull(s);
		verify(db, never()).find(any(),any());
		verify(db, never()).persist(any());
	}
	
	@Test
	public void testRegisterIzenaNull() {
		Seller s = sut.register("jon@ehu.eus", null , "Abcde1234!");
		
		assertNull(s);
		verify(db, never()).find(any(),any());
		verify(db, never()).persist(any());
	}

	@Test
	public void testRegisterPasahitzaNull() {
		Seller s = sut.register("enara@gmail.com", "Hodei", null);
		
		assertNull(s);
		verify(db, never()).find(any(),any());
		verify(db, never()).persist(any());
	}

	
	@Test
	public void testRegisterExistitzenDaMock() {
		String email = "existitu@ehu.eus";
		Seller berria = new Seller(email, "Exi", "Abcde1234!");
		
		when(db.find(Seller.class,email)).thenReturn(berria);
		
		Seller s = sut.register(email, "Jon", "Abcde1234!z");
		
		assertNull(s);
		verify(db, times(1)).find(Seller.class,email);
		verify(db, never()).persist(any(Seller.class));
		verify(et, times(1)).commit();
	}

	@Test
	public void testRegisterEzDaExistitzenMock() {
		String email = "berria@ehu.eus";
		
		when(db.find(Seller.class,email)).thenReturn(null);
		
		Seller s = sut.register(email, "Jon", "Abcde1234!z");
		
		assertNotNull(s);
		assertEquals(email,s.getEmail());
		
		verify(db, times(1)).find(Seller.class,email);
		verify(db, times(1)).persist(any(Seller.class));
		verify(et, times(1)).commit();
	}
	
	@Test(expected = RuntimeException.class)
	public void testRegisterExceptionMock() {
		String email = "errorea@ehu.eus";
		
		when(db.find(Seller.class,email)).thenThrow(new RuntimeException("DBan Errorea"));
		
		Seller s = sut.register(email, "Hodei", "Abcde1234!");

        assertNull(s);
	}
}
