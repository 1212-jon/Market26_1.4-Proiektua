package test.createReclamation;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;

public class CreateReclamationBlackBoxBDTest {

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
    public void testCreateReclamationSuccessBD() {
        // 1. Datuak prestatu DBan
        Seller saltzaile = new Seller("saltzaileBD@gmail.com", "Saltzaile Test", "123");
        Seller erosle = new Seller("erosleBD@gmail.com", "Erosle Test", "123");
        
        // ZUZENDUTA: null fitxategiaren ordez eta saltzaile amaieran
        Sale sale = new Sale("Produktua Test", "Deskribapena", 1, 50.0f, new Date(), null, saltzaile);
        sale.setSaleNumber(9999); // Probako identifikadore bat

        sut.addSellerWithSaleAndBuyer(saltzaile, sale, erosle);

        // 2. Metodoa exekutatu (int, String, String) -> boolean
        boolean res = sut.createReclamation(9999, "Produktu okerra iritsi da", "erosleBD@gmail.com");

        // 3. Egiaztapena
        assertTrue("Erreklamazioa zuzen sortu beharko litzateke", res);

        // 4. Garbiketa
        sut.removeReclamationTestData("saltzaileBD@gmail.com", "erosleBD@gmail.com", 9999);
    }
}