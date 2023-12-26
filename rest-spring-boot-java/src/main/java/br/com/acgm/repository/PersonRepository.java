package br.com.acgm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import br.com.acgm.model.Person;


@Repository
public interface PersonRepository extends JpaRepository<Person, Long>{

}
