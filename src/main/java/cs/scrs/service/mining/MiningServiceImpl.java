package cs.scrs.service.mining;


import cs.scrs.config.KeysConfig;
import cs.scrs.miner.dao.block.Block;
import cs.scrs.miner.dao.block.BlockRepository;
import cs.scrs.miner.dao.citations.Citation;
import cs.scrs.miner.dao.transaction.Transaction;
import cs.scrs.miner.dao.transaction.TransactionRepository;
import cs.scrs.miner.dao.user.User;
import cs.scrs.miner.dao.user.UserRepository;
import cs.scrs.miner.models.IP;
import cs.scrs.miner.models.MerkleTree;
import cs.scrs.service.connection.ConnectionServiceImpl;
import cs.scrs.service.ip.IPServiceImpl;
import cs.scrs.service.poolDispatcher.PoolDispatcherServiceImpl;
import cs.scrs.service.request.AsyncRequest;
import cs.scrs.service.util.AudioUtil;
import cs.scrs.service.util.CryptoUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.Future;


/**
 */
@Service
public class MiningServiceImpl implements IMiningService {

    // Blocco da minare
    private Block block;

    // Chiave privata del creatore del blocco
    private String privateKey;

    // Difficoltà in cui si sta minando
    private Integer difficulty;

    // Maschera per il check dell'hash nei byte interi
    private Integer fullMask;

    // Maschera per il check dell'hash nel byte di "resto"
    private byte restMask;

    // Callback chiamata dopo l'interruzione del thread
    private Runnable interruptCallback;

    // Chiave pubblica dell'autore del blocco
    private String publicKey;

    // Blocco precedente nella catena
    private Block previousBlock;

    // Lista di transazioni presente nel blocco
    private List<Transaction> transactions;

    // Potenza media di calcolo della macchina
    private Float averagePowerMachine = 0f;

    @Autowired
    private UserRepository userRepository;
    // Block repository
    @Autowired
    private BlockRepository blockRepository;
    @Autowired
    private TransactionRepository transRepo;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private ConnectionServiceImpl connectionServiceImpl;
    @Autowired
    private IPServiceImpl ipService;
    @Autowired
    private PoolDispatcherServiceImpl poolDispService;

    private Boolean stopMining;

    @Autowired
    private KeysConfig keysConfigProperties;


    @PostConstruct
    public void init() {

        System.out.println("MiningServiceImpl init method called");
        this.publicKey = keysConfigProperties.getPublicKey();
        this.privateKey = keysConfigProperties.getPrivateKey();
        this.block = null;
        this.difficulty = -1;
        this.fullMask = 0;
        this.restMask = (byte) 0b11111111;
        this.interruptCallback = null;
    }

    /**
     * Metodo per calcolare le maschere per effettuare il check dell'hash
     */
    private void calculateMasks() {

        // Calcolo full mask
        fullMask = difficulty / 8;

        // Calcolo rest mask
        int restanti = difficulty % 8;

        if (restanti == 0) {
            restMask = 0b00000000;
        } else {
            restMask = (byte) 0b11111111;
            restMask = (byte) (restMask << (8 - restanti));
        }
    }

	/*
     * (non-Javadoc)
	 * @see cs.scrs.service.mining.IMiningServiceImpl#run()
	 */

    /**
     * Metodo per minare un blocco
     */
    @Async
    public Future<Boolean> mine(Integer i) throws Exception {

        if (block == null)
            initializeService();
        if (difficulty == -1) {
            System.err.println("Complessità per il calcolo del blocco errata, impossibile minare");
            return new AsyncResult<Boolean>(Boolean.FALSE);
        }
        if (transactions.isEmpty() || block.getUserContainer() == null)
            return new AsyncResult<Boolean>(Boolean.FALSE);

        // Calcolo le maschere per il check dell'hash.
        calculateMasks();

        // Tempo di inizio mining
        long startTime = new Date().getTime();

        // Nonce
        Integer nonce = new Random().nextInt();
        Integer nonceStart = nonce;
        Integer nonceFinish = 0;
        float totalTime = 0;

        System.out.println("Nonce di partenza: " + nonce);

        // Hash del blocco
        byte[] hash;

        System.out.println("Sono il miner numero : " + i);
        do {
            // Genera nuovo hash 256
            hash = DigestUtils.sha256(block.toString() + nonce);
            // Incremento il nonce
            nonce++;
            if (nonce % 1000000 == 0)
                System.out.println("Sono il miner numero : " + i + " e sto minando con nonce " + nonce + " sto trovando il Blocco con ChainLevel " + block.getChainLevel());

        } while (!verifyHash(hash) && !stopMining);
        System.out.println("Sono il miner numero : " + i + " e mi sono fermato a minare");
        if (stopMining) {
            return new AsyncResult<Boolean>(Boolean.TRUE);
        }
        AudioUtil.alert(); // avviso sonoro
        nonceFinish = nonce - 1;
        totalTime = (new Date().getTime() - startTime) / 1000.0f;

        // Calcolo hash corretto in esadecimale
        // Spiegazione nonce - 1: Viene fatto -1 perché nell'ultima iterazione
        // viene incrementato anche se l'hash era corretto.
        String hexHash = DigestUtils.sha256Hex(block.toString() + (nonce - 1));

        // Impostazione dell'hash e del nonce
        block.setHashBlock(hexHash);
        block.setNonce(nonce - 1);

        // private key null
        block.setSignature(CryptoUtil.sign(hexHash, privateKey));
        block.setMinerPublicKey(publicKey);
        block.setFatherBlockContainer(previousBlock.getHashBlock());

        // per ogni transazione mette il riferimento al blocco container
        int indexInBlock = 0;
        User temp;
        for (Transaction trans : transactions) {
            temp = userRepository.findByPublicKeyHash(trans.getAuthorContainer().getPublicKeyHash());
            if (temp == null) {
                userRepository.save(trans.getAuthorContainer());
            }
            trans.setBlockContainer(block.getHashBlock());
            trans.setIndexInBlock(indexInBlock);
            System.out.println(trans.getIndexInBlock());

            //Genero il nuovo hash della transaziona in relazione al vecchio Hash e al blocco in cui è contenuta
            //In questo modo avrò delle transazioni con HashFile sempre univoco perchè ora tiene conto del blocco in cui è contenuta
            String newTransHash = DigestUtils.sha256Hex(trans.getHashFile() + trans.getBlockContainer());
            trans.setHashTransBlock(newTransHash);
            //Aggiorno ora tutte le citazioni all'interno della transazioni con il nuovo
            //Hash a cui dovranno fare riferimento
            for (Citation cit : trans.getCitations()) {
                cit.getKey().setHashCiting(trans.getHashTransBlock());
            }


            // transRepo.save(trans);
            indexInBlock++;
        }

        Float calculatePower = (((Math.abs(nonceFinish - nonceStart)) / totalTime) / 1000000.0f);
        if (averagePowerMachine != 0f)
            avgPower(calculatePower);
        else
            averagePowerMachine = calculatePower;

        block.setTransactionsContainer(transactions);
        block.setCreationTime(Long.toString(System.currentTimeMillis()));
        System.out.println("Hash trovato: " + block.getHashBlock() + " con difficoltà: " + difficulty + " Nonce: " + nonce + " Tempo impiegato: " + totalTime + " secondi");
        System.out.println("Hash provati: " + (Math.abs(nonceFinish - nonceStart)) + " HashRate: " + calculatePower + " MH/s");

        // Salvo il blocco
        try {
            if (blockRepository != null)
                blockRepository.save(block);
            sendBlockToMiners();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return new AsyncResult<Boolean>(Boolean.TRUE);
    }

    @Async
    public Future<List<Block>> sendBlockToMiners() throws InterruptedException {

        System.out.println(restTemplate.toString());

        HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory();
        rf.setReadTimeout(1000 * 10);
        rf.setConnectTimeout(1000 * 10);
        rf.setConnectionRequestTimeout(1000 * 10);
        restTemplate.setRequestFactory(rf);

        List<Block> blocks = new ArrayList<Block>();
        String bool = Boolean.FALSE.toString();
        Map<IP, Integer> map = new HashMap<IP, Integer>();
        Map<IP, Integer> counter = Collections.synchronizedMap(map);
        connectionServiceImpl.firstConnectToEntryPoint();
        List<IP> minerList = new ArrayList<IP>();
        synchronized (counter) {
            for (IP ip : ipService.getIPList()) {
                counter.put(ip, 0);
                minerList.add(ip);
            }

            System.out.println("Dimensione lista hashmap " + counter.size());

        }

        while (counter.size() > 0) {

            for (IP ip : minerList) {
                System.out.println("Invio blocco a: " + ip.getIp());
                try {
                    // String response = HttpUtil.doPost("http://" + ip.getIp() + "/fil3chain/newBlock",
                    // JsonUtility.toJson(block));
                    String response = restTemplate.postForObject("http://" + ip.getIp() + "/fil3chain/newBlock", block, String.class);
                    System.out.println("Ho inviato il blocco e mi è ritornato come risposta: " + response);
                    synchronized (counter) {
                        // Se ho mandato il blocco rimuovo il miner
                        counter.remove(ip);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Thread.sleep(1000);
                    System.out.println("Il miner " + ip.getIp() + " non è più connesso.");
                    System.out.println("Errore invio blocco: " + bool);
                } finally {
                    synchronized (counter) {
                        // altrimenti aumenta il counter di uno
                        if (counter.get(ip) != null) {
                            counter.put(ip, counter.get(ip) + 1);
                            if (counter.get(ip) > AsyncRequest.REQNUMBER)
                                counter.remove(ip);

                        }
                    }
                }
            }

        }

        // Annullo il blocco appena minato
        block = null;

        return new AsyncResult<>(blocks);
    }

    private Boolean verifyHash(byte[] hash) {

        // Verifica dei primi fullMask byte interi
        for (int i = 0; i < fullMask; i++) {
            if (hash[i] != 0) {
                return false;
            }
        }

        // Se non ci sono bit restanti allora restituisce true
        if (restMask == 0)
            return true;

        // Altrimenti controlla i bit rimanenti
        return (hash[fullMask] & restMask) == 0;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getBlock()
     */
    @Override
    public Block getBlock() {

        return block;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#updateService(cs.scrs.miner.dao.block.Block, cs.scrs.miner.dao.block.Block, int, java.util.List)
     */
    @Override
    public void updateService(Block miningBlock, Block previousBlock, int difficulty, List<Transaction> transactionList) {

        // TODO MERGE !
        System.out.println("Update service");
        this.block = miningBlock;
        this.previousBlock = previousBlock;
        this.difficulty = difficulty;
        this.transactions = transactionList;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#initializeService()
     */
    @Override
    public void initializeService() {

        // TODO MERGE !
        System.out.println("Inizializza servizio");
        User user = userRepository.findByPublicKey(keysConfigProperties.getPublicKey());
        user.setPassword(null);
        System.out.println("User founded " + user);

        // Prendo l'ultmo blocco della catena
        Block lastBlock = blockRepository.findFirstByOrderByChainLevelDesc();

        // Inizializzo il nuovo blocco da minare
        block = new Block();
        block.setFatherBlockContainer(lastBlock.getHashBlock());
        block.setChainLevel(lastBlock.getChainLevel() + 1);
        block.setMinerPublicKey(publicKey);
        block.setUserContainer(user);
        // Prendo le transazioni dal Pool Dispatcher
        List<Transaction> transactionsList = poolDispService.getTransactions(lastBlock);

        ArrayList<String> hashTransactions = new ArrayList<>();
        for (Transaction transaction : transactionsList) {
            hashTransactions.add(transaction.getHashFile());
        }
        block.setMerkleRoot(MerkleTree.buildMerkleTree(hashTransactions));

        // Test chiamata per difficoltà
        Integer complexity = poolDispService.getCurrentComplexity();

        previousBlock = lastBlock;
        difficulty = complexity;
        transactions = transactionsList;
    }

    /*
     * (non-Javadoc)p
     * @see cs.scrs.service.mining.IMiningServiceImpl#updateMiningService()
     */
    @Override
    public void updateMiningService() {

        // TODO MERGE !
        // Prendo l'ultmo blocco della catena
        Block lastBlock = blockRepository.findFirstByOrderByChainLevelDesc();
        // Inizializzo il nuovo blocco da minare
        Block newBlock = new Block();
        newBlock.setFatherBlockContainer(lastBlock.getHashBlock());
        newBlock.setChainLevel(lastBlock.getChainLevel() + 1);
        newBlock.setMinerPublicKey(publicKey);
        User user = userRepository.findByPublicKey(keysConfigProperties.getPublicKey());
        user.setPassword(null);
        newBlock.setUserContainer(user);
        // Prendo le transazioni dal Pool Dispatcher
        List<Transaction> transactionsList = poolDispService.getTransactions(lastBlock);

        ArrayList<String> hashTransactions = new ArrayList<>();
        for (Transaction transaction : transactionsList) {
            hashTransactions.add(transaction.getHashFile());
        }

        newBlock.setMerkleRoot(MerkleTree.buildMerkleTree(hashTransactions));

        // Test chiamata per difficoltà
        Integer complexity = poolDispService.getCurrentComplexity();

        updateService(newBlock, lastBlock, complexity, transactionsList);
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setBlock(cs.scrs.miner.dao.block.Block)
     */
    @Override
    public void setBlock(Block block) {

        this.block = block;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getPrivateKey()
     */
    @Override
    public String getPrivateKey() {

        return privateKey;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setPrivateKey(java.lang.String)
     */
    @Override
    public void setPrivateKey(String privateKey) {

        this.privateKey = privateKey;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getDifficulty()
     */
    @Override
    public Integer getDifficulty() {

        return difficulty;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setDifficulty(java.lang.Integer)
     */
    @Override
    public void setDifficulty(Integer difficulty) {

        this.difficulty = difficulty;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#isInitialized()
     */
    @Override
    public Boolean isInitialized() {

        return (block != null && difficulty != -1);
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getFullMask()
     */
    @Override
    public Integer getFullMask() {

        return fullMask;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setFullMask(java.lang.Integer)
     */
    @Override
    public void setFullMask(Integer fullMask) {

        this.fullMask = fullMask;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getRestMask()
     */
    @Override
    public byte getRestMask() {

        return restMask;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setRestMask(byte)
     */
    @Override
    public void setRestMask(byte restMask) {

        this.restMask = restMask;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getInterruptCallback()
     */
    @Override
    public Runnable getInterruptCallback() {

        return interruptCallback;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setInterruptCallback(java.lang.Runnable)
     */
    @Override
    public void setInterruptCallback(Runnable interruptCallback) {

        this.interruptCallback = interruptCallback;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getPublicKey()
     */
    @Override
    public String getPublicKey() {

        return publicKey;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setPublicKey(java.lang.String)
     */
    @Override
    public void setPublicKey(String publicKey) {

        this.publicKey = publicKey;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getPreviousBlock()
     */
    @Override
    public Block getPreviousBlock() {

        return previousBlock;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setPreviousBlock(cs.scrs.miner.dao.block.Block)
     */
    @Override
    public void setPreviousBlock(Block previousBlock) {

        this.previousBlock = previousBlock;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getTransactions()
     */
    @Override
    public List<Transaction> getTransactions() {

        return transactions;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setTransactions(java.util.List)
     */
    @Override
    public void setTransactions(List<Transaction> transactions) {

        this.transactions = transactions;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getBlockRepository()
     */
    @Override
    public BlockRepository getBlockRepository() {

        return blockRepository;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setBlockRepository(cs.scrs.miner.dao.block.BlockRepository)
     */
    @Override
    public void setBlockRepository(BlockRepository blockRepository) {

        this.blockRepository = blockRepository;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getTransRepo()
     */
    @Override
    public TransactionRepository getTransRepo() {

        return transRepo;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setTransRepo(cs.scrs.miner.dao.transaction.TransactionRepository)
     */
    @Override
    public void setTransRepo(TransactionRepository transRepo) {

        this.transRepo = transRepo;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getRestTemplate()
     */
    @Override
    public RestTemplate getRestTemplate() {

        return restTemplate;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setRestTemplate(org.springframework.web.client.RestTemplate)
     */
    @Override
    public void setRestTemplate(RestTemplate restTemplate) {

        this.restTemplate = restTemplate;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getConnectionServiceImpl()
     */
    @Override
    public ConnectionServiceImpl getConnectionServiceImpl() {

        return connectionServiceImpl;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setConnectionServiceImpl(cs.scrs.service.connection.ConnectionServiceImpl)
     */
    @Override
    public void setConnectionServiceImpl(ConnectionServiceImpl connectionServiceImpl) {

        this.connectionServiceImpl = connectionServiceImpl;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getIpService()
     */
    @Override
    public IPServiceImpl getIpService() {

        return ipService;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setIpService(cs.scrs.service.ip.IPServiceImpl)
     */
    @Override
    public void setIpService(IPServiceImpl ipService) {

        this.ipService = ipService;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#getPoolDispService()
     */
    @Override
    public PoolDispatcherServiceImpl getPoolDispService() {

        return poolDispService;
    }

    /*
     * (non-Javadoc)
     * @see cs.scrs.service.mining.IMiningServiceImpl#setPoolDispService(cs.scrs.service.poolDispatcher.PoolDispatcherServiceImpl)
     */
    @Override
    public void setPoolDispService(PoolDispatcherServiceImpl poolDispService) {

        this.poolDispService = poolDispService;
    }

    public Boolean getStopMining() {

        return stopMining;
    }

    public void setStopMining(Boolean stopMining) {

        this.stopMining = stopMining;
    }

    private void avgPower(Float val) {

        averagePowerMachine = (averagePowerMachine + val) / 2;
    }

    public Float getAveragePowerMachine() {

        return averagePowerMachine;
    }
}
