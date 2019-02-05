/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cs.scrs.miner.dao.user;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 *
 *
 */
@Entity
@Table(name = "User")
public class User {

	// Columns
	@Id
	@Column(name = "publicKeyHash")
	private String publicKeyHash;
	@Column(name = "publicKey")
    @Length(max = 500)
	private String publicKey;
	@Column(name = "name")
	private String name;
	@Column(name = "lastname")
	private String lastName;
	@Column(name = "email")
	private String email;
	@Column(name = "username")
	private String username;
	@Column(name="password")
	private String password;




	// Relations
//	@OneToMany(mappedBy = "userContainer")
//	@JsonIgnore
//	private List<Block> calculatedBlocks;

/*
	@OneToMany(mappedBy = "authorContainer")
    @JsonIgnore
	// @JoinColumn(name = "hashFile")// Autore
	private List<Transaction> fileContainer;
*/
	
	
	public User(){};

	/**
	 * @param publicKeyHash
	 * @param publicKey
	 * @param name
	 * @param lastName
	 * @param email
	 * @param username
	 */
	public User(String publicKeyHash, String publicKey, String name, String lastName, String email, String username) {
		super();
		this.publicKeyHash = publicKeyHash;
		this.publicKey = publicKey;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
	}

	public User(String publicKeyHash, String publicKey, String name, String lastName, String email, String username, String password) {
		super();
		this.publicKeyHash = publicKeyHash;
		this.publicKey = publicKey;
		this.name = name;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
	}
	/**
	 *
	 */
	public void loadKeyConfig() {

		Properties prop = new Properties();
		InputStream in = Object.class.getResourceAsStream("/keys.properties");
		try {
			prop.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.publicKey = prop.getProperty("public");
	}
	
	
	/**
	 * @return the publicKeyHash
	 */
	public String getPublicKeyHash() {

		return publicKeyHash;
	}

	/**
	 * @param publicKeyHash
	 *            the publicKeyHash to set
	 */
	public void setPublicKeyHash(String publicKeyHash) {

		this.publicKeyHash = publicKeyHash;
	}

	/**
	 * @return the publicKey
	 */
	public String getPublicKey() {

		return publicKey;
	}

	/**
	 * @param publicKey
	 *            the publicKey to set
	 */
	public void setPublicKey(String publicKey) {

		this.publicKey = publicKey;
	}

	/**
	 * @return the name
	 */
	public String getName() {

		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {

		this.name = name;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {

		return lastName;
	}

	/**
	 * @param lastName
	 *            the lastName to set
	 */
	public void setLastName(String lastName) {

		this.lastName = lastName;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {

		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {

		this.email = email;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {

		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {

		this.username = username;
	}

	@Override
	public String toString() {
		return "{" +
				"\"username\":\"" + username + "\"" +
				",\"email\":\"" + email + "\"" +
				",\"lastName\":\"" + lastName + "\"" +
				",\"name\":\"" + name + "\"" +
				",\"publicKey\":\"" + publicKey + "\"" +
				",\"publicKeyHash\":\"" + publicKeyHash + "\"" +
				"}";
	}


	@Override
	public boolean equals(Object o) {
		User u=(User)o;
		if(this.getPublicKeyHash()== u.getPublicKeyHash() && this.getPublicKey()==u.getPublicKey() && this.getName() == u.getName() && this.getLastName() == u.getLastName() && this.getEmail()==u.getEmail()&& this.getUsername()==u.getUsername())
			return Boolean.TRUE;
		return Boolean.FALSE;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	/**
	 * @return the fileContainer
	 */
	/*
	public List<Transaction> getFileContainer() {

		return fileContainer;
	}
*/

	/*
	public void setFileContainer(List<Transaction> fileContainer) {

		this.fileContainer = fileContainer;
	}
	*/

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
