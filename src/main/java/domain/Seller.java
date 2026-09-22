package domain;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;

@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Seller implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@XmlID
	@Id 
	private String email;
	private String name;
	private String password;
	private float dirua; 

	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	private List<Sale> sales = new ArrayList<Sale>();
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private Set<Sale> erosketak = new HashSet<Sale>();

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Sale> saldutakoak = new ArrayList<Sale>();

	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Transaction> transactions = new ArrayList<Transaction>();
	
	@XmlIDREF
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
	private List<Reclamation> jasoatakoErreklamazioak = new Vector<Reclamation>();

	// --- BERRIA: SASKIA ---
	@XmlIDREF
	@ManyToMany(fetch=FetchType.EAGER)
	private List<Sale> basket = new ArrayList<Sale>();
	
	public Seller() {
		super();
	}

	public Seller(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
	
	// --- GETTER eta SETTER-ak ---

	public List<Sale> getSales() { return sales; }
	public float getDirua() { return dirua; }
	public void setDirua(float dirua) { this.dirua = dirua; }
    
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }
	
	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
		
	public Set<Sale> getErosketak() { return erosketak; }
	public void addErosketa(Sale s) { this.erosketak.add(s); }

	public List<Sale> getSaldutakoak() { return saldutakoak; }
	public void addSaldutakoa(Sale s) { this.saldutakoak.add(s); }

	public List<Transaction> getTransactions() { return transactions; }
	public void addTransaction(Transaction t) { this.transactions.add(t); }

	// --- SASKIAREN LOGIKA (BERRIA) ---

	public List<Sale> getBasket() { return basket; }

	/**
	 * Saskira produktu bat gehitzen du. 
	 * GARRANTZITSUA: Irakasleak esandakoa betetzen du, 
	 * saltzailea ezberdina bada Exception bat jaurtiko du.
	 */
	public void addBasket(Sale s) {
	    if (s == null) return;
	    if (!basket.contains(s)) {
	        basket.add(s);
	    }
	}

	public void removeFromBasket(Sale s) {
		basket.remove(s);
	}

	public void clearBasket() {
		basket.clear();
	}

	// --- ERREKLAMAZIOAK ---

	public List<Reclamation> getJasoatakoErreklamazioak() {
        return jasoatakoErreklamazioak;
    }

	public void setJasoatakoErreklamazioak(List<Reclamation> jasoatakoErreklamazioak) {
        this.jasoatakoErreklamazioak = jasoatakoErreklamazioak;
    }

	public void addErreklamazioa(Reclamation r) {
        if (this.jasoatakoErreklamazioak == null) {
            this.jasoatakoErreklamazioak = new Vector<Reclamation>();
        }
        this.jasoatakoErreklamazioak.add(r);
    }

	// --- BESTE METODOAK ---

	public Sale addSale(String title, String description, int status, float price, Date pubDate, File file) {
		Sale sale = new Sale(title, description, status, price, pubDate, file, this);
        sales.add(sale);
        return sale;
	}

	public boolean doesSaleExist(String title) {	
		for (Sale s : sales) {
			if (s.getTitle().equals(title)) return true;
		}
		return false;
	}
		
	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Seller other = (Seller) obj;
		if (email == null) return other.email == null;
		return email.equals(other.email);
	}

	@Override
	public String toString() {
		return email + ";" + name;
	}
	
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Demand> demands = new ArrayList<Demand>();

	// Seller.java klasean
	public Demand addDemand(String title, String description, float maxPrice) {
	    if (this.demands == null) {
	        this.demands = new Vector<Demand>();
	    }
	    Demand d = new Demand(title, description, maxPrice, this);
	    this.demands.add(d);
	    return d;
	}

	public List<Demand> getDemands() {
	    return demands;
	}
	
	// Seller.java barruan
	public void addSale(Sale s) {
	    this.saldutakoak.add(s); // 'saldutakoak' gure salmenten zerrenda (List<Sale>) izan behar da
	}
	
	private boolean aktibo = true;
	
	public boolean isAktibo() {
		return aktibo;
	}
	
	public void setAktibo(boolean aktibo) {
		this.aktibo = aktibo;
	}
}