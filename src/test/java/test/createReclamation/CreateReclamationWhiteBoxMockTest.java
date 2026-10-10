package test.createReclamation;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dataAccess.DataAccess;
import domain.Reclamation;
import domain.Sale;
import domain.Seller;

public class CreateReclamationWhiteBoxMockTest {

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
    public void testCreateReclamationNullDescriptionMock() {
        // Path 1: deskribapena null denean
        boolean res = sut.createReclamation(100, null, "erosle@gmail.com");

        assertFalse(res);
        verify(db, never()).find(any(), any());
    }

    @Test
    public void testCreateReclamationNullBuyerEmailMock() {
        // Path 2: buyerEmail null denean
        boolean res = sut.createReclamation(100, "Deskribapena", null);

        assertFalse(res);
        verify(db, never()).find(any(), any());
    }

    @Test
    public void testCreateReclamationSaleNotFoundMock() {
        // Path 3: Salmenta ez denean aurkitzen (null)
        when(db.find(Sale.class, 100)).thenReturn(null);

        boolean res = sut.createReclamation(100, "Deskribapena", "erosle@gmail.com");

        assertFalse(res);
        verify(db, times(1)).find(Sale.class, 100);
        verify(db, never()).persist(any());
    }

    @Test
    public void testCreateReclamationBuyerNotFoundMock() {
        // Path 4: Salmenta badago baina eroslea ez da aurkitzen
        Sale mockSale = new Sale();
        when(db.find(Sale.class, 100)).thenReturn(mockSale);
        when(db.find(Seller.class, "ez_existitzen@gmail.com")).thenReturn(null);

        boolean res = sut.createReclamation(100, "Deskribapena", "ez_existitzen@gmail.com");

        assertFalse(res);
        verify(db, times(1)).find(Seller.class, "ez_existitzen@gmail.com");
        verify(db, never()).persist(any());
    }

    
    @Test
    public void testCreateReclamationExceptionHandlingMock() {
        // Configurar el mock para simular una excepción en la base de datos
        Mockito.when(db.find(Mockito.any(), Mockito.any())).thenThrow(new RuntimeException("DB Error"));
        
        // IMPORTANTE: Indicar que la transacción está activa
        Mockito.when(et.isActive()).thenReturn(true);

        boolean result = sut.createReclamation(1, "Descripción", "buyer@gmail.com");

        assertFalse(result);
        Mockito.verify(et, Mockito.times(1)).rollback();
    }
    
    
    
}