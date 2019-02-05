package cs.scrs.miner.dao.login;

import cs.scrs.miner.dao.user.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "login", path = "login")
public interface LoginRepository extends CrudRepository<Login,String> {


	Login findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);

}
