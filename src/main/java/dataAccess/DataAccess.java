package dataAccess;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import configuration.ConfigXML;
import configuration.UtilDate;
import domain.Seller;
import domain.Transaction;
import domain.Reclamation;
import domain.Sale;
import exceptions.FileNotUploadedException;
import exceptions.MustBeLaterThanTodayException;
import exceptions.SaleAlreadyExistException;
import domain.BlacklistedUser;
import domain.Demand;
public class DataAccess  {
	private  EntityManager  db;
	private  EntityManagerFactory emf;
	private static final int baseSize = 160;
	private static final String basePath="src/main/resources/images/";

	ConfigXML c=ConfigXML.getInstance();

	public DataAccess() {
		open();
		try {
			// Begiratu ea lehen saltzailea existitzen den
			Seller s = db.find(Seller.class, "seller1@gmail.com");

			// Existitzen ez bada bakarrik hasieratu
			if (s == null && c.isDatabaseInitialized()) {
				initializeDB();
			}
		} catch (Exception e) {
			// Errorearen bat badago find egitean (adibidez, taula oraindik ez bada existitzen)
			if (c.isDatabaseInitialized()) {
				initializeDB();
			}
		}
		System.out.println("DataAccess created => isDatabaseInitialized: " + c.isDatabaseInitialized());
		close();
	}

	public DataAccess(EntityManager db) {
		this.db=db;
	}

