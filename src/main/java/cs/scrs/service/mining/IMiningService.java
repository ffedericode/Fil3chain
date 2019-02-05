package cs.scrs.service.mining;


import cs.scrs.miner.dao.block.Block;
import cs.scrs.miner.dao.block.BlockRepository;
import cs.scrs.miner.dao.transaction.Transaction;
import cs.scrs.miner.dao.transaction.TransactionRepository;
import cs.scrs.service.connection.ConnectionServiceImpl;
import cs.scrs.service.ip.IPServiceImpl;
import cs.scrs.service.poolDispatcher.PoolDispatcherServiceImpl;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.Future;



public interface IMiningService{

	


	/**
	 * Metodo per minare un blocco
	 * @return 
	 */
	public Future<Boolean> mine(Integer i) throws Exception;

	public Future<List<Block>> sendBlockToMiners() throws InterruptedException;

	public Block getBlock();

	public void setBlock(Block block);

	public String getPrivateKey();

	public void setPrivateKey(String privateKey);

	public Integer getDifficulty();

	public void setDifficulty(Integer difficulty);

	public Boolean isInitialized();

	public void updateService(Block miningBlock, Block previousBlock, int difficulty, List<Transaction> transactionList);

	public void initializeService();

	public void updateMiningService();

	/**
	 * @return the fullMask
	 */
	public Integer getFullMask();

	/**
	 * @param fullMask the fullMask to set
	 */
	public void setFullMask(Integer fullMask);

	/**
	 * @return the restMask
	 */
	public byte getRestMask();

	/**
	 * @param restMask the restMask to set
	 */
	public void setRestMask(byte restMask);

	/**
	 * @return the interruptCallback
	 */
	public Runnable getInterruptCallback();

	/**
	 * @param interruptCallback the interruptCallback to set
	 */
	public void setInterruptCallback(Runnable interruptCallback);

	/**
	 * @return the publicKey
	 */
	public String getPublicKey();

	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(String publicKey);

	/**
	 * @return the previousBlock
	 */
	public Block getPreviousBlock();

	/**
	 * @param previousBlock the previousBlock to set
	 */
	public void setPreviousBlock(Block previousBlock);

	/**
	 * @return the transactions
	 */
	public List<Transaction> getTransactions();

	/**
	 * @param transactions the transactions to set
	 */
	public void setTransactions(List<Transaction> transactions);

	/**
	 * @return the blockRepository
	 */
	public BlockRepository getBlockRepository();

	/**
	 * @param blockRepository the blockRepository to set
	 */
	public void setBlockRepository(BlockRepository blockRepository);

	/**
	 * @return the transRepo
	 */
	public TransactionRepository getTransRepo();

	/**
	 * @param transRepo the transRepo to set
	 */
	public void setTransRepo(TransactionRepository transRepo);

	/**
	 * @return the restTemplate
	 */
	public RestTemplate getRestTemplate();

	/**
	 * @param restTemplate the restTemplate to set
	 */
	public void setRestTemplate(RestTemplate restTemplate);

	/**
	 * @return the connectionServiceImpl
	 */
	public ConnectionServiceImpl getConnectionServiceImpl();

	/**
	 * @param connectionServiceImpl the connectionServiceImpl to set
	 */
	public void setConnectionServiceImpl(ConnectionServiceImpl connectionServiceImpl);

	/**
	 * @return the ipService
	 */
	public IPServiceImpl getIpService();

	/**
	 * @param ipService the ipService to set
	 */
	public void setIpService(IPServiceImpl ipService);

	/**
	 * @return the poolDispService
	 */
	public PoolDispatcherServiceImpl getPoolDispService();

	/**
	 * @param poolDispService the poolDispService to set
	 */
	public void setPoolDispService(PoolDispatcherServiceImpl poolDispService);

	public Boolean getStopMining();

	public void setStopMining(Boolean stopMining);

	public Float getAveragePowerMachine();

}