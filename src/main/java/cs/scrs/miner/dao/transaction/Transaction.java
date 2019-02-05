/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose Tools | Templates and open the template in the editor.
 */
package cs.scrs.miner.dao.transaction;


import cs.scrs.miner.dao.citations.Citation;
import cs.scrs.miner.dao.user.User;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.List;



@Entity
@Table(name = "transaction")
public class Transaction {

	// Rappresenta l'hash della transazione, calcolata dopo aver aggiunto
	// a essa il blocco a cui fa riferimento
	@Id
	@Column(name = "hashTransBlock")
	private String hashTransBlock;

	@Column(name = "hashFile")
	private String hashFile;
	@Column(name = "filename")
	private String filename;
	@Column(name = "index_in_block")
	private Integer indexInBlock;

	// Relations
	@Column(name = "blockContainer")
	private String blockContainer;

	@ManyToOne
	@JoinColumn(name = "User_publicKeyHash")
	private User authorContainer;

	// @ManyToMany
	// @JoinTable(name = "Citations", joinColumns = { @JoinColumn(name = "Transaction_hashFileCite", referencedColumnName = "hashFile") }, inverseJoinColumns = { @JoinColumn(name = "Transaction_hashFileCited", referencedColumnName = "hashFile") })
	// private List<Transaction> citationsContainer;

	@OneToMany
	@JoinColumn(name = "hashCiting")
	@Cascade(org.hibernate.annotations.CascadeType.ALL)
	private List<Citation> citations;


	public Transaction(String hashFile, String filename) {
		super();
		this.hashFile = hashFile;
		this.filename = filename;
	}

	public Transaction() {
	}

	/**
	 * @return the hashFile
	 */
	public String getHashFile() {

		return hashFile;
	}

	/**
	 * @param hashFile
	 *            the hashFile to set
	 */
	public void setHashFile(String hashFile) {

		this.hashFile = hashFile;
	}

	/**
	 * @return the filename
	 */
	public String getFilename() {

		return filename;
	}

	/**
	 * @param filename
	 *            the filename to set
	 */
	public void setFilename(String filename) {

		this.filename = filename;
	}

	/**
	 * @return the blockContainer
	 */
	public String getBlockContainer() {

		return blockContainer;
	}

	/**
	 * @param blockContainer
	 *            the blockContainer to set
	 */
	public void setBlockContainer(String blockContainer) {

		this.blockContainer = blockContainer;
	}

	/**
	 * @return the authorContainer
	 */
	public User getAuthorContainer() {

		return authorContainer;
	}

	/**
	 * @param authorContainer
	 *            the authorContainer to set
	 */
	public void setAuthorContainer(User authorContainer) {

		this.authorContainer = authorContainer;
	}

	public List<Citation> getCitations() {
		return citations;
	}

	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}

	public Integer getIndexInBlock() {

		return indexInBlock;
	}

	public void setIndexInBlock(Integer indexInBlock) {

		this.indexInBlock = indexInBlock;
	}

	public String getHashTransBlock() {
		return hashTransBlock;
	}

	public void setHashTransBlock(String hashTransBlock) {
		this.hashTransBlock = hashTransBlock;
	}

	@Override
	public String toString() {

		return "{\"hashTransBlock\":\"" + hashTransBlock + "\"" + "," + "\"hashFile\":\"" + hashFile + "\"" + "," + "\"filename\": \"" + filename + "\"" + "," + "\"indexInBlock\":\"" + indexInBlock + "\"," + "\"blockContainer\": \"" + blockContainer + "\"" + "," + "\"authorContainer\": "
				+ authorContainer + "," + "\"citations\": " + citations + "}";
	}
}