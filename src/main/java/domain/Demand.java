package domain;

import java.io.Serializable;
import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@SuppressWarnings("serial")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
public class Demand implements Serializable {
    @XmlID
    @Id 
    @XmlJavaTypeAdapter(IntegerAdapter.class)
    @GeneratedValue
    private Integer demandNumber;
    private String title;
    private String description;
    private float maxPrice;
    
    @XmlIDREF
    @ManyToOne(fetch=FetchType.EAGER)
    private Seller claimant; // Eskatzailea

    public Demand() {
        super();
    }

    public Demand(String title, String description, float maxPrice, Seller claimant) {
        super();
        this.title = title;
        this.description = description;
        this.maxPrice = maxPrice;
        this.claimant = claimant;
    }

    // Getters eta Setters
    public Integer getDemandNumber() { return demandNumber; }
    public void setDemandNumber(Integer demandNumber) { this.demandNumber = demandNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public float getMaxPrice() { return maxPrice; }
    public void setMaxPrice(float maxPrice) { this.maxPrice = maxPrice; }
    public Seller getClaimant() { return claimant; }
    public void setClaimant(Seller claimant) { this.claimant = claimant; }

    @Override
    public String toString() {
        return title + " (Max: " + maxPrice + "€)";
    }
}