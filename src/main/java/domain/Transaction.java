package domain;
 
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Transaction implements Serializable {
    
    @Id
    @GeneratedValue
    private Integer transactionID;
    private String mota;        // "SARRERA", "IRTEERA", "EROSKETA", "SALMENTA"
    private String kontzeptua;  // Adib: "Dirua kargatu", "Iphone 17 saldu da"
    private float kantitatea;
    private Date data;

    public Transaction() {
        super();
    }

    public Transaction(String mota, String kontzeptua, float kantitatea, Date data) {
        this.mota = mota;
        this.kontzeptua = kontzeptua;
        this.kantitatea = kantitatea;
        this.data = data;
    }

    // Getters eta Setters
    public Integer getTransactionID() { return transactionID; }
    public String getMota() { return mota; }
    public String getKontzeptua() { return kontzeptua; }
    public float getKantitatea() { return kantitatea; }
    public Date getData() { return data; }

    @Override
    public String toString() {
        return data + " - " + mota + ": " + kantitatea + "€ (" + kontzeptua + ")";
    }
}