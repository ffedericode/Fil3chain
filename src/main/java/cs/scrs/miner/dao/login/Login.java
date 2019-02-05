package cs.scrs.miner.dao.login;

import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Christian on 07/07/2016.
 */

@Entity
@Table(name = "Login")
public class Login {

    @Id
    @Column(name = "publicKeyHash")
    private String publicKeyHash;
    @Column(name = "publicKey")
    @Length(max = 500)
    private String publicKey;
    @Column(name = "role")
    private Integer role;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    public Login(String publicKeyHash, String publicKey, Integer role, String username, String password) {
        this.publicKeyHash = publicKeyHash;
        this.publicKey = publicKey;
        this.role = role;
        this.username = username;
        this.password = password;
    }

    public String getPublicKeyHash() {
        return publicKeyHash;
    }

    public void setPublicKeyHash(String publicKeyHash) {
        this.publicKeyHash = publicKeyHash;
    }

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Login(){}

}
