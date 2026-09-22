package businessLogic;

import java.io.File;
import java.util.Date;
import java.util.List;

import domain.BlacklistedUser;
import domain.Demand;
import domain.Reclamation;
import domain.Sale;
import domain.Seller; //  Seller inportatu

import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.awt.image.BufferedImage;
import java.awt.Image;

import gui.*;
/**
 * Interface that specifies the business logic.
 */
@WebService
public interface BLFacade  {


	/**
	 * This method creates/adds a product to a seller
	 * 
	 * @param title of the product
	 * @param description of the product
	 * @param status 
	 * @param selling price
	 * @param category of a product
	 * @param publicationDate
	 * @return Sale
	 */
	@WebMethod
	public Sale createSale(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException;


	/**
	 * This method retrieves the products that contain desc
	 * 
	 * @param desc the text to search
	 * @return collection of sales that contain desc 
	 */
	@WebMethod public List<Sale> getSales(String desc);

	/**
	 * 	 * This method retrieves the products that contain a desc text in a title and the publicationDate today or before
	 * 
	 * @param desc the text to search
	 * @param pubDate the date  of the publication date
	 * @return collection of sales that contain desc and published before pubDate
	 */
	@WebMethod public List<Sale> getPublishedSales(String desc, Date pubDate);


	/**
	 * This method calls the data access to initialize the database with some sellers and products.
	 * It is only invoked  when the option "initialize" is declared in the tag dataBaseOpenMode of resources/config.xml file
	 */	
	@WebMethod public void initializeBD();


	@WebMethod public Image downloadImage(String imageName);



	// LOGIN ETA REGISTER METODO BERRIAK

	/**
	 * Kredentzialak zuzenak badira Seller itzultzen du, bestela null.
	 */
	@WebMethod public Seller isLogin(String email, String password);

	/**
	 * Seller berri bat erregistratzen du.
	 */
	@WebMethod public Seller register(String email, String name, String password);
	
	//Produktu bat ezabatzeko
	
	// ... lehendik dituzun metodoak ...

    /**
     * Produktu bat datu-basetik kentzen du.
     * Segurtasun gisa, saltzailearen emaila eta produktua konparatzen dira
     * jabea dela ziurtatzeko.
     * * @param email Saioa hasita duen erabiltzailearen posta
     * @param sale Ezabatu nahi den produktuaren objektua
     * @return true ondo ezabatu bada, false bestela
     */
    @WebMethod 
    public boolean removeSale(String email, Sale sale);
    
    /**
	 * Salmenta bat erosten du, bere egoera 'salduta' gisa markatuz.
	 * Irakaslearen oharra: Egiaztatu behar da salmenta existitzen dela eta ez dagoela salduta.
	 * * @param saleNumber Erosi nahi den salmentaren identifikazio zenbakia
	 * @return true erosketa ondo burutu bada, false bestela
	 */
	@WebMethod 
	public boolean buySale(int saleNumber, String buyerEmail);
	
	@WebMethod 
	public void updateSaldo(String email, float saldoBerria);
	
	/**
     * Produktu baten erosketa prozesu osoa kudeatzen du.
     */
    @WebMethod 
    public boolean erosketaBurutu(Seller eroslea, Sale produktua);
    
    @WebMethod 
    public Seller getUser(String email);
    
    /**
     * Erreklamazio berri bat sortzen du eta produktuarekin zein saltzailearekin lotzen du.
     * * @param deskribapena Kexaren testua
     * @param saleNumber Lotutako produktuaren id-a
     * @param buyerEmail Kexa jartzen duen eroslearen posta
     * @return true ondo gorde bada
     */
    @WebMethod 
    public boolean createReclamation(int saleNumber, String deskribapena, String buyerEmail);

    /**
     * Saltzaile baten erreklamazio guztiak berreskuratzen ditu (Egoera eguneratuak ikusteko).
     * * @param email Saltzailearen posta
     * @return Erreklamazio zerrenda
     */
    @WebMethod 
    public List<domain.Reclamation> getReclamations(String email);
    
    @WebMethod 
    public List<Reclamation> getAllReclamations();
    
    @WebMethod 
    public void updateReclamationStatus(int reclamationNumber, String newStatus, String soluzioa);

    public boolean addToBasket(String buyerEmail, int saleNumber);
    public void clearBasket(String email);
    public boolean buyBasket(String buyerEmail);
    public boolean isBlacklisted(String email);
    
    /**
     * Kontua itxi, erabiltzailea desgaitu eta zerrenda beltzera gehituz.
     */
    public boolean kontuaItxi(String email);
    
    /**
     * Datu-basean dauden saltzaile (Seller) guztiak lortzen ditu.
     * @return Seller objektuen zerrenda.
     */
    public List<Seller> getAllSellers();
    public List<BlacklistedUser> getBlacklistedUsers();
    
    /**
     * Produktu aktiboak dituzten saltzaileen zerrenda lortzen du.
     */
    public List<Seller> getActiveSellers(String filter);

    /**
     * Saltzaile zehatz baten produktu erabilgarriak lortzen ditu.
     */
    public List<Sale> getSalesBySeller(String sellerEmail);
    
    public void removeFromBasket(String email, Integer saleId);
    @WebMethod 
    Demand createDemand(String title, String description, float maxPrice, String claimantEmail);
    @WebMethod 
    public List<Sale> getOffersForDemand(Demand d);

    @WebMethod
    public Sale createOffer(String title, float price, String description, String sellerEmail, Demand d);
    
 
    public List<Demand> getAllDemands();

    // Eguneratu createSale sinadura (Demand parent gehitu amaieran)
    public Sale createSale(String title, String description, int numStatus, float price, Date date, String sellerEmail, File image, Demand parent) throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException;
    
    public boolean deleteDemand(Demand d);
}