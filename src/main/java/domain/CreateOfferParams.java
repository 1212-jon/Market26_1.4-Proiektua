package domain;

import java.io.Serializable;

public class CreateOfferParams implements Serializable {
	private static final long serialVersionUID = 1L;

    private String title;
    private float price;
    private String description;
    private Seller seller;
    private Demand demand;

    public CreateOfferParams(String title, float price, String description, Seller seller, Demand demand) {
        this.title = title;
        this.price = price;
        this.description = description;
        this.seller = seller;
        this.demand = demand;
    }

    // Getter-ak
    public String getTitle() { return title; }
    public float getPrice() { return price; }
    public String getDescription() { return description; }
    public Seller getSeller() { return seller; }
    public Demand getDemand() { return demand; }
}
