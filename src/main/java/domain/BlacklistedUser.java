  package domain;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class BlacklistedUser implements Serializable {
    @Id
    private String email;

    public BlacklistedUser() { super(); }
    public BlacklistedUser(String email) { this.email = email; }
    public String getEmail() { return email; }
    public String toString() { return email; }
}
