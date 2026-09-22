package domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlIDREF;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Reclamation implements Serializable {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer reclamationNumber; // Identifikatzaile bakarra 
	
	private String deskribapena; // Zer gertatu den azalpena
	private String egoera;       // "Zabalik", "Aztertzen", "Itxita"
	
	@XmlIDREF
	@ManyToOne(fetch=FetchType.EAGER)
	private Seller saltzailea;   // Nori egiten zaion erreklamazioa (Saltzailea)
	
	@XmlIDREF
	@ManyToOne(fetch=FetchType.EAGER)
	private Seller eroslea;      // Nork jartzen duen erreklamazioa (Eroslea)

	private String soluzioa = "Soluzioaren zain..."; // Hasierako balioa

	public String getSoluzioa() { return soluzioa; }
	public void setSoluzioa(String soluzioa) { this.soluzioa = soluzioa; }
	
	public Reclamation() {
		super();
	}

	@XmlIDREF
    @OneToOne // Erreklamazio bakoitza produktu bakar bati lotuta dago
    private Sale produktua;

    // Eraikitzailean gehitu:
    public Reclamation(String deskribapena, Sale produktua, Seller saltzailea, Seller eroslea) {
        this.deskribapena = deskribapena;
        this.produktua = produktua; // Garrantzitsua!
        this.saltzailea = saltzailea;
        this.eroslea = eroslea;
        this.egoera = "Zabalik"; 
    }
    
    
	// GETTER-ak eta SETTER-ak
    public Sale getProduktua() { return produktua; }
	
	public Integer getReclamationNumber() {
		return reclamationNumber;
	}

	public String getDeskribapena() {
		return deskribapena;
	}

	public void setDeskribapena(String deskribapena) {
		this.deskribapena = deskribapena;
	}

	public String getEgoera() {
		return egoera;
	}

	public void setEgoera(String egoera) {
		this.egoera = egoera;
	}

	public Seller getSaltzailea() {
		return saltzailea;
	}

	public void setSaltzailea(Seller saltzailea) {
		this.saltzailea = saltzailea;
	}

	public Seller getEroslea() {
		return eroslea;
	}

	public void setEroslea(Seller eroslea) {
		this.eroslea = eroslea;
	}

	@Override
	public String toString() {
		return "Erreklamazioa #" + reclamationNumber + " [" + egoera + "]: " + deskribapena;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Reclamation other = (Reclamation) obj;
		return reclamationNumber != null && reclamationNumber.equals(other.reclamationNumber);
	}

	@Override
	public int hashCode() {
		return reclamationNumber != null ? reclamationNumber.hashCode() : 0;
	}
}