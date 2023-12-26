package br.com.acgm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import br.com.acgm.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u from User u WHERE u.userName =:userName") //objeto user e nao a tabela
	User findByUserName(@Param("userName") String userName);
}
