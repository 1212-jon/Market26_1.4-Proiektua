package test.createReclamation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Reclamation;
import domain.Sale;
import domain.Seller;

public class CreateReclamationBlackBoxMockTest {

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
    public void testCreateReclamationSuccessMock() {
        // Datuak simulatu
        int saleNumber = 100;
        String buyerEmail = "erosleMock@gmail.com";
        String deskribapena = "Produktu hondatua iritsi da";

        Seller saltzaile = new Seller("saltzaileMock@gmail.com", "Saltzaile Test", "123");
        Seller erosle = new Seller(buyerEmail, "Erosle Test", "123");
        Sale sale = new Sale();
        sale.setSaleNumber(saleNumber);
        sale.setSeller(saltzaile);

        // Mock-en portaera definitu
        when(db.find(Sale.class, saleNumber)).thenReturn(sale);
        when(db.find(Seller.class, buyerEmail)).thenReturn(erosle);

        // Metodoa exekutatu
        boolean result = sut.createReclamation(saleNumber, deskribapena, buyerEmail);

        // Egiaztapenak
        assertTrue("Erreklamazioak arrakastatsua izan behar luke", result);
        verify(db, times(1)).persist(any(Reclamation.class));
        verify(et, times(1)).commit();
    }
 
 
 
}