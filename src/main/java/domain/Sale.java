package domain;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Sale implements Serializable {
	@XmlID
	@Id 
	@XmlJavaTypeAdapter(IntegerAdapter.class)
	@GeneratedValue
	private Integer saleNumber;
	private String title;
	private String description;
	private int  status;
	private float price;
	private Date pubDate;
	private String fileName;
	
	private boolean sold = false; 
    
    // BERRIA: Produktua merkatutik kendua izan den ala ez
    private boolean available = true; 
	
	@XmlIDREF
    @ManyToOne(fetch=FetchType.EAGER)
    private Seller seller; 

    @XmlIDREF
    @ManyToOne(fetch=FetchType.EAGER)
    private Seller buyer; 
    
    @XmlIDREF
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private Reclamation reclamation; 
    
	public Sale(){
		super();
	}
		
	public Sale(String title, String description, int status, float price, Date pubDate, File file, Seller seller) {
		super();

		this.title = title;
		this.description = description;
		this.status = status;
		this.price=price;
		this.pubDate=pubDate;
        this.available = true; // Hasieran salgai dago
        
		if (file!=null) {
		    this.fileName=file.getName();
			try {
				BufferedImage img1 = ImageIO.read(file);
				String path="src/main/resources/images/";
				File outputfile = new File(path+file.getName());
			    ImageIO.write(img1, "png", outputfile); 
			} catch(IOException ex) {
				// Errore kudeaketa
		    }
		}

		this.seller = seller;
	}
	
	public Integer getSaleNumber() { return saleNumber; }
	public Seller getBuyer() { return buyer; }
    public void setBuyer(Seller buyer) { this.buyer = buyer; }
    public Reclamation getReclamation() { return reclamation; }
    public void setReclamation(Reclamation reclamation) { this.reclamation = reclamation; }
	
	/**
	 * ZUZENDUTA: Salduta dagoela joko dugu boolean-a true bada EDO buyer bat badauka.
	 */
	public boolean isSold() {
	    return sold || buyer != null;
	}

	public void setSold(boolean sold) {
	    this.sold = sold;
	}

    // BERRIAK: Available kudeatzeko (erabiltzaileak produktua ezabatu nahi badu)
    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
	
	public void setSaleNumber(Integer saleNumber) { this.saleNumber = saleNumber; }
	public String getTitle() { return title; }
	public void setTitle(String title) { this.title = title; }
	public String getDescription() { return description; }
	public void setDescription(String description) { this.description = description; }
	public int getStatus() { return status; }
	public void setStatus(int status) { this.status = status; }
	public float getPrice() { return price; }
	public void setPrice(float price) { this.price = price; }
	public Date getPublicationDate() { return pubDate; }
	public void setPublicationDate(Date publicationDate) { this.pubDate = publicationDate; }
	public Seller getSeller() { return seller; }
	public void setSeller(Seller seller) { this.seller = seller; }
	public String getFile() { return fileName; }
	
	public String toString(){
		return saleNumber+";"+title+";"+price;  
	}

    // GARRANTZITSUA SASKIARENTZAT: equals metodoa ziurtatzeko saskian produktu bera bi aldiz ez sartzea
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Sale other = (Sale) obj;
        if (saleNumber == null) return other.saleNumber == null;
        return saleNumber.equals(other.saleNumber);
    }
    
    private Demand parentDemand; // Eskaintza bada, hemen gordeko dugu eskaera

    public Demand getParentDemand() {
        return parentDemand;
    }

    public void setParentDemand(Demand parentDemand) {
        this.parentDemand = parentDemand;
    }
}