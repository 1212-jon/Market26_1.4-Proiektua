package test.createReclamation;

import static org.junit.Assert.*;

import java.util.Date;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import dataAccess.DataAccess;
import domain.Sale;
import domain.Seller;

public class CreateReclamationWhiteBoxBDTest {

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
    public void testCreateReclamationNullDescriptionBD() {
        // Path 1: deskribapena null denean -> false bueltatu behar du
        boolean res = sut.createReclamation(100, null, "erosle@gmail.com");
        assertFalse("Deskribapena null bada false bueltatu behar du", res);
    }

    @Test
    public void testCreateReclamationNullBuyerEmailBD() {
        // Path 2: buyerEmail null denean -> false bueltatu behar du
        boolean res = sut.createReclamation(100, "Deskribapena", null);
        assertFalse("buyerEmail null bada false bueltatu behar du", res);
    }

    @Test
    public void testCreateReclamationSaleNotFoundBD() {
        // Path 3: Salmenta ez denean existitzen DBan -> false bueltatu behar du
        boolean res = sut.createReclamation(-9999, "Deskribapena", "erosle@gmail.com");
        assertFalse("Salmenta aurkitzen ez bada false bueltatu behar du", res);
    }

    @Test
    public void testCreateReclamationBuyerNotFoundBD() {
        // Path 4: Salmenta existitzen da baina eroslea ez dago DBan -> false bueltatu behar du
        Seller saltzaile = new Seller("saltzaileWB@gmail.com", "Saltzaile WB", "123");
        Seller erosle = new Seller("erosleWB@gmail.com", "Erosle WB", "123");
        Sale sale = new Sale("Produktua WB", "Deskribapena", 1, 30.0f, new Date(), null, saltzaile);
        sale.setSaleNumber(8888);

        sut.addSellerWithSaleAndBuyer(saltzaile, sale, erosle);

        // Existitzen ez den erosle baten emaila pasatzen dugu
        boolean res = sut.createReclamation(8888, "Deskribapena", "ez_existitzen@gmail.com");

        assertFalse("Eroslea aurkitzen ez bada false bueltatu behar du", res);

        // Garbiketa
        sut.removeReclamationTestData("saltzaileWB@gmail.com", "erosleWB@gmail.com", 8888);
    }
    
    
}