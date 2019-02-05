package cs.scrs.miner.dao.transaction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


@RepositoryRestResource(collectionResourceRel = "trsnsaction", path = "transactions")
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findByhashFile(String hashBlock);

    
    //torna lista perche potrebbe esserci lo stesso file su piu branch
    List<Transaction> findByHashFile(@Param("hashFile") String hashFile);
    
    Transaction findByHashTransBlock(@Param("hashTransBlock") String hashTransBlock);
    

}
