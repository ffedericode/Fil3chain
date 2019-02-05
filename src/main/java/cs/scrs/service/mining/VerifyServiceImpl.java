package cs.scrs.service.mining;


import cs.scrs.miner.dao.block.Block;
import cs.scrs.miner.dao.block.BlockRepository;
import cs.scrs.miner.dao.transaction.Transaction;
import cs.scrs.miner.models.MerkleTree;
import cs.scrs.service.poolDispatcher.PoolDispatcherServiceImpl;
import cs.scrs.service.util.CryptoUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



@Service
public class VerifyServiceImpl {

	@Autowired
	private BlockRepository blockRepository;
	@Autowired
	private PoolDispatcherServiceImpl poolDispService;


	public VerifyServiceImpl() {
	}

	// Metodo di verifica di un singolo blocco
	/**
	 * @param blockRepository
	 * @param blockToVerify
	 * @return boolean true if is ok
	 */
	public Boolean singleBlockVerify(Block blockToVerify) {

		List<Transaction> x = blockToVerify.getTransactionsContainer();
		System.out.println("transazioni nel blocco");
		for (Integer i = 0; i < x.size(); i++)
			System.out.println(" Trans ricevte dal blocco in verifica " + x.get(i) + " posizione " + x.get(i).getIndexInBlock());
		// Ordine di verifica migliore: Firma, PoW, Markle root, Double Trans
		// TODO
		// COntrolla se il apdre è ad un livello meno 1 del mio chain level
		System.out.println("inizio verify");
		// Verifica firma
		if (!verifySignature(blockToVerify)) {
			System.out.println("problemi sulla firma");
			return Boolean.FALSE;

		}
		System.out.println("firma ok");
		// Verifica Proof of Work
		if (!verifyProofOfWork(blockToVerify)) {
			System.out.println("problema con la proof of work");
			return Boolean.FALSE;
		}

		System.out.println("pow ok");
		// Verifica MerkleRoot
		if (!verifyMerkleRoot(blockToVerify)) {
			System.out.println("problema merkle tree");
			return Boolean.FALSE;
		}
		System.out.println("merkle ok");
		// Verifica transazioni uniche
		if (!verifyUniqueTransactions(blockToVerify)) {
			System.out.println("problema con transazioni uniche");
			return Boolean.FALSE;
		}
		System.out.println("finito verify");
		// Se ha passato tutti i controlli allora ritorna TRUE
		return Boolean.TRUE;
	}

	// Metodo di verifica della proof of work
	private Boolean verifyProofOfWork(Block block) {

		Integer complexity = poolDispService.getBlockComplexity(block.getCreationTime());

		// Se c'è stato un errore o la complessità non è stata trovata nel
		// server
		// allora termina con FALSE
		if (complexity == -1) {
			System.err.println("Verify Proof Of Work: Errore nella complessità, impossibile verificare blocco");
			return Boolean.FALSE;
		}

		// Calcolo full mask
		int fullMask = complexity / 8;

		// Calcolo rest mask
		int restanti = complexity % 8;

		byte restMask;

		if (restanti == 0) {
			restMask = 0b000000;
		} else {
			restMask = (byte) 0b11111111;
			restMask = (byte) (restMask << (8 - restanti));
		}

		byte[] hash = DigestUtils.sha256(block.toString() + block.getNonce());

		// Verifica dei primi fullMask byte interi
		for (int i = 0; i < fullMask; i++) {
			if (hash[i] != 0) {
				System.err.println("Verify Proof Of Work: Errore nei primi zeri " + hash[i] + " " + i + " Fullmask " + fullMask);
				return false;
			}
		}

		// Se non ci sono bit restanti allora restituisce true
		if (restMask == 0)
			return true;

		// Altrimenti controlla i bit rimanenti
		boolean result = (hash[fullMask] & restMask) == 0;
		if (!result) {
			System.err.println("Verify Proof Of Work: Errore nei restanti bit " + hash[fullMask] + " " + restMask);
		}
		return (hash[fullMask] & restMask) == 0;
	}

	// Metodo di verifica della firma di un blocco
	// Abbiamo stabilito di firmare solo l'hash del blocco essendo già esso
	// fatto su tutti gli altri campi
	private Boolean verifySignature(Block block) {

		try {
			if (!CryptoUtil.verifySignature(block.getHashBlock(), block.getSignature(), block.getMinerPublicKey())) {
				return Boolean.FALSE;
			}
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}

		return Boolean.TRUE;
	}

	// Metodo di verifica del merkle root di un bloccok
	private Boolean verifyMerkleRoot(Block block) {

		ArrayList<String> transactionsHash = new ArrayList<>();

		Collections.sort(block.getTransactionsContainer(), Comparator.comparing(Transaction::getIndexInBlock));
		for (Transaction transaction : block.getTransactionsContainer()) {
			System.out.println("controllo merkle posione nel blocco" + transaction.getIndexInBlock());
			transactionsHash.add(transaction.getHashFile());
		}
		if (transactionsHash.size() == 0) {
			System.out.println("Merkle root verify: Nessuna transazione nel blocco con hash: " + block.getHashBlock());
			return Boolean.FALSE;
		}

		System.out.println("Merckle Hash Block:" + block.getHashBlock());
		String checkMerkle = MerkleTree.buildMerkleTree(transactionsHash);
		// Collections.reverse(transactionsHash);
		// String checkMerkle2 = MerkleTree.buildMerkleTree(transactionsHash);
		//
		// for (Transaction transaction : block.getTransactionsContainer()) {
		// System.out.println("Lista transazioni blocco da
		// verificare:"+transaction.getHashFile());
		// }

		System.out.println("Merkle mio: " + checkMerkle);
		// System.out.println("Merkle mio2:"+checkMerkle2);
		System.out.println("Merkle suo: " + block.getMerkleRoot());
		System.out.println("Confronto merkle: " + checkMerkle.equals(block.getMerkleRoot()));

		if ((!checkMerkle.equals(block.getMerkleRoot()))) {
			System.err.println("MerkleRoot diverso. Mio: " + checkMerkle + " Suo: " + block.getMerkleRoot());
			return Boolean.FALSE;
		}
		// System.out.println("Lista Transazioni Merkle
		// Root"+transactionsHash.toString());
		return Boolean.TRUE;
	}

	// Metodo di verifica della transazione unica
	// Tutti i predecessori del blocco arrivato NON devono avere la transazione
	private Boolean verifyUniqueTransactions(Block block) {

		List<Block> predecessori = new ArrayList<>();
		Block currentBlock = blockRepository.findByhashBlock(block.getFatherBlockContainer());
		List<Transaction> tList = block.getTransactionsContainer();

		
		//finche il padre è diverso è da null aggiungi risali il ramo e aggiungi i blocchi
		while (currentBlock.getFatherBlockContainer() != null) {
			//aggiungo l blocco a predecesori
			predecessori.add(currentBlock);
			//avanzo il blocco indice
			currentBlock = blockRepository.findByhashBlock(currentBlock.getFatherBlockContainer());
		}
		//per ogni blocco neo predecessori
		for (Integer i = 0; i < predecessori.size(); i++) {
			//per ogni transazione nel blocco dei predecessori
			for (Integer j = 0; j < predecessori.get(i).getTransactionsContainer().size(); j++)
				//per ogni transazione nel blocco che mi è stato mandato
				for (Integer k = 0; k < tList.size(); k++)
					//se la transazione nella catena e quella nel blocco hanno lo stesso hash sono uguali
					if (predecessori.get(i).getTransactionsContainer().get(j).getHashFile().equals((tList.get(k).getHashFile()))) {
						System.err.println("La transazione è presente in uno dei predecessori.");
						return Boolean.FALSE;
					}
		}
		System.out.println("transazioni validate");
		return Boolean.TRUE;
	}

	/**
	 * @return the blockRepository
	 */
	public BlockRepository getBlockRepository() {

		return blockRepository;
	}

	/**
	 * @param blockRepository
	 *            the blockRepository to set
	 */
	public void setBlockRepository(BlockRepository blockRepository) {

		this.blockRepository = blockRepository;
	}

	/**
	 * @return the poolDispService
	 */
	public PoolDispatcherServiceImpl getPoolDispService() {

		return poolDispService;
	}

	/**
	 * @param poolDispService
	 *            the poolDispService to set
	 */
	public void setPoolDispService(PoolDispatcherServiceImpl poolDispService) {

		this.poolDispService = poolDispService;
	}

}
