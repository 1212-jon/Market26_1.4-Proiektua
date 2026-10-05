package domain;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class CreateSaleParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String description;
    private int numStatus;
    private float price;
    private Date date;
    private String sellerEmail;
    private File image;
    private Demand parent;

    public CreateSaleParams(String title, String description, int numStatus, float price, Date date, String sellerEmail, File image, Demand parent) {
        this.title = title;
        this.description = description;
        this.numStatus = numStatus;
        this.price = price;
        this.date = date;
        this.sellerEmail = sellerEmail;
        this.image = image;
        this.parent = parent;
    }

    // Getter-ak eta Setter-ak
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getNumStatus() { return numStatus; }
    public float getPrice() { return price; }
    public Date getDate() { return date; }
    public String getSellerEmail() { return sellerEmail; }
    public File getImage() { return image; }
    public Demand getParent() { return parent; }
}