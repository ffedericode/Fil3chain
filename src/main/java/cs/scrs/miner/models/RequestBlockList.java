package cs.scrs.miner.models;

import cs.scrs.miner.dao.block.Block;

import java.util.List;

public class RequestBlockList {
	/**
	 * 
	 */
	public RequestBlockList() {
		super();
	}

	private List<Block> blocks;

	
	/**
	 * @return the blocks
	 */
	public List<Block> getBlocks() {
	
		return blocks;
	}

	
	/**
	 * @param blocks the blocks to set
	 */
	public void setBlocks(List<Block> blocks) {
	
		this.blocks = blocks;
	}


	


	
	
}
