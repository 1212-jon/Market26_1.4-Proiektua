package businessLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebService;

import dataAccess.DataAccess;
import domain.BlacklistedUser;
import domain.Demand;
import domain.Reclamation;
import domain.Sale;
import domain.Seller;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

import java.awt.image.BufferedImage;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.IOException;

/**
 * It implements the business logic as a web service.
 */
@WebService(endpointInterface = "businessLogic.BLFacade")
public class BLFacadeImplementation implements BLFacade {
    private static final int baseSize = 160;
    private static final String basePath = "src/main/resources/images/";
    DataAccess dbManager;

    public BLFacadeImplementation() {
        System.out.println("Creating BLFacadeImplementation instance");
        dbManager = new DataAccess();
    }

    public BLFacadeImplementation(DataAccess da) {
        System.out.println("Creating BLFacadeImplementation instance with DataAccess parameter");
        dbManager = da;
    }

    /**
     * {@inheritDoc}
     */
    @WebMethod
    public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
        dbManager.open();
        Sale product = dbManager.createSale(title, description, status, price, pubDate, sellerEmail, file);
        dbManager.close();
        return product;
    }

    /**
     * {@inheritDoc}
     */
    @WebMethod
    public List<Sale> getSales(String desc) {
        dbManager.open();
        List<Sale> rides = dbManager.getSales(desc);
        dbManager.close();
        return rides;
    }

    /**
     * {@inheritDoc}
     */
    @WebMethod
    public List<Sale> getPublishedSales(String desc, Date pubDate) {
        dbManager.open();
        List<Sale> rides = dbManager.getPublishedSales(desc, pubDate);
        dbManager.close();
        return rides;
    }

    /**
     * {@inheritDoc}
     */
    @WebMethod
    public BufferedImage getFile(String fileName) {
        dbManager.open();
        BufferedImage bi = dbManager.getFile(fileName);
        dbManager.close();
        return bi;
    }

    public void close() {
        dbManager.close();
    }

    /**
     * {@inheritDoc}
     */
    @WebMethod
    public void initializeBD() {
        dbManager.open();
        dbManager.initializeDB();
        dbManager.close();
    }

    /**
     * {@inheritDoc}
     */
    @WebMethod
    public Image downloadImage(String imageName) {
        File image = new File(basePath + imageName);
        try {
            return ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // --- METODO BERRIAK ---

    /**
     * Erabiltzailearen kredentzialak egiaztatzeko metodoa.
     * {@inheritDoc}
     */
    @WebMethod
    public Seller isLogin(String email, String password) {
        dbManager.open();
        Seller s = dbManager.isLogin(email, password);
        return s;
    }

    /**
     * Erabiltzaile berri bat datu-basean erregistratzeko metodoa.
     * {@inheritDoc}
     */
    @WebMethod
    public Seller register(String email, String name, String password) {
        dbManager.open();
        Seller s = dbManager.register(email, name, password);
        dbManager.close();
        return s;
    }

    /**
     * Produktu bat datu-basetik kentzeko metodoa.
     * {@inheritDoc}
     */
    @WebMethod
    public boolean removeSale(String email, Sale sale) {
        // Datu-basea ireki
        dbManager.open();
        
        // DataAccess-ari eskatu produktua ezabatzeko
        boolean res = dbManager.removeSale(email, sale);
        
        // Datu-basea itxi
        dbManager.close();
        
        return res;
    }
    
    /**
	 * {@inheritDoc}
	 */
	@WebMethod
	public boolean buySale(int saleNumber, String buyerEmail) {
	    // 3. PUNTUA: Balidazioa Business Logic-en
	    if (saleNumber < 0 || buyerEmail == null) return false;
	    
	    dbManager.open();
	    // ORAIN BAI: Bi parametroak pasatzen dizkiogu DataAccess-i
	    boolean b = dbManager.buySale(saleNumber, buyerEmail); 
	    dbManager.close();
	    return b;
	}
	
	@WebMethod
	public void updateSaldo(String email, float saldoBerria) {
	    dbManager.open();
	    dbManager.updateSaldo(email, saldoBerria);
	    dbManager.close();
	}
	
	@WebMethod
	public boolean erosketaBurutu(Seller eroslea, Sale produktua) {
	    dbManager.open();
	    // Hemen egiten dugu lotura: GUI-ak ematen digun objektutik ID-a eta Emaila ateratzen ditugu
	    boolean ondo = dbManager.buySale(produktua.getSaleNumber(), eroslea.getEmail());
	    dbManager.close();
	    return ondo;
	}
	
	@WebMethod
	public Seller getUser(String email) {
	    dbManager.open();
	    Seller s = dbManager.getUser(email);
	    dbManager.close();
	    return s;
	}
	
	/**
     * {@inheritDoc}
     */
    @WebMethod
    public boolean createReclamation(int saleNumber, String deskribapena, String buyerEmail) {
        // 1. Datu-basea ireki
        dbManager.open();
        
        // 2. DataAccess-ari deitu benetako logika exekutatzeko
        boolean res = dbManager.createReclamation(saleNumber, deskribapena, buyerEmail);
        
        // 3. Datu-basea itxi
        dbManager.close();
        
        return res;
    }
    
    /**
     * {@inheritDoc}
     */
    @WebMethod
    public List<domain.Reclamation> getReclamations(String email) {
        dbManager.open();
        List<domain.Reclamation> kexak = dbManager.getReclamations(email);
        dbManager.close();
        return kexak;
    }
    
    @WebMethod
    public List<Reclamation> getAllReclamations() {
        dbManager.open();
        List<Reclamation> res = dbManager.getAllReclamations();
        dbManager.close();
        return res;	
    }
    
    @WebMethod
    public void updateReclamationStatus(int reclamationNumber, String newStatus, String soluzioa) {
        dbManager.open();
        dbManager.updateReclamationStatus(reclamationNumber, newStatus, soluzioa);
        dbManager.close();
    }
    
    @WebMethod
    public boolean addToBasket(String buyerEmail, int saleNumber) {
        dbManager.open();
        boolean res = dbManager.addToBasket(buyerEmail, saleNumber);
        dbManager.close();
        return res;
    }

    @WebMethod
    public void clearBasket(String email) {
        dbManager.open();
        dbManager.clearBasket(email);
        dbManager.close();
    }

    @WebMethod
    public boolean buyBasket(String buyerEmail) {
        dbManager.open();
        boolean res = dbManager.buyBasket(buyerEmail);
        dbManager.close();
        return res;
    }
    
    @WebMethod
    public boolean isBlacklisted(String email) {
        dbManager.open();
        boolean res = dbManager.isBlacklisted(email);
        dbManager.close();
        return res;
    }
    
    @WebMethod
    public boolean kontuaItxi(String email) {
        dbManager.open();
        boolean ondo = dbManager.kontuaItxi(email);
        dbManager.close();
        return ondo;
    }
    
    @WebMethod
    public List<Seller> getAllSellers() {
        dbManager.open(); 
        List<Seller> res = dbManager.getAllSellers();
        dbManager.close();
        return res;
    }
    
    @WebMethod
    public List<BlacklistedUser> getBlacklistedUsers() {
        dbManager.open();
        List<BlacklistedUser> res = dbManager.getBlacklistedUsers();
        dbManager.close();
        return res;
    }
    
    @Override
    public List<Seller> getActiveSellers(String filter) {
        dbManager.open();
        List<Seller> sellers = dbManager.getActiveSellers(filter);
        dbManager.close();
        return sellers;
    }

    @Override
    public List<Sale> getSalesBySeller(String sellerEmail) {
        dbManager.open();

        Seller s = dbManager.getUser(sellerEmail);
        List<Sale> sales = new ArrayList<>();
        
        if (s != null) {
            for (Sale sale : s.getSales()) {
                // Bakarrik saldu gabe eta erabilgarri daudenak gehitu
                if (!sale.isSold() && sale.isAvailable()) {
                    sales.add(sale);
                }
            }
        }
        
        dbManager.close();
        return sales;
    }
    @WebMethod
    public void removeFromBasket(String email, Integer saleId) {
        
        dbManager.open(); 
        
        dbManager.removeFromBasket(email, saleId);
        
        dbManager.close();
    }
    @WebMethod
    public Demand createDemand(String title, String description, float maxPrice, String claimantEmail) {
        dbManager.open();
        Demand d = dbManager.createDemand(title, description, maxPrice, claimantEmail);
        dbManager.close();
        return d;
    }
    @WebMethod
    public List<Sale> getOffersForDemand(Demand d) {
        dbManager.open(); // Konexioa ireki
        List<Sale> ofertak = null;
        try {
            ofertak = dbManager.getOffersForDemand(d);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.close(); // Ziurtatu beti itxi egiten dela
        }
        return ofertak;
    }

    @WebMethod
    public Sale createOffer(String title, float price, String description, String sellerEmail, Demand d) {
        Seller s = dbManager.getUser(sellerEmail);
        return dbManager.createOffer(title, price, description, s, d);
    }
    
    @WebMethod
    public List<Demand> getAllDemands() {
        dbManager.open();
        List<Demand> demands = dbManager.getAllDemands();
        dbManager.close();
        return demands;
    }

    @WebMethod
    public Sale createSale(String title, String description, int numStatus, float price, Date date, String sellerEmail, File image, Demand parent) throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException  {
        dbManager.open();
        // Parametro guztiak pasatzen dizkiogu DataAccess-i
        Sale s = dbManager.createSale2(title, description, numStatus, price, date, sellerEmail, image, parent);
        dbManager.close();
        return s;
    }
    
    @WebMethod
    public boolean deleteDemand(Demand d) {
        dbManager.open();
        boolean ondo = false;
        try {
            ondo = dbManager.deleteDemand(d);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            dbManager.close();
        }
        return ondo;
    }
}