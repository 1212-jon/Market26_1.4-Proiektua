package test_removeSale;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;

public class RemoveSaleWhiteBoxTestMock {
			
	@Mock
	private EntityManager db;
	
	@Mock
	private EntityTransaction et;
	
	private DataAccess sut;
	
	@Before
	public void setUp(){
		db = mock(EntityManager.class);
		et = mock(EntityTransaction.class);
		
		when(db.getTransaction()).thenReturn(et);
		
		sut = new DataAccess(db);
	}
	

	@Test
	public void emailNull() {
		Sale sale = new Sale();
		sale.setSaleNumber(123);
		
		boolean emaitza = sut.removeSale(null, sale);
		
		assertFalse(emaitza);
	}
	

	/**@Test
	public void saleNull(){
		String email = "jon@gmail.com";
		
		boolean emaitza = sut.removeSale(email, null);
		
		assertFalse(emaitza);
	}
	
	@Test
	public void sellerNotFound() {
		String email = "jon@gmail.com";
		Sale sale = new Sale();
		sale.setSaleNumber(123);
		
		when(db.find(Seller.class, email)).thenReturn(null);
		
		boolean emaitza = sut.removeSale(email, sale);
		
		assertFalse(emaitza);
	}
	
	@Test
	public void saleNotFound() {
		String email = "jon@gmail.com";
		Seller saltzailea = new Seller(email, "Jon", "pasahitza");
		
		Sale sale = new Sale();
		sale.setSaleNumber(123);
		
		when(db.find(Seller.class, email)).thenReturn(saltzailea);
		when(db.find(Sale.class, 123)).thenReturn(null); // Salmentarik ez da aurkitzen
		
		boolean emaitza = sut.removeSale(email, sale);
		
		assertFalse(emaitza);
	}
	
	@Test
	public void wrongSeller() {
		String email = "jon@gmail.com";
		Seller saltzailea = new Seller(email, "Jon", "pasahitza");
		
		String besteEmail = "ane@gmail.com";
		Seller besteSaltzailea = new Seller(besteEmail, "Ane", "pasahitza");
		
		Sale sale = new Sale();
		sale.setSaleNumber(123);
		sale.setSeller(besteSaltzailea); // Salmenta beste norbaitena da
		
		when(db.find(Seller.class, email)).thenReturn(saltzailea);
		when(db.find(Sale.class, 123)).thenReturn(sale);
		
		boolean emaitza = sut.removeSale(email, sale);
		
		assertFalse(emaitza);
	}
	
	@Test
	public void productAlreadySold() {
		String email = "jon@gmail.com";
		Seller saltzailea = new Seller(email, "Jon", "pasahitza");
		
		Sale sale = new Sale();
		sale.setSaleNumber(123);
		sale.setSeller(saltzailea);
		sale.setSold(true); 
		
		when(db.find(Seller.class, email)).thenReturn(saltzailea);
		when(db.find(Sale.class, 123)).thenReturn(sale);
		
		boolean emaitza = sut.removeSale(email, sale);
		
		assertFalse(emaitza);
		verify(et, times(1)).rollback(); 
	}
	
	@Test
	public void removeSaleSuccess() {
		String email = "jon@gmail.com";
		Seller saltzailea = new Seller(email, "Jon", "pasahitza");
		
		Sale sale = new Sale();
		sale.setSaleNumber(123);
		sale.setSeller(saltzailea);
		sale.setSold(false); 
		
		when(db.find(Seller.class, email)).thenReturn(saltzailea);
		when(db.find(Sale.class, 123)).thenReturn(sale);
		
		boolean emaitza = sut.removeSale(email, sale);
		
		assertTrue(emaitza);
		verify(db, times(1)).remove(sale);
		verify(et, times(1)).commit();
	}
	
	@Test
	public void exceptionThrown() {
		String email = "jon@gmail.com";
		Sale sale = new Sale();
		sale.setSaleNumber(123);
		
		when(et.isActive()).thenReturn(true);
		
		when(db.find(Seller.class, email)).thenThrow(new RuntimeException("Error simulado"));
		
		boolean emaitza = sut.removeSale(email, sale);
		
		assertFalse(emaitza);
		verify(et, times(1)).rollback(); 
	}*/
}