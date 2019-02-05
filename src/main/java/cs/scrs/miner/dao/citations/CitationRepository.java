package cs.scrs.miner.dao.citations;


import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;



// findTop10BychainLevelOrderBychainLeveleAsc(Integer cLevel);

@RepositoryRestResource(collectionResourceRel = "citation", path = "citations")
public interface CitationRepository extends CrudRepository<Citation, Long> {

	
}