	public void initializeDB(){
		db.getTransaction().begin();
		try { 
			Seller seller1 = new Seller("seller1@gmail.com", "Aitor Fernandez", "123");
			Seller seller2 = new Seller("seller22@gmail.com", "Ane Gaztañaga", "abc");
			Seller seller3 = new Seller("seller3@gmail.com", "Test Seller", "test");

			Date today = UtilDate.trim(new Date());

			seller1.addSale("futbol baloia", "oso polita, gutxi erabilita", 10, 2,  today, null);
			seller1.addSale("salomon mendiko botak", "44 zenbakia, 3 ateraldi",20,  2,  today, null);
			seller1.addSale("samsung 42\" telebista", "berria, erabili gabe", 175, 1,  today, null);

			seller2.addSale("imac 27", "7 urte, dena ondo dabil", 1, 200,today, null);
			seller2.addSale("iphone 17", "oso gutxi erabilita", 2, 400, today, null);
			seller2.addSale("orbea mendiko bizikleta", "29\" 10 urte, mantenua behar du", 3,225, today, null);
			seller2.addSale("polar kilor erlojua", "Vantage M, ondo dago", 3, 30, today, null);

			seller3.addSale("sukaldeko mahaia", "1.8*0.8, 4 aulkiekin. Prezio finkoa", 3,45, today, null);

			db.persist(seller1);
			db.persist(seller2);
			db.persist(seller3);

			db.getTransaction().commit();
			System.out.println("Db initialized");
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	public Sale createSale(String title, String description, int status, float price,  Date pubDate, String sellerEmail, File file) throws  FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
		if (title == null || pubDate == null || sellerEmail == null) {
			return null; 
		}
		try {
			if(pubDate.before(UtilDate.trim(new Date()))) {
				throw new MustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorSaleMustBeLaterThanToday"));
			}
			if (file==null)
				throw new FileNotUploadedException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorFileNotUploadedException"));

			db.getTransaction().begin();
			Seller seller = db.find(Seller.class, sellerEmail);
			if (seller.doesSaleExist(title)) {
				db.getTransaction().commit();
				throw new SaleAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.SaleAlreadyExist"));
			}

			Sale sale = seller.addSale(title, description, status, price, pubDate, file);
			db.persist(seller); 
			db.getTransaction().commit();
			return sale;
		} catch (NullPointerException e) {
			e.printStackTrace();
			if (db.getTransaction().isActive()) db.getTransaction().rollback();
			return null;
		}
	}
	
	public Sale createSale2(String title, String description, int status, float price, Date pubDate, String sellerEmail, File file, Demand parentDemand) 
	        throws FileNotUploadedException, MustBeLaterThanTodayException, SaleAlreadyExistException {
	    
	    if (title == null || pubDate == null || sellerEmail == null) return null;

	    try {
	        if(pubDate.before(UtilDate.trim(new Date()))) {
	            throw new MustBeLaterThanTodayException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorSaleMustBeLaterThanToday"));
	        }
	        if (file == null)
	            throw new FileNotUploadedException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.ErrorFileNotUploadedException"));

	        db.getTransaction().begin();
	        Seller seller = db.find(Seller.class, sellerEmail);
	        
	        if (seller.doesSaleExist(title)) {
	            db.getTransaction().commit();
	            throw new SaleAlreadyExistException(ResourceBundle.getBundle("Etiquetas").getString("DataAccess.SaleAlreadyExist"));
	        }

	        // Salmenta sortu
	        Sale sale = seller.addSale(title, description, status, price, pubDate, file);
	        
	        // Eskaera (parent) badago, lotu
	        if (parentDemand != null) {
	            Demand d = db.find(Demand.class, parentDemand.getDemandNumber());
	            sale.setParentDemand(d); 
	        }

	        db.persist(seller); 
	        db.getTransaction().commit();
	        return sale;
	        
	    } catch (Exception e) {
	        if (db.getTransaction().isActive()) db.getTransaction().rollback();
	        throw e; // Errorea gorantz bota GUI-ak kudeatu dezan
	    }
	}
	
	

	public List<Sale> getSales(String desc) {
	    if (desc == null) return new ArrayList<Sale>();

	    
	    TypedQuery<Sale> query = db.createQuery(
	            "SELECT s FROM Sale s WHERE s.title LIKE ?1 AND s.sold = false AND s.available = true", Sale.class);   
	    query.setParameter(1, "%" + desc + "%");
	    return query.getResultList();
	}

	public List<Sale> getPublishedSales(String desc, Date pubDate) {
	    List<Sale> res = new ArrayList<Sale>(); 
	    
	    TypedQuery<Sale> query = db.createQuery(
	            "SELECT s FROM Sale s WHERE s.title LIKE ?1 AND s.pubDate <= ?2 AND s.sold = false AND s.available = true", Sale.class); 
	    query.setParameter(1, "%"+desc+"%");
	    query.setParameter(2, pubDate);
	    List<Sale> sales = query.getResultList();
	    for (Sale sale : sales) res.add(sale);
	    return res;
	}

	public void open(){
		String fileName=c.getDbFilename();
		if (c.isDatabaseLocal()) {
			emf = Persistence.createEntityManagerFactory("objectdb:"+fileName);
			db = emf.createEntityManager();
		} else {
			Map<String, String> properties = new HashMap<String, String>();
			properties.put("javax.persistence.jdbc.user", c.getUser());
			properties.put("javax.persistence.jdbc.password", c.getPassword());
			emf = Persistence.createEntityManagerFactory("objectdb://"+c.getDatabaseNode()+":"+c.getDatabasePort()+"/"+fileName, properties);
			db = emf.createEntityManager();
		}
	}

	public BufferedImage getFile(String fileName) {
		File file=new File(basePath+fileName);
		BufferedImage targetImg=null;
		try {
			targetImg = rescale(ImageIO.read(file));
		} catch (IOException ex) { }
		return targetImg;
	}

	public BufferedImage rescale(BufferedImage originalImage) {
		BufferedImage resizedImage = new BufferedImage(baseSize, baseSize, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = resizedImage.createGraphics();
		g.drawImage(originalImage, 0, 0, baseSize, baseSize, null);
		g.dispose();
		return resizedImage;
	}

	public void close(){
		db.close();
		System.out.println("DataAcess closed");
	}

	public Seller isLogin(String email, String password) {
		if (email == null || password == null) return null;

		TypedQuery<Seller> query = db.createQuery(
				"SELECT s FROM Seller s WHERE s.email=?1 AND s.password=?2", Seller.class);
		query.setParameter(1, email);
		query.setParameter(2, password);
		try {
			return query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}

	public Seller register(String email, String name, String password) {

		if (email == null || name == null || password == null) return null;

		db.getTransaction().begin();
		Seller s = db.find(Seller.class, email);
		if (s != null) {
			db.getTransaction().commit(); 
			return null; 
		}
		Seller newSeller = new Seller(email, name, password);
		db.persist(newSeller);
		db.getTransaction().commit();
		return newSeller;
	}

	public boolean removeSale(String email, Sale sale) {
		// 3. puntua: Balidazioa (null-ak ekiditeko)
		if (email == null || sale == null) return false;

		db.getTransaction().begin();
		try {
			Seller seller = db.find(Seller.class, email);
			Sale s = db.find(Sale.class, sale.getSaleNumber());

			if (seller != null && s != null && s.getSeller().getEmail().equals(email)) {

				// 1. puntua: Erosita badago, ezin da ezabatu
				if (s.isSold()) {
					System.out.println("ERROREA: Ezin da ezabatu, produktua jada salduta dago.");
					db.getTransaction().rollback();
					return false; 
				}

				seller.getSales().remove(s);
				db.remove(s);
				db.getTransaction().commit();
				return true;
			}
			db.getTransaction().commit();
			return false;
		} catch (Exception e) {
			if (db.getTransaction().isActive()) db.getTransaction().rollback();
			return false;
		}
	}
	public boolean buySale(int saleNumber, String buyerEmail) {
	    if (saleNumber < 0 || buyerEmail == null) return false;

	    db.getTransaction().begin();
	    try {
	        Sale s = db.find(Sale.class, saleNumber);
	        Seller buyer = db.find(Seller.class, buyerEmail);

	        // Ziurtatu produktua eta eroslea existitzen direla, eta ez dagoela salduta
	        if (s != null && buyer != null && !s.isSold()) {
	            
	            // Diru nahikoa duen egiaztatu
	            if (buyer.getDirua() >= s.getPrice()) {

	                // 1. EROSLEARI dirua kendu eta transakzioa sortu
	                buyer.setDirua(buyer.getDirua() - s.getPrice());
	                Transaction tErosle = new Transaction("EROSKETA", s.getTitle() + " erosi da", s.getPrice(), new Date());
	                buyer.addTransaction(tErosle);
	                db.persist(tErosle);

	                // 2. SALTZAILEARI dirua gehitu eta transakzioa sortu
	                Seller saltzailea = s.getSeller();
	                if (saltzailea != null) {
	                    saltzailea.setDirua(saltzailea.getDirua() + s.getPrice());
	                    Transaction tSaltzaile = new Transaction("SALMENTA", s.getTitle() + " saldu da", s.getPrice(), new Date());
	                    saltzailea.addTransaction(tSaltzaile);
	                    saltzailea.addSaldutakoa(s);
	                    db.persist(tSaltzaile);
	                }

	                // 3. PRODUKTUA EGUNERATU
	                s.setSold(true);
	                s.setAvailable(false); // Ez dadila bilaketetan agertu
	                s.setBuyer(buyer);
	                buyer.addErosketa(s);

	                // 4. SASKITIK KENDU (Gakoa saskian ez geratzeko)
	                // Erosleak produktua saskian baldin badu, ezabatu egingo dugu
	                if (buyer.getBasket().contains(s)) {
	                    buyer.getBasket().remove(s);
	                }

	                // Dena ondo, gorde aldaketak
	                db.getTransaction().commit();

	                // Objektuak freskatu memoriarako datu eguneratuak izan ditzaten
	                db.refresh(buyer);
	                if (s.getSeller() != null) db.refresh(s.getSeller());

	                return true;
	            }
	        }
	        
	        // Baldintzaren bat bete ez bada, atzera egin
	        if (db.getTransaction().isActive()) db.getTransaction().rollback();
	        return false;

	    } catch (Exception e) {
	        // Errore baten aurrean transakzioa bertan behera utzi
	        if (db.getTransaction().isActive()) db.getTransaction().rollback();
	        e.printStackTrace();
	        return false;
	    }
	}
	public boolean createReclamation(int saleNumber, String deskribapena, String buyerEmail) {
		if (deskribapena == null || buyerEmail == null) return false;

		db.getTransaction().begin();
		try {
			// 1. Bilatu datu-basean beharrezko objektuak
			Sale s = db.find(Sale.class, saleNumber);
			Seller eroslea = db.find(Seller.class, buyerEmail);

			if (s != null && eroslea != null) {
				Seller saltzailea = s.getSeller();

				// 2. Erreklamazioa sortu (4 parametro: deskribapena, produktua, saltzailea, eroslea)
				domain.Reclamation r = new domain.Reclamation(deskribapena, s, saltzailea, eroslea);

				// 3. Loturak egin objektuen artean
				s.setReclamation(r); // Produktuak badaki erreklamazio bat duela
				saltzailea.addErreklamazioa(r); // Saltzaileak badaki erreklamazio bat jaso duela

				// 4. Gorde erreklamazioa datu-basean
				db.persist(r);

				db.getTransaction().commit();
				db.refresh(saltzailea);
				return true;
			}

			if (db.getTransaction().isActive()) db.getTransaction().rollback();
			return false;

		} catch (Exception e) {
			if (db.getTransaction().isActive()) db.getTransaction().rollback();
			e.printStackTrace();
			return false;
		}
	}
	
	

	
	public Seller getUser(String email) {
		TypedQuery<Seller> query = db.createQuery(
		        "SELECT DISTINCT s FROM Seller s WHERE s.email = :email", Seller.class);
		    query.setParameter("email", email);
		    List<Seller> results = query.getResultList();
		    return results.isEmpty() ? null : results.get(0);
	}
	public void updateSaldo(String email, float saldoBerria) {
		try {
			db.getTransaction().begin();
			Seller s = db.find(Seller.class, email);
			if (s != null) {
				// 1. Kalkulatu aldea jakiteko sarrera ala irteera den
				float aldea = saldoBerria - s.getDirua();
				String mota = (aldea > 0) ? "SARRERA" : "IRTEERA";
				String kontzeptua = (aldea > 0) ? "Dirua kargatu da" : "Dirua atera da";

				// 2. Transakzio objektua sortu
				// Kantitatea positiboa gorde nahi badugu beti: Math.abs(aldea)
				Transaction t = new Transaction(mota, kontzeptua, Math.abs(aldea), new Date());

				// 3. Seller-ari gehitu eta saldoa eguneratu
				s.addTransaction(t);
				s.setDirua(saldoBerria);

				// 4. persist egin behar da transakzio berria denez
				db.persist(t); 

				db.getTransaction().commit();
			} else {
				db.getTransaction().rollback();
			}
		} catch (Exception e) {
			if (db.getTransaction().isActive()) db.getTransaction().rollback();
			e.printStackTrace();
		}
	}
	public List<domain.Reclamation> getReclamations(String email) {
		if (email == null) return new ArrayList<domain.Reclamation>();

		// Saltzailea bilatu datu-basean
		Seller s = db.find(Seller.class, email);

		if (s != null) {
			// Seller klasean daukagun zerrenda bueltatu
			return s.getJasoatakoErreklamazioak();
		}

		return new ArrayList<domain.Reclamation>();
	}

	public List<Reclamation> getAllReclamations() {
	    TypedQuery<Reclamation> query = db.createQuery("SELECT r FROM Reclamation r", Reclamation.class);
	    return query.getResultList();
	}
	
	public void updateReclamationStatus(int reclamationNumber, String newStatus, String soluzioa) {
	    db.getTransaction().begin();
	    try {
	        Reclamation r = db.find(Reclamation.class, reclamationNumber);
	        if (r != null) {
	            r.setEgoera(newStatus); // Ziurtatu Reclamation klasean setEgoera metodoa duzula
	            r.setSoluzioa(soluzioa); // Soluzioa gordetzen dugu
	            db.getTransaction().commit();
	        }
	    } catch (Exception e) {
	        db.getTransaction().rollback();
	        e.printStackTrace();
	    }
	}
	// --- SASKIAREN LOGIKA BERRIA ---

    /**
     * Produktu bat erabiltzailearen saskian gehitzen du.
     */
	public boolean addToBasket(String buyerEmail, int saleNumber) {
	    db.getTransaction().begin();
	    try {
	        Seller buyer = db.find(Seller.class, buyerEmail);
	        Sale s = db.find(Sale.class, saleNumber);

	        if (buyer != null && s != null && !s.isSold()) {
	            buyer.addBasket(s); // Orain Seller-ek mugarik gabe gehitzen du
	            db.getTransaction().commit();
	            return true;
	        }
	        if (db.getTransaction().isActive()) db.getTransaction().rollback();
	        return false;
	    } catch (Exception e) {
	        if (db.getTransaction().isActive()) db.getTransaction().rollback();
	        return false;
	    }
	}

    /**
     * Erabiltzailearen saskia husten du.
     */
    public void clearBasket(String email) {
        db.getTransaction().begin();
        try {
            Seller s = db.find(Seller.class, email);
            if (s != null) {
                s.clearBasket();
                db.getTransaction().commit();
            }
        } catch (Exception e) {
            if (db.getTransaction().isActive()) db.getTransaction().rollback();
        }
    }

    /**
     * Saskian dauden produktu guztiak transakzio bakar batean erosten ditu.
     */
    public boolean buyBasket(String buyerEmail) {
        db.getTransaction().begin();
        try {
            Seller buyer = db.find(Seller.class, buyerEmail);
            if (buyer == null || buyer.getBasket().isEmpty()) return false;

            List<Sale> saskia = new ArrayList<>(buyer.getBasket());
            float prezioTotala = 0;
            for (Sale s : saskia) prezioTotala += s.getPrice();

            // 1. Dirua egiaztatu
            if (buyer.getDirua() < prezioTotala) {
                db.getTransaction().rollback();
                return false;
            }

            // 2. Produktu bakoitza prozesatu
            for (Sale s : saskia) {
                // Datu-basetik freskatu azken momentuan norbaitek erosi duen ikusteko
                Sale dbSale = db.find(Sale.class, s.getSaleNumber());
                if (dbSale == null || dbSale.isSold()) {
                    db.getTransaction().rollback();
                    return false;
                }

                // EROSLEAREN ALDEKOAK
                buyer.setDirua(buyer.getDirua() - dbSale.getPrice());
                Transaction tErosle = new Transaction("EROSKETA", dbSale.getTitle() + " (Saskia)", dbSale.getPrice(), new Date());
                buyer.addTransaction(tErosle);
                db.persist(tErosle);

                // SALTZAILEAREN ALDEKOAK
                Seller saltzailea = dbSale.getSeller();
                saltzailea.setDirua(saltzailea.getDirua() + dbSale.getPrice());
                Transaction tSaltzaile = new Transaction("SALMENTA", dbSale.getTitle() + " saldua", dbSale.getPrice(), new Date());
                saltzailea.addTransaction(tSaltzaile);
                saltzailea.addSaldutakoa(dbSale);
                db.persist(tSaltzaile);

                // PRODUKTUA EGUNERATU
                dbSale.setSold(true);
                dbSale.setBuyer(buyer);
                buyer.addErosketa(dbSale);
            }

            // 3. Saskia garbitu eta commit
            buyer.clearBasket();
            db.getTransaction().commit();
            
            // Objektuak freskatu memoriarako
            db.refresh(buyer);
            return true;

        } catch (Exception e) {
            if (db.getTransaction().isActive()) db.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }
    }
    
 // --- KONTUA ITXI ETA ZERRENDA BELTZA LOGIKA ---

    /**
     * Kontu bat itxi: Erabiltzailea sistematik ezabatzen du 
     * eta bere helbide elektronikoa zerrenda beltzean sartzen du.
     */
    public boolean kontuaItxi(String email) {
        if (email == null) return false;
        
        db.getTransaction().begin();
        try {
            Seller s = db.find(Seller.class, email);
            
            if (s != null) {
                // 2. Eskaerak (Demands) ezabatu 
                try {
                    TypedQuery<Demand> queryD = db.createQuery("SELECT d FROM Demand d WHERE d.claimant.email = :email", Demand.class);
                    queryD.setParameter("email", email);
                    List<Demand> demands = queryD.getResultList();
                    for (Demand d : demands) {
                        db.remove(d);
                    }
                } catch (Exception e) {
                    System.out.println("Oharra: Ez da eskaerarik aurkitu");
                }

                // 3. Produktuak (Sale) desgaitu BAINA EZ EZABATU
                // Bakarrik desgaitu eroslearen historian datuak gera daitezen
                try {
                    for (Sale sale : s.getSales()) {
                        sale.setAvailable(false); // Ez da dendan agertuko, baina DBan jarraituko du
                    }
                } catch (Exception e) {
                    System.out.println("Oharra: Errorea produktuak desgaitzean");
                }
                
                // 4. Erabiltzailea "desgaitu"
                s.setAktibo(false);
                
                // 5. Zerrenda beltzera gehitu
                BlacklistedUser bu = new BlacklistedUser(email);
                db.persist(bu);
 
                db.getTransaction().commit();
                return true;
            } else {
                if (db.getTransaction().isActive()) db.getTransaction().rollback();
                return false;
            }
        } catch (Exception e) {
            if (db.getTransaction().isActive()) db.getTransaction().rollback();
            e.printStackTrace();
            return false;
        }   
    }

    /**
     * Egiaztatu email bat zerrenda beltzean dagoen.
     */
    public boolean isBlacklisted(String email) {
    	if (email == null) return false;
        // ObjectDB-n find bidez bilatzen dugu entitate espezifikoa
        BlacklistedUser bu = db.find(BlacklistedUser.class, email);
        return bu != null;
    }

    /**
     * Administratzaileak ikusteko erabiltzaile (Seller) guztien zerrenda.
     */
    public List<Seller> getAllSellers() {
        TypedQuery<Seller> query = db.createQuery("SELECT s FROM Seller s WHERE s.aktibo = true OR s.aktibo IS NULL", Seller.class);
        return query.getResultList();
    }
    
    /**
     * Zerrenda beltzean dauden email guztiak lortzeko.
     */
    public List<BlacklistedUser> getBlacklistedUsers() {
        TypedQuery<BlacklistedUser> query = db.createQuery("SELECT b FROM BlacklistedUser b", BlacklistedUser.class);
        return query.getResultList();
    }
    
    public List<Seller> getActiveSellers(String filter) {
        // Query honek produktu libreak dituzten saltzaileak lortzen ditu
        // Erabiltzailearen izenagatik edo emailagatik filtratuz
        TypedQuery<Seller> query = db.createQuery(
            "SELECT DISTINCT s.seller FROM Sale s " +
            "WHERE s.sold = false AND s.available = true " +
            "AND (s.seller.name LIKE ?1 OR s.seller.email LIKE ?1)", Seller.class);
        query.setParameter(1, "%" + filter + "%");
        return query.getResultList();
    }
    
    public void removeFromBasket(String email, Integer saleId) {
        // Erabiltzailea eta Produktua aurkitu DBan
        Seller user = db.find(Seller.class, email);
        Sale sale = db.find(Sale.class, saleId);
        
        if (user != null && sale != null) {
            db.getTransaction().begin();
            // Saskiko zerrendatik (List) objektua ezabatu
            user.getBasket().remove(sale); 
            db.getTransaction().commit();
        }
    }
    
    public Demand createDemand(String title, String description, float maxPrice, String claimantEmail) {
        if (claimantEmail == null || title == null) return null;
        
        db.getTransaction().begin();
        try {
            Seller claimant = db.find(Seller.class, claimantEmail);
            if (claimant == null) {
                db.getTransaction().rollback();
                return null;
            }

            // 1. Gure Seller-eko metodoari deitu objektua sortzeko
            Demand demand = claimant.addDemand(title, description, maxPrice);
            
            // 2. GAKOA: ObjectDB-ri esan Demand berri hau DB-an persistitu behar duela.
            // Honek "Attempt to persist a reference to a non managed domain.Demand" errorea konpontzen du.
            db.persist(demand); 
            
            // 3. Seller-a eguneratu bere zerrenda berriarekin
            db.persist(claimant); 

            db.getTransaction().commit();
            return demand;
            
        } catch (Exception e) {
            if (db.getTransaction().isActive()) db.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }

    public Sale createOffer(String title, float price, String description, Seller seller, Demand demand) {
        db.getTransaction().begin();
        try {
            Demand d = db.find(Demand.class, demand.getDemandNumber());
            Seller s = db.find(Seller.class, seller.getEmail());

            Sale offer = new Sale(); 
            offer.setTitle(title);
            offer.setPrice(price);
            offer.setDescription(description);
            offer.setSeller(s);
            offer.setPublicationDate(new java.util.Date());
            offer.setParentDemand(d);

            db.persist(offer);
            
            
            s.addSale(offer); 

            db.getTransaction().commit();
            return offer;
            
        } catch (Exception e) {
            if (db.getTransaction().isActive()) db.getTransaction().rollback();
            e.printStackTrace();
            return null;
        }
    }

    // OfertakGUI-ak behar duen kontsulta
    public List<Sale> getOffersForDemand(Demand d) {
        if (d == null) return new ArrayList<Sale>();
        // HEMEN: db objektua erabili aurretik irekita egon behar da (Fasadak ireki duena)
        TypedQuery<Sale> query = db.createQuery(
            "SELECT s FROM Sale s WHERE s.parentDemand.demandNumber = :dNum", Sale.class);
        query.setParameter("dNum", d.getDemandNumber());
        return query.getResultList();
    }
    
    public List<Demand> getDemandsByEmail(String email) {
        Seller s = db.find(Seller.class, email);
        if (s != null) {
            db.refresh(s); // Datu-baseko azken egoera ekartzen du (38. demand hori barne)
            return s.getDemands(); 
        }
        return new ArrayList<Demand>();
    }
    
    public List<Demand> getAllDemands() {
        TypedQuery<Demand> query = db.createQuery("SELECT d FROM Demand d", Demand.class);
        return query.getResultList();
    }
    
    public boolean deleteDemand(Demand d) {
        try {
            db.getTransaction().begin();
            
            // Objektua freskatu konexioan egon dadin
            Demand managedDemand = db.find(Demand.class, d.getDemandNumber());
            
            if (managedDemand != null) {
                // Eskaintza (Sale) lotuak baditu, null jarri behar zaie parentDemand-en
                TypedQuery<Sale> query = db.createQuery("SELECT s FROM Sale s WHERE s.parentDemand = :demand", Sale.class);
                query.setParameter("demand", managedDemand);
                List<Sale> linkedSales = query.getResultList();
                
                for (Sale s : linkedSales) {
                    s.setParentDemand(null);
                }
                
                // Jabearen (Seller) zerrendatik kendu
                Seller s = managedDemand.getClaimant();
                if (s != null) {
                    s.getDemands().remove(managedDemand);
                }
                
                db.remove(managedDemand);
                db.getTransaction().commit();
                return true;
            }
        } catch (Exception e) {
            if (db.getTransaction().isActive()) db.getTransaction().rollback();
            e.printStackTrace();
        }
        return false;
    }
}