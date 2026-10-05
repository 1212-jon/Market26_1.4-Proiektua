package test_removeSale;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import dataAccess.DataAccess;
import domain.Reclamation;
import domain.Sale;
import domain.Seller;

public class RemoveSaleBlackBoxMockTest {
	
	@Mock
	private EntityManager db;
	
	@Mock
	private EntityTransaction et;
	
	private DataAccess sut;
	
	@Before
	public void setUp(){
		db=mock(EntityManager.class);
		
		et=mock(EntityTransaction.class);
		
        when(db.getTransaction()).thenReturn(et);
        
        sut= new DataAccess(db);
	}
	
	@Test 
	public void removeSaleSuccesTestMock() {
		String email="jon@gmail.com";
		String izena="jon";
		String password="Jonjon12!";
		
		Seller saltzailea= new Seller(email, izena, password);
		
		int saleNumber=123;
		
		Sale sale = new Sale();
        sale.setSaleNumber(saleNumber);
        
        sale.setSeller(saltzailea);
		
		// Mock-en portaera definitu
        when(db.find(Sale.class, saleNumber)).thenReturn(sale);
		when(db.find(Seller.class, email)).thenReturn(saltzailea);
		
		boolean emaitza= sut.removeSale(email, sale);
		
		assertTrue(emaitza);
		verify(db, times(1)).remove(sale);
		verify(et, times(1)).commit();
	}

}
