package test_removeSale;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;

public class RemoveSaleWhiteBoxBDTest {
			
	private EntityManagerFactory emf;
	private EntityManager db;
	private DataAccess sut;
	
	private final String testEmail = "jon_test@gmail.com";
	private final String besteEmail = "ane_test@gmail.com";
	private final int testSaleNumber = 9999;
	
	@Before
	public void setUp() {
		// KONTUZ: Jarri hemen zure benetako persistence unit izena!
		emf = Persistence.createEntityManagerFactory("objectdb:test.odb"); 
		db = emf.createEntityManager();
		sut = new DataAccess(db); 
	}
	
	@After
	public void tearDown() {
		if (db != null && db.isOpen()) {
			try {
				// 1. ZUZENKETA: Aurreko testak (adibidez wrongSeller) transakzioa 
				// zintzilik utzi badu, lehenik rollback egingo dugu datu-basea desblokeatzeko.
				if (db.getTransaction().isActive()) {
					db.getTransaction().rollback();
				}
				
				// Orain bai, seguru gaude transakzio berri bat hasi eta dena garbitzeko
				db.getTransaction().begin();
				
				Sale s = db.find(Sale.class, testSaleNumber);
				if (s != null) db.remove(s);
				
				Seller seller1 = db.find(Seller.class, testEmail);
				if (seller1 != null) db.remove(seller1);
				
				Seller seller2 = db.find(Seller.class, besteEmail);
				if (seller2 != null) db.remove(seller2);
				
				db.getTransaction().commit();
			} catch (Exception e) {
				if (db.getTransaction().isActive()) db.getTransaction().rollback();
			} finally {
				db.close();
			}
		}
		if (emf != null && emf.isOpen()) {
			emf.close();
		}
	}

	@Test
	public void emailNull() {
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber);
		
		boolean emaitza = sut.removeSale(null, sale);
		assertFalse(emaitza);
	}
	
	@Test
	public void saleNull() {
		boolean emaitza = sut.removeSale(testEmail, null);
		assertFalse(emaitza);
	}
	
	@Test
	public void sellerNotFound() {
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber);
		
		boolean emaitza = sut.removeSale(testEmail, sale);
		assertFalse(emaitza);
	}
	
	@Test
	public void saleNotFound() {
		db.getTransaction().begin();
		Seller saltzailea = new Seller(testEmail, "Jon", "pasahitza");
		db.persist(saltzailea);
		db.getTransaction().commit();
		
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber); 
		
		boolean emaitza = sut.removeSale(testEmail, sale);
		assertFalse(emaitza);
	}
	
	@Test
	public void wrongSeller() {
		db.getTransaction().begin();
		
		Seller saltzailea = new Seller(testEmail, "Jon", "pasahitza");
		Seller besteSaltzailea = new Seller(besteEmail, "Ane", "pasahitza");
		
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber);
		sale.setSeller(besteSaltzailea);
		
		db.persist(saltzailea);
		db.persist(besteSaltzailea);
		db.persist(sale);
		db.getTransaction().commit();
		
		boolean emaitza = sut.removeSale(testEmail, sale);
		assertFalse(emaitza);
	}
	
	@Test
	public void productAlreadySold() {
		db.getTransaction().begin();
		Seller saltzailea = new Seller(testEmail, "Jon", "pasahitza");
		
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber);
		sale.setSeller(saltzailea);
		sale.setSold(true);
		
		db.persist(saltzailea);
		db.persist(sale);
		db.getTransaction().commit();
		
		boolean emaitza = sut.removeSale(testEmail, sale);
		
		assertFalse(emaitza);
		
		Sale dbSale = db.find(Sale.class, testSaleNumber);
		assertNotNull(dbSale);
	}
	
	@Test
	public void removeSaleSuccess() {
		db.getTransaction().begin();
		Seller saltzailea = new Seller(testEmail, "Jon", "pasahitza");
		
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber);
		sale.setSeller(saltzailea);
		sale.setSold(false); 
		
		db.persist(saltzailea);
		db.persist(sale);
		db.getTransaction().commit();
		
		boolean emaitza = sut.removeSale(testEmail, sale);
		
		assertTrue(emaitza);
		
		Sale dbSale = db.find(Sale.class, testSaleNumber);
		assertNull(dbSale);
	}
	
	@Test
	public void exceptionThrown() {
	    Sale sale = new Sale();
	    sale.setSaleNumber(testSaleNumber);
	    
	    // Aktibo badago lehenengo rollback egin dugu
	    if (db.getTransaction().isActive()) {
	        db.getTransaction().rollback();
	    }
	    
	    db.getTransaction().begin();
	    boolean emaitza = sut.removeSale(testEmail, sale);
	    assertFalse(emaitza);
	}
}