package cs.scrs.miner.models;


import cs.scrs.miner.dao.block.Block;
import cs.scrs.miner.dao.block.BlockRepository;
import cs.scrs.miner.dao.transaction.Transaction;
import cs.scrs.miner.dao.user.User;
import cs.scrs.miner.dao.user.UserRepository;
import cs.scrs.service.connection.ConnectionServiceImpl;
import cs.scrs.service.ip.IPServiceImpl;
import cs.scrs.service.mining.IMiningService;
import cs.scrs.service.mining.VerifyServiceImpl;
import cs.scrs.service.request.AsyncRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;



/**
 *
 */
@Service
@EnableAsync
public class Filechain {

	@Autowired
	private AsyncRequest asyncRequest;

	@Autowired
	private IPServiceImpl ipService;

	@Autowired
	private BlockRepository blockRepository;

	@Autowired
	private VerifyServiceImpl verifySerice;

	@Autowired
	private IMiningService miningService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ConnectionServiceImpl connectionServiceImpl;

	private static Boolean flagNewBlock = Boolean.TRUE;

	private Boolean flagRunningMinining = Boolean.FALSE;

	// private static final Integer KMAXLEVEL = 4;//DECISO DA CHRISTIAN SIMOLO IL 1/7/16 15:35 (A Random) SPOSTARE NEL PROPERTIES
	private static final Integer KMAXLEVEL = 5;// DECISO DA CHRISTIAN SIMOLO IL 1/7/16 15:36 (MOTIVATO:perche me pare più completo de 4,SImene vinciguerra aggiunge 1/5 sezione aurea) SPOSTARE NEL PROPERTIES


	/**
	 * 
	 */

	public Filechain() {
	}

	public void initializeFilechain() {

		connectionServiceImpl.firstConnectToEntryPoint();
		// Se non ho nessun blocco ne aggiungo uno fittizio
		if (!blockRepository.findAll().iterator().hasNext()) {
			Block block = getFirstBlock();
			blockRepository.save(block);

		}

	}

	private Block getFirstBlock() {

		Block block = new Block();
		block.setHashBlock("0");
		block.setChainLevel(0);
		block.setCreationTime(new Date(0).getTime() + "");
		block.setMerkleRoot("0");
		block.setNonce(0);
		block.setSignature("0");
		block.setMinerPublicKey("0");
		return block;
	}

	//
	/**
	 * Metodo di update della blockchain per chain level
	 * 
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws Exception
	 */
	public Boolean update() {

		List<IP> ipMiners = (List<IP>) ((ArrayList<IP>) ipService.getIPList()).clone();

		Integer myChainLevel;
		System.out.println("Filechain Update BlockRespository " + blockRepository.toString());

		while (!ipMiners.isEmpty()) {
			System.out.println("Update filechain");
			// Lista contenente le richieste asincrone ai 3 ip
			List<Future<Pairs<IP, Integer>>> minerResp = new ArrayList<>();
			// Chiedi al db il valore del mio Max chainLevel
			myChainLevel = blockRepository.findFirstByOrderByChainLevelDesc().getChainLevel();

			// Finche non sono aggiornato(ovvero mi rispondono con stringa
			// codificata o blocco fittizio)
			// Prendo k ip random da tutta la lista di Ip che mi sono stati inviati
			askMinerChainLvl(ipMiners, minerResp);
			System.out.println("Ho fatto le richieste");
			// minerResp.add(serviceMiner.findMaxChainLevel("192.168.0.107"));
			// System.out.println("1");

			// Oggetto che contiene la coppia IP,ChainLevel del Miner designato
			Pairs<IP, Integer> designedMiner = new Pairs<>();
			try {
				waitAndChooseMiner(ipMiners, myChainLevel, minerResp, designedMiner);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
			System.out.println("ho scelto il miner");

			try {
				killRequest(minerResp);
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

			// Aggiorno la mia blockChain con i blocchi che mi arrivano in modo incrementale
			if (designedMiner.getValue1() != null) {
				System.out.println("Il Miner designato = " + designedMiner.getValue1() + " con ChainLevel = " + designedMiner.getValue2() + "\n");
				Integer counter = 0;
				Boolean flag = Boolean.TRUE;
				while (counter <= AsyncRequest.REQNUMBER && flag) {
					try {
						System.out.println("\nBranchUpdate GetBlock");
						getBlocksFromMiner(ipMiners, designedMiner);
						flag = Boolean.FALSE;
					} catch (IOException | ExecutionException | InterruptedException e) {
						e.printStackTrace();
						counter++;
					}

					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}
			// aspetta una risposta
			// verifico i blocchi e aggiungo al db

			// mi connetto al primo che rispondi si e gli chiedo 10 blocchi o meno
			// chiusi dal blocco fittizio
			System.out.println(ipMiners.toString());
		}

		return Boolean.TRUE;
	}

	/**
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws Exception
	 */
	public Boolean updateBranChain(String hash) {

		List<IP> ipMiners = (List<IP>) ((ArrayList<IP>) ipService.getIPList()).clone();
		Boolean flag = Boolean.TRUE;
		// Rimuovo il mio IP
		// ipMiners.remove(miner.getIp());
		System.out.println("\nBranchUpdate");
		while (!ipMiners.isEmpty() && flag) {
			// Lista contenente le richieste asincrone ai 3 ip
			List<Pairs<Future<String>, IP>> minerResp = new ArrayList<>();

			// Finche non sono aggiornato(ovvero mi rispondono con stringa
			// codificata o blocco fittizio)
			// Prendo k ip random da tutta la lista di Ip che mi sono stati inviati
			askMinerBlock(ipMiners, minerResp);
			System.out.println("\nBranchUpdate REQ");

			// minerResp.add(serviceMiner.findMaxChainLevel("192.168.0.107"));
			// System.out.println("1");

			// Oggetto che contiene la coppia IP,ChainLevel del Miner designato
			IP designedMiner = null;
			try {
				designedMiner = waitAndChooseMinerBlock(minerResp);
				System.out.println("\nBranchUpdate Wait and Choose");

			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

			try {
				killRequestBlock(minerResp);
				System.out.println("\nBranchUpdate KILL");
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}

			// Aggiorno la mia blockChain con i blocchi che mi arrivano in modo incrementale
			if (designedMiner != null) {
				System.out.println("Il Miner designato = " + designedMiner.getIp() + "\n");
				Integer counter = 0;
				flag = Boolean.TRUE;
				while (counter <= AsyncRequest.REQNUMBER && flag) {
					try {
						System.out.println("\nBranchUpdate GetBlock");
						getBlockFromMiner(ipMiners, hash, designedMiner);
						flag = Boolean.FALSE;
					} catch (IOException | ExecutionException | InterruptedException e) {
						e.printStackTrace();
						counter++;
					}

					try {
						Thread.sleep(250);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} else {
				// Caso esplosione brutta, manca il miner
				System.err.println("Nessun miner ha risposto, spengi tutto e scappa.");
			}

			// aspetta una risposta
			// verifico i blocchi e aggiungo al db

			// mi connetto al primo che rispondi si e gli chiedo 10 blocchi o meno
			// chiusi dal blocco fittizio
			System.out.println("\n Hash branching: " + hash + " Miner ancora in lista" + ipMiners.toString());
		}
		return Boolean.TRUE;
	}

	/**
	 * @param minerResp
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private void killRequest(List<Future<Pairs<IP, Integer>>> minerResp) throws InterruptedException, ExecutionException {

		System.out.println("Numero di richieste da killare: " + minerResp.size());

		for (Future<Pairs<IP, Integer>> f : minerResp) {
			System.out.println("\nElimino :" + f.get().getValue1().getIp());
			f.cancel(Boolean.TRUE);
		}
	}

	/**
	 * @param minerResp
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private void killRequestBlock(List<Pairs<Future<String>, IP>> minerResp) throws InterruptedException, ExecutionException {

		System.out.println("Numero di richieste da killare: " + minerResp.size());

		for (Pairs<Future<String>, IP> pair : minerResp) {
			Future<String> future = pair.getValue1();
			System.out.println("\nElimino :" + pair.getValue2().getIp());
			future.cancel(Boolean.TRUE);
		}
	}

	/**
	 * Restituisce la lista di miner che hanno risposto con il loro livello di block chain.
	 *
	 * @param ipMiners
	 * @param minerResp
	 */
	private void askMinerChainLvl(List<IP> ipMiners, List<Future<Pairs<IP, Integer>>> minerResp) {

		for (int i = 0; i < ipMiners.size(); i++) {
			// Double x = Math.random() * ipMiners.size();
			Future<Pairs<IP, Integer>> result = asyncRequest.findMaxChainLevel(ipMiners.get(i).getIp());
			try {
				if (result == null || result.get() == null || result.get().getValue1() == null || result.get().getValue2() == null) {
					IP tmp = ipMiners.remove(i);
					System.out.println("\nHo rimosso l'IP: " + tmp.getIp());
				} else {
					minerResp.add(result);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Restituisce la lista di miner che hanno risposto con il loro livello di block chain.
	 *
	 * @param ipMiners
	 * @param minerResp
	 */
	private void askMinerBlock(List<IP> ipMiners, List<Pairs<Future<String>, IP>> minerResp) {

		for (int i = 0; i < ipMiners.size(); i++) {
			// Double x = Math.random() * ipMiners.size();
			System.out.println("iterazione " + i);
			// Future<Pairs<IP, Block>> result = serviceMiner.pingUser(ipMiners.get(i).getIp(), "getBlockByhash?hash=0");
			Future<String> result = asyncRequest.pingUser(ipMiners.get(i).getIp());
			try {
				if (result == null || result.get() == null) {
					IP tmp = ipMiners.remove(i);
					System.out.println("\nHo rimosso l'IP: " + tmp.getIp());
				} else {
					System.out.println("\nHo aggiunto l'IP: " + ipMiners.get(i).getIp());
					minerResp.add(new Pairs<>(result, ipMiners.get(i)));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param minerResp
	 * @param designedMiner
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */
	private void waitAndChooseMiner(List<IP> ipMiners, Integer myChainLevel, List<Future<Pairs<IP, Integer>>> minerResp, Pairs<IP, Integer> designedMiner) throws InterruptedException, ExecutionException {

		Boolean flag = Boolean.TRUE;

		while (flag && !minerResp.isEmpty()) {
			System.out.println("Size del numero di risposte dei miner: " + minerResp.size());
			// Controlliamo se uno dei nostri messaggi di richiesta è tornato
			// indietro con successo

			Future<Pairs<IP, Integer>> future;

			for (Integer i = 0; i < minerResp.size(); i++) {
				System.out.println("Il mio chain level: " + myChainLevel);
				System.out.println("Il chain level dell'altro miner: " + minerResp.get(i).get().getValue2() + " ip miner " + minerResp.get(i).get().getValue1());
				System.out.println("Size del numero di risposte dei miner: " + minerResp.size());
				future = minerResp.get(i);
				// facciamo un For per ciclare tutte richieste attive
				// all'interno del nostro array e controlliamo se
				// sono arrivate le risposte
				if (future != null) {
					if (future.isDone()) {
						if (future.get().getValue2() > myChainLevel) {
							flag = Boolean.FALSE;
							// IP del miner designato da cui prendere la blockchain
							designedMiner.setValue1(future.get().getValue1());
							// ChainLevel del miner designato
							designedMiner.setValue2(future.get().getValue2());
							System.out.println("\nRisposto da: " + future.get().getValue1() + " Chain level: " + future.get().getValue2());
						} else {
							// Miner ha livello minore del mio, allora elimino
							ipMiners.remove(future.get().getValue1());
							minerResp.remove(future);
							System.out.println("Elimino il miner perche ha chain level minore uguale al mio " + future.get().getValue2() + " e la dimensione della lista " + minerResp.size());
						}
					} else {
						System.out.println("Richiesta non pronta");
					}
				} else {
					// Future nulla, elimino il miner dalla lista
					// ipMiners.remove(future.get().getValue1());
					System.out.println("Elimino il miner perche future nulla e la dimensione della lista " + minerResp.size());
				}
			}
		}
	}

	/**
	 * @param minerResp
	 * @throws InterruptedException
	 * @throws ExecutionException
	 */

	private IP waitAndChooseMinerBlock(List<Pairs<Future<String>, IP>> minerResp) throws InterruptedException, ExecutionException {

		// Boolean flag = Boolean.TRUE;
		while (!minerResp.isEmpty()) {
			System.out.println("size: " + minerResp.size());
			// Controlliamo se uno dei nostri messaggi di richiesta è tornato
			// indietro con successo
			for (Pairs<Future<String>, IP> pair : minerResp) {

				Future<String> future = pair.getValue1();
				IP minerIp = pair.getValue2();

				// facciamo un For per ciclare tutte richieste attive
				// all'interno del nostro array e controlliamo se
				// sono arrivate le risposte
				System.out.println("\nSono in attesa di branch miner");
				if (future != null) {
					if (future.isDone()) {
						System.out.println("Controllo equals Loopo non se fida de Java: " + future.get().equals("{\"response\":\"ACK\"}") + " " + future.get());

						if (future.get().equals("{\"response\":\"ACK\"}")) {
							// IP del miner designato da cui prendere la blockchain
							System.out.println("\nRisposto da: " + minerIp.getIp());
							return minerIp;
						} else {
							future.cancel(Boolean.TRUE);
							minerResp.remove(pair);
						}
					}
				} else {
					minerResp.remove(pair);
				}

			}
			// Attesa tra una richiesta e l'altra
			Thread.sleep(250L);
		}

		return null;

	}

	/**
	 * @param ipMiners
	 * @param designedMiner
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Boolean getBlockFromMiner(List<IP> ipMiners, String hash, IP designedMiner) throws IOException, ExecutionException, InterruptedException {

		System.out.println("Hash richiesta:" + hash + "\n");
		// Type type = new TypeToken<Block>() {
		// }.getType();
		// // TODO RENDERE ASYNCRONA E SPOSTARE IN ASYNCH
		//
		//
		Block blockResponse = asyncRequest.getBlockFromHash(designedMiner, hash);
		if (blockResponse != null) {
			System.out.println("\n Block response branch update: " + blockResponse.getHashBlock() + "\n");

			// TODO miner.verifyBlock(blockResponse)
			if (verifyBlock(blockResponse)) {

				// blockRepository.save(blockResponse);
				return Boolean.TRUE;
			} else {
				// Elimino il miner se il blocco non è verificato
				ipMiners.remove(designedMiner);
				System.err.println("Il miner " + designedMiner.getIp() + " ha inviato un blocco non corretto, lo elimino dalla lista.");
				return Boolean.FALSE;
			}
			// System.out.println("Ho tirato fuori il blocco con chainLevel: " + b.getChainLevel() + "\n");

		} else {
			// Il miner non ha risposto, lo elimino
			ipMiners.remove(designedMiner);
			return Boolean.FALSE;
		}

	}

	/**
	 * @param b
	 *            blocco che cerco
	 * @return
	 * @throws InterruptedException
	 * @throws ExecutionException
	 * @throws IOException
	 */
	public Boolean verifyBlock(Block b) throws InterruptedException, ExecutionException, IOException {

		Boolean result = Boolean.FALSE;
		// nell primo updateh
		System.out.println("\n" + b.getFatherBlockContainer());

		// cerco il padre nel mio db
		Block bFather = blockRepository.findByhashBlock(b.getFatherBlockContainer());
		// non lo trovo lo cerco dai miner
		if (bFather == null) {
			if (!connectionServiceImpl.firstConnectToEntryPoint())
				System.err.println("errore get lista ip");
			result = updateBranChain(b.getFatherBlockContainer());
		}
		// se io ce l ho oppure l hotrovato in rete controllo il figlio e torno
		// true

		bFather = blockRepository.findByhashBlock(b.getFatherBlockContainer());

		if ((bFather != null || result) && verifySerice.singleBlockVerify(b)) {
			User u = b.getUserContainer();
			if (userRepo.findByPublicKeyHash(u.getPublicKeyHash()) == null)
				userRepo.save(u);

			for (Transaction trans : b.getTransactionsContainer()) {
				trans.setBlockContainer(b.getHashBlock());// TODO checkitout looponserito da wualcuno quando non arrivano i utenti nelle tran vedere se tolto funziona
				u = userRepo.findByPublicKeyHash(trans.getAuthorContainer().getPublicKeyHash());
				if (u == null) {
					userRepo.save(trans.getAuthorContainer());
				}
			}
			System.out.println("Salvo il blocco");
			// Salvo il blocco nella catena
			blockRepository.save(b);
			return Boolean.TRUE;
		}
		return Boolean.FALSE;

	}

	/**
	 * @param ipMiners
	 * @param designedMiner
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private Boolean getBlocksFromMiner(List<IP> ipMiners, Pairs<IP, Integer> designedMiner) throws IOException, ExecutionException, InterruptedException {

		Integer myChainLevel = blockRepository.findFirstByOrderByChainLevelDesc().getChainLevel() + 1;
		ArrayList<Block> blockResponse = (ArrayList<Block>) asyncRequest.getBlocksFromChainLevel(designedMiner, myChainLevel);
		Block b;
		if (blockResponse != null) {
			System.out.println("\n Numero di blocchi ricevuti da block chain update from level: " + blockResponse.size());

			for (int i = 0; i < blockResponse.size(); i++) {

				System.out.println("Blocco ricevuto: " + blockResponse.get(i));

				// miner.verifyBlock(b, blockRepository) TODO
				b = blockResponse.get(i);
				if (verifyBlock(b)) {
					// for (Transaction t : b.getTransactionsContainer())
					// t.setBlockContainer(b.getHashBlock());
					// blockRepository.save(b);

					// Se il miner attuale ha un livello minore o uguale al mio lo elimino
					if (designedMiner.getValue2() <= blockRepository.findFirstByOrderByChainLevelDesc().getChainLevel()) {
						ipMiners.remove(designedMiner.getValue1());
					}

					return Boolean.TRUE;

				} else {
					// Elimino il miner se il blocco non è verificato
					ipMiners.remove(designedMiner.getValue1());
					System.err.println("Il miner " + designedMiner.getValue1() + " ha inviato un blocco non corretto, lo elimino.");
				}
				// System.out.println("Ho tirato fuori il blocco con chainLevel: " + b.getChainLevel() + "\n");
			}
		}
		return Boolean.FALSE;

		// System.out.println("2");

	}

	@Async
	public Future<Boolean> onNewBlockArrived(Block block) {

		System.out.println("Nuovo blocco arrivato, verifico...");

		Boolean isVerified = Boolean.FALSE;

		Integer heightBFS = blockRepository.findFirstByOrderByChainLevelDesc().getChainLevel();
		// Se ho già il blocco nella catena termina.
		if (blockRepository.findByhashBlock(block.getHashBlock()) != null)
			return new AsyncResult<Boolean>(isVerified);

		try {

			isVerified = verifyBlock(block);

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Blocco valido? " + isVerified);
		if (isVerified) {
			// Stoppo il processo di mining
			// mi salvo l altrezza prima dell inserimento
			// Salvo il blocco nella catena
			// blockRepository.save(block);
			// se il blocco è con chain level maggiore del mio blocco il mining
			if (block.getChainLevel() > heightBFS) {
				flagNewBlock = Boolean.TRUE;
				miningService.setStopMining(Boolean.TRUE);
				// Aggiorno il servizio di mining
				miningService.updateMiningService();//

			}
		}

		return new AsyncResult<Boolean>(isVerified);
	}

	// Metodo che avvia il Mining del miner e ne gestisce interruzione
	public void manageMine() {

		Integer i = 0;
		miningService.initializeService();
		Future<Boolean> response = null;
		while (flagRunningMinining) {
			// Reimposto la variabile di stop di mining a false
			miningService.setStopMining(Boolean.FALSE);
			if (response != null) {
				response.cancel(Boolean.TRUE);
				response = null;
			}
			System.out.println("Richiesta asincrona");
			try {
				miningService.updateMiningService();
				// Reimposto la variabile di arrivo nuovo blocco a false
				flagNewBlock = Boolean.FALSE;
				response = miningService.mine(i);
				i++;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println("Sono asincrona?");

			do {
				System.out.println("Riposo ZzZzZ");
				try {
					Thread.sleep(2500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} while (!response.isDone() && flagNewBlock == Boolean.FALSE && flagRunningMinining);

			miningService.setStopMining(Boolean.TRUE);

			if (response != null) {
				System.out.println("Il miner stava minando ed è stato bloccato");
			}

			System.out.println("Ho aspettato la risposta: " + response.isDone() + " oppure mi è arrivato il blocco: " + flagNewBlock + " oppure ho fermato il mining");
			// flagNewBlock=Boolean.FALSE;

		}

		if (response != null) {
			miningService.setStopMining(Boolean.TRUE);
			response.cancel(Boolean.TRUE);
		}

		System.out.println("Il miner è stato fermato con successo");
	}

	@Async
	public void startMining() {

		initializeFilechain();
		setFlagRunningMinining(Boolean.TRUE);
		update();
		manageMine();
	}

	/**
	 * 
	 */
	private Block getFatherBlock() {

		Integer count = 0;
		Integer cLevel = blockRepository.findFirstByOrderByChainLevelDesc().getChainLevel();
		if (cLevel - KMAXLEVEL <= 0)
			return blockRepository.findBychainLevel(0).get(0);
		Boolean flag = Boolean.TRUE;

		Set<Block> blocksTemp = new HashSet<Block>();
		Set<Block> blocksTemp2 = new HashSet<Block>();

		// Aggiungo i blocchi che sono all utlimo livello
		blocksTemp.addAll(blockRepository.findBychainLevel(cLevel));
		while (flag) {
			count++;
			// per ogni blocco nell ultimo livello risalgo la catena
			// ovvero aggiungo i padri
			for (Block b : blocksTemp)
				blocksTemp2.add(blockRepository.findByhashBlock(b.getFatherBlockContainer()));
			// se il livello di paranoia è maggiore di 0
			// aggiungo anche i blocchi concorrenziali
			if (count < KMAXLEVEL)
				blocksTemp2.addAll(blockRepository.findBychainLevel(cLevel - count));

			blocksTemp.clear();
			blocksTemp.addAll(blocksTemp2);
			blocksTemp2.clear();

			if (blocksTemp.size() == 1 && count >= KMAXLEVEL)
				flag = Boolean.FALSE;

		}
		for (Block b : blocksTemp)
			return b;
		return blockRepository.findBychainLevel(0).get(0);
	}

	/**
	 * @param b
	 * @return
	 */
	private List<Transaction> getAllTransFromHash(Block b) {

		List<Transaction> transList = new ArrayList<>();

		// Questo get block non serve perchè senò stiamo ritornando il figlio del blocco da cui dobbiamo prende le transazioni e quindi ci perdiamo
		// le transazioni del blocco B che abbiamo calcolato sopra
		// Block b = blockRepository.findByhashBlock(f.getFatherBlockContainer());
		System.out.println("Blocco del padre per ricavare hash: " + b.toString());

		while (b.getFatherBlockContainer() != null && transList != null) {
			transList.addAll(b.getTransactionsContainer());
			b = blockRepository.findByhashBlock(b.getFatherBlockContainer());
		}
		System.out.println("Tutte le citazioni: " + transList.toString());
		return transList;

	}

	/**
	 * @return
	 */
	public List<Transaction> getAllAvalaibleCit() {

		return getAllTransFromHash(getFatherBlock());

	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {

		// TODO Auto-generated method stub
		return super.hashCode();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {

		// TODO Auto-generated method stub
		return super.equals(obj);
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	protected Object clone() throws CloneNotSupportedException {

		// TODO Auto-generated method stub
		return super.clone();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {

		// TODO Auto-generated method stub
		return super.toString();
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#finalize()
	 */

	/**
	 * @return the miningService
	 */
	public IMiningService getMiningService() {

		return miningService;
	}

	/**
	 * @param miningService
	 *            the miningService to set
	 */
	public void setMiningService(IMiningService miningService) {

		this.miningService = miningService;
	}

	public Boolean getFlagRunningMinining() {

		return flagRunningMinining;
	}

	public void setFlagRunningMinining(Boolean flagRunningMinining) {

		this.flagRunningMinining = flagRunningMinining;
	}
	// @Override
	// public void inject(BeansManager beansManager) {
	//
	// asyncRequest = beansManager.getAsyncRequest();
	// ipService = beansManager.getiPService();
	// // blockRepository = beansManager.getBlockRepository;
	// }

	@SuppressWarnings("unchecked")
	private List<Transaction> getTransFromDisp(Integer nTrans) throws Exception {
		// TODO List<Transaction> trans = HttpUtil.doGetJSON("http://" + getEntryPointBaseUri() +

		// Type type = new TypeToken<List<Transaction>>() {
		// }.getType();
		// List<Transaction> trans = HttpUtil.doGetJSON("http://" + "10.198.0.7" + ":8080/JsonTransaction?nTrans=" + nTrans, type);

		return null;
	}

}
