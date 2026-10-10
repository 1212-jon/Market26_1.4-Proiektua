package domain;

import java.io.File;
import java.util.Date;

public class SaleData {
    private String title;
    private String description;
    private int status;
    private float price;
    private Date pubDate;
    private File file;
    private Seller seller;

    public SaleData(String title, String description, int status, float price, Date pubDate, File file, Seller seller) {
        this.title = title;
        this.description = description;
        this.status = status;
        this.price = price;
        this.pubDate = pubDate;
        this.file = file;
        this.seller = seller;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public int getStatus() { return status; }
    public float getPrice() { return price; }
    public Date getPubDate() { return pubDate; }
    public File getFile() { return file; }
    public Seller getSeller() { return seller; }
}

