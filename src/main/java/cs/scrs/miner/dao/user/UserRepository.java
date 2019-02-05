package cs.scrs.miner.dao.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "user", path = "users")
public interface UserRepository extends CrudRepository<User,String> {

	List<User> findByName(@Param("name") String name);

	User findByPublicKey(@Param("pubKey") String pubKey);
	User findByPublicKeyHash(@Param("pubKeyHash") String pubKeyHash);
	User findByUsernameAndPassword(@Param("username")String username,@Param("password")String password);

}
