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

public class RemoveSaleBlackBoxBDTest {
			
	private EntityManagerFactory emf;
	private EntityManager db;
	private DataAccess sut;
	
	private final String testEmail = "jon@gmail.com";
	private final String besteEmail = "enara@gmail.com";
	private final int testSaleNumber = 8888;
	
	@Before
	public void setUp() {
		emf = Persistence.createEntityManagerFactory("objectdb:test.odb");
		db = emf.createEntityManager();
		sut = new DataAccess(db);
	}
	
	@After
	public void tearDown() {
		if (db != null && db.isOpen()) {
			try {
				if (db.getTransaction().isActive()) {
					db.getTransaction().rollback();
				}
				
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

	// 1. KLASE BALIOKIDEA: Sarrera baliogabeak (Null balioak)
	@Test
	public void testNullEmailReturnsFalse() {
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber);
		
		boolean emaitza = sut.removeSale(null, sale);
		assertFalse("Emaila null bada, metodoak false itzuli beharko luke.", emaitza);
	}
	
	@Test
	public void testNullSaleReturnsFalse() {
		boolean emaitza = sut.removeSale(testEmail, null);
		assertFalse("Salmenta null bada, metodoak false itzuli beharko luke.", emaitza);
	}
	
	// 2. KLASE BALIOKIDEA: Erregistratu gabeko entitateak (Negozio logika)
	@Test
	public void testUnregisteredSellerReturnsFalse() {
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber);
		
		// DB-an ez dugu inor sartu, beraz saltzailea ez da existitzen
		boolean emaitza = sut.removeSale(testEmail, sale);
		assertFalse("Saltzailea ez bada existitzen DBan, false itzuli behar du.", emaitza);
	}

	// 3. KLASE BALIOKIDEA: Baimenik gabeko eragiketak (Beste baten salmenta ezabatzea)
	@Test
	public void testRemovingOtherSellersSaleReturnsFalse() {
		db.getTransaction().begin();
		Seller saltzailea = new Seller(testEmail, "Jon", "pasahitza");
		Seller jabeErreala = new Seller(besteEmail, "Enara", "pasahitza");
		
		Sale sale = new Sale();
		sale.setSaleNumber(testSaleNumber);
		sale.setSeller(jabeErreala);
		
		db.persist(saltzailea);
		db.persist(jabeErreala);
		db.persist(sale);
		db.getTransaction().commit();
		
		boolean emaitza = sut.removeSale(testEmail, sale);
		assertFalse("Ezin da beste saltzaile baten salmenta ezabatu.", emaitza);
	}
	
	// 4. KLASE BALIOKIDEA: Jada saldutako produktuak (Negozio arau espezifikoa)
	@Test
	public void testRemovingAlreadySoldProductReturnsFalse() {
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
		
		assertFalse("Salduta dagoen produktua ezin da ezabatu.", emaitza);
		
		Sale dbSale = db.find(Sale.class, testSaleNumber);
		assertNotNull("Salmentak DBan jarraitu behar du.", dbSale);
	}
	
	// 5. KLASE BALIOKIDEA: Bide zuzena (Dena ondo)
	@Test
	public void testSuccessfulRemoval() {
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
		
		assertTrue("Dena ondo badago metodoak true itzuli behar du.", emaitza);
		
		Sale dbSale = db.find(Sale.class, testSaleNumber);
		assertNull("Salmenta DB-tik ezabatu behar da eragiketa ondo joan bada.", dbSale);
	}
}