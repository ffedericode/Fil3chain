package cs.scrs.miner.dao.block;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;



// findTop10BychainLevelOrderBychainLeveleAsc(Integer cLevel);

@RepositoryRestResource(collectionResourceRel = "block", path = "blocks")
public interface BlockRepository extends CrudRepository<Block, Long> {


	List<Block> findAll();

	Block findFirstByOrderByChainLevelDesc();

	Block findByhashBlock(@Param("hashBlock") String hashBlock);

	List<Block> findBychainLevel(@Param("cLevel") Integer cLevel);
	
	List<Block> findBychainLevelGreaterThan(@Param("cLevel") Integer cLevel);

	List<Block> findByChainLevelGreaterThanOrderByChainLevelDesc(@Param("cLevel") Integer cLevel);

	List<Block> findTop10ByChainLevelGreaterThanOrderByChainLevelDesc(@Param("cLevel") Integer cLevel);

	List<Block> findByChainLevelBetweenOrderByChainLevelAsc(@Param("inf") Integer inf,@Param("sup") Integer sup);

}
