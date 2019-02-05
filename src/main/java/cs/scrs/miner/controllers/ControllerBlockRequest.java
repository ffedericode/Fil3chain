package cs.scrs.miner.controllers;

import cs.scrs.miner.dao.block.Block;
import cs.scrs.miner.dao.block.BlockRepository;
import cs.scrs.miner.dao.transaction.Transaction;
import cs.scrs.miner.dao.transaction.TransactionRepository;
import cs.scrs.miner.dao.user.UserRepository;
import cs.scrs.miner.models.Filechain;
import cs.scrs.service.ip.IPServiceImpl;
import cs.scrs.service.util.Conversions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Component
@RestController
public class ControllerBlockRequest {

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BlockRepository blockRepository;

	@Autowired
	private IPServiceImpl ipService;
	
	@Autowired
	private Filechain filechain;
        
        


	/////////////////// MAPPING RICHIESTE DI BLOCCHI DELLA FIL3CH4IN ///////////////////////////////////////////////
     
	// Controller che intercetta arrivo di un nuovo blocco
	@RequestMapping(value = "/fil3chain/newBlock", method = RequestMethod.POST)
	@ResponseBody
	public String newBlock(@RequestBody Block block, HttpServletRequest request) {
		// Block newBlock = block;
		//System.out.println("Il blocco che mi è stato mandato è " + block);
		// TODO dobbiamo verificare il blocco appena arrivato se è valido
		// blocco il thread di mining e lo riavvio sulla fil3chain aggiornata

		//MinersListenerRegister.getInstance().notifyListenersNewBlock(newBlock);
		//TODO POSSIAMO TOGLIERE IL LISTENER E FARE COSI

		System.out.println("Il miner "+request.getRemoteAddr()+" mi ha mandato il blocco: "+block);
		filechain.onNewBlockArrived(block);
		System.out.println("rispondo");
		return Boolean.TRUE.toString();
	}

	@RequestMapping(value = "/fil3chain/getBlockByChain", method = RequestMethod.GET)
	@ResponseBody
	public List<Block> getBlock(Integer chainLevel,HttpServletRequest request) {
		// System.err.println("Rispondo con: " +
		// blockRepository.findBychainLevel(chainLevel));
		System.out.println("Il miner "+request.getRemoteAddr()+" mi ha chiesto un blocco con ChainLevel "+chainLevel);
		return blockRepository.findBychainLevel(chainLevel);
	}

	@RequestMapping(value = "/fil3chain/getBlockByhash", method = RequestMethod.GET)
	@ResponseBody
	public Block getBlock(String hash,HttpServletRequest request) {
		// System.err.println("Rispondo con: " +
		// blockRepository.findBychainLevel(chainLevel));
		System.out.println("Il miner "+request.getRemoteAddr()+" mi ha chiesto un blocco con questo Hash: "+hash);
		Block b = blockRepository.findByhashBlock(hash);
		return b;
	}
        
        //viene utilizzato quando dall'interfaccia vengono richiesti i
        // dettagli di un blocco
        @RequestMapping(value = "/fil3chain/blockDetail", method = RequestMethod.POST)
	@ResponseBody
	public String getBlockDetail(@RequestBody Block b) {		
		Block block = blockRepository.findByhashBlock(b.getHashBlock());
		return Conversions.toJson(block);
	}

	// Mappiamo la richiesta di invio di blocchi ad un Peer che la richiede
	@RequestMapping(value = "/fil3chain/updateAtMaxLevel", method = RequestMethod.GET)
	public Integer updateAtMaxLevel(HttpServletRequest request) {
		// Inutile che ritorno si/no con accodato il chain level basta che torno
		// il chain level e
		// il ricevente sa a chi chiedere tutti i blocchi di cui ha bisogno
		System.out.println("Il miner "+request.getRemoteAddr()+" mi ha chiesto il mio Chain Level ");
		return blockRepository.findFirstByOrderByChainLevelDesc().getChainLevel();
	}
        

/////////MAPPING DI RICHIESTER A SCOPO DI TESTING//////////////////////

    @RequestMapping(value = "/provaJson", method = RequestMethod.GET)
    public Block requestBlocks() {

        Block block = new Block("adas", "dsadsa", "12", 23, 3);

        return block;
    }

    // http://localhost:8080/addJsonBlock?hashBlock=22213&merkleRoot=cad&minerPublicKey=12&nonce=1&chainLevel=1
    @RequestMapping(value = "/addJsonBlock", method = RequestMethod.GET)
    public Block addJsonBlock(String hashBlock, String merkleRoot, String minerPublicKey, Integer nonce,
                              Integer chainLevel, String signature) {

        Block block = new Block(hashBlock, merkleRoot, minerPublicKey, nonce, chainLevel);
        block.setSignature(signature);
        blockRepository.save(block);
        return block;
    }

    // Aggiungo delle transazioni di prova
    @RequestMapping(value = "/JsonTransaction", method = RequestMethod.GET)
    @ResponseBody
    public List<Transaction> JsonTransaction(Integer nTrans) {
        String s = "";
        Double x;
        List<Transaction> trans = new ArrayList<Transaction>();
        for (int i = 0; i < nTrans; i++) {
            x = Math.random() * nTrans;
            s = org.apache.commons.codec.digest.DigestUtils.sha256Hex(x.toString());
            Transaction transaction = new Transaction(s, "file prova numero: " + i);
            trans.add(transaction);
        }
        return trans;
    }
    	

	/**
	 * @return the transactionRepository
	 */
	public TransactionRepository getTransactionRepository() {
	
		return transactionRepository;
	}

	
	/**
	 * @param transactionRepository the transactionRepository to set
	 */
	public void setTransactionRepository(TransactionRepository transactionRepository) {
	
		this.transactionRepository = transactionRepository;
	}

	
	/**
	 * @return the userRepository
	 */
	public UserRepository getUserRepository() {
	
		return userRepository;
	}

	
	/**
	 * @param userRepository the userRepository to set
	 */
	public void setUserRepository(UserRepository userRepository) {
	
		this.userRepository = userRepository;
	}

	
	/**
	 * @return the blockRepository
	 */
	public BlockRepository getBlockRepository() {
	
		return blockRepository;
	}

	
	/**
	 * @param blockRepository the blockRepository to set
	 */
	public void setBlockRepository(BlockRepository blockRepository) {
	
		this.blockRepository = blockRepository;
	}

	
	/**
	 * @return the ipService
	 */
	public IPServiceImpl getIpService() {
	
		return ipService;
	}

	
	/**
	 * @param ipService the ipService to set
	 */
	public void setIpService(IPServiceImpl ipService) {
	
		this.ipService = ipService;
	}

	
	/**
	 * @return the filechain
	 */
	public Filechain getFilechain() {
	
		return filechain;
	}

	
	/**
	 * @param filechain the filechain to set
	 */
	public void setFilechain(Filechain filechain) {
	
		this.filechain = filechain;
	}

}
