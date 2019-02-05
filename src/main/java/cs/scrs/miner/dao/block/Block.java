/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package cs.scrs.miner.dao.block;


import cs.scrs.miner.dao.transaction.Transaction;
import cs.scrs.miner.dao.user.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.hibernate.annotations.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import java.util.List;




/**
 *
 *
 */
@Entity
public class Block {

    // Columns
    @Id
    private String hashBlock; // ProofOfWOrk //Costituisce l'id del blocco, per
    // essere identificato in maniera UNIVOCA nella
    // blockchain
    @Column(nullable = false)
    private String creationTime;

    @Column(nullable = false)
    private String merkleRoot;

    @Column(nullable = false)
    @Length(max = 500)
    private String minerPublicKey;

    @Column(nullable = false)
    private Integer nonce;

    @Column(nullable = false)
    private Integer chainLevel;

    @Column(nullable=false)
    @Length(max = 500)
    private String signature; //firma
    // Difficoltà per la proof of work ,inviato da poolDispatcher

    // Relationship

    @OneToMany(mappedBy = "blockContainer", fetch = FetchType.EAGER, cascade = { CascadeType.ALL })
    @OrderBy("index_in_block")
    private List<Transaction> transactionsContainer;

    
    
//    TODO PROVARE
//    @ManyToOne
//    private Block children;
//    @OneToMany(mappedBy="children")
//    private List<Block> parent;
    @Column(name="Block_prevHashBlock")
    private String fatherBlockContainer;
    
//    @OneToOne
//    @JoinColumn(name = "Block_prevHashBlock")
//    private Block fatherBlockContainer;

    @ManyToOne
    @JoinColumn(name = "User_publicKeyHash") // Autore
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private User userContainer;


    /**
     *
     */
    public Block() {

    }

    /**
     * @param hashBlock
     * @param merkleRoot
     * @param minerPublicKey
     * @param nonce
     * @param chainLevel
     */
    public Block(String hashBlock, String merkleRoot, String minerPublicKey, Integer nonce, Integer chainLevel) {
        super();
        this.hashBlock = hashBlock;
        this.creationTime = Long.toString(System.currentTimeMillis());
        this.merkleRoot = merkleRoot;
        this.minerPublicKey = minerPublicKey;
        this.nonce = nonce;
        this.chainLevel = chainLevel;
    }

    /**
     * @param merkleRoot
     * @param minerPublicKey
     * @param nonce
     * @param chainLevel
     * @param transactionsContainer
     * @param fatherBlockContainer
     * @param userContainer
     */
    public Block(String merkleRoot, String minerPublicKey, Integer nonce, Integer chainLevel, List<Transaction> transactionsContainer, String fatherBlockContainer, User userContainer) {
        super();
        this.creationTime = Long.toString(System.currentTimeMillis());
        this.merkleRoot = merkleRoot;
        this.minerPublicKey = minerPublicKey;
        this.nonce = nonce;
        this.chainLevel = chainLevel;
        this.transactionsContainer = transactionsContainer;
        this.fatherBlockContainer = fatherBlockContainer;
        this.userContainer = userContainer;
    }

    @Override
    public String toString() {
        return fatherBlockContainer + merkleRoot + minerPublicKey;
    }

    public void generateHashBlock() {
        String s = DigestUtils.sha256Hex(this.toString());
        this.setHashBlock(s);
    }

    public String generateAndGetHashBlock() {

        String s = org.apache.commons.codec.digest.DigestUtils.sha256Hex(this.toString());
        this.setHashBlock(s);
        return s;
    }

    /**
     * @param merkleRoot
     * @param minerPublicKey
     * @param nonce
     * @param chainLevel
     */
    public Block(String merkleRoot, String minerPublicKey, Integer nonce, Integer chainLevel) {
        super();
        this.merkleRoot = merkleRoot;
        this.minerPublicKey = minerPublicKey;
        this.nonce = nonce;
        this.chainLevel = chainLevel;
    }

    /**
     * @return the hashBlock
     */
    public String getHashBlock() {

        return hashBlock;
    }

    /**
     * @param hashBlock
     *            the hashBlock to set
     */
    public void setHashBlock(String hashBlock) {

        this.hashBlock = hashBlock;
    }

    /**
     * @return the creationTime
     */
    public String getCreationTime() {

        return creationTime;
    }

    /**
     * @param creationTime
     *            the creationTime to set
     */
    public void setCreationTime(String creationTime) {

        this.creationTime = creationTime;
    }

    /**
     * @return the merkleRoot
     */
    public String getMerkleRoot() {

        return merkleRoot;
    }

    /**
     * @param merkleRoot
     *            the merkleRoot to set
     */
    public void setMerkleRoot(String merkleRoot) {

        this.merkleRoot = merkleRoot;
    }

    /**
     * @return the minerPublicKey
     */
    public String getMinerPublicKey() {

        return minerPublicKey;
    }

    /**
     * @param minerPublicKey
     *            the minerPublicKey to set
     */
    public void setMinerPublicKey(String minerPublicKey) {

        this.minerPublicKey = minerPublicKey;
    }

    /**
     * @return the nonce
     */
    public Integer getNonce() {

        return nonce;
    }

    /**
     * @param nonce
     *            the nonce to set
     */
    public void setNonce(Integer nonce) {

        this.nonce = nonce;
    }

    /**
     * @return the chainLevel
     */
    public Integer getChainLevel() {

        return chainLevel;
    }

    /**
     * @param chainLevel
     *            the chainLevel to set
     */
    public void setChainLevel(Integer chainLevel) {

        this.chainLevel = chainLevel;
    }

    /**
     * @return the transactionsContainer
     */
    public List<Transaction> getTransactionsContainer() {

        return transactionsContainer;
    }

    /**
     * @param transactionsContainer
     *            the transactionsContainer to set
     */
    public void setTransactionsContainer(List<Transaction> transactionsContainer) {

        this.transactionsContainer = transactionsContainer;
    }

    /**
     * @return the fatherBlockContainer
     */
    public String getFatherBlockContainer() {

        return fatherBlockContainer;
    }

    /**
     * @param fatherBlockContainer
     *            the fatherBlockContainer to set
     */
    public void setFatherBlockContainer(String fatherBlockContainer) {

        this.fatherBlockContainer = fatherBlockContainer;
    }

    /**
     * @return the userContainer
     */
    public User getUserContainer() {

        return userContainer;
    }

    /**
     * @param userContainer the userContainer to set
     */
    public void setUserContainer(User userContainer) {

        this.userContainer = userContainer;
    }

    /**
     * @return the signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * @param signature the signature to set
     */
    public void setSignature(String signature) {
        this.signature = signature;
    }

}
