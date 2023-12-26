package br.com.acgm.services;

import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.acgm.controller.PersonController;
import br.com.acgm.data.vo.v1.PersonVO;
import br.com.acgm.data.vo.v2.PersonVOV2;
import br.com.acgm.exceptions.RequiredObjectIsNullException;
import br.com.acgm.exceptions.ResourceNotFoundException;
import br.com.acgm.mapper.DozerMapper;
import br.com.acgm.mapper.custom.PersonMapper;
import br.com.acgm.model.Person;
import br.com.acgm.repository.PersonRepository;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service //spring instancia o objeto onde for usado
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired 
	PersonRepository repository;
	
	@Autowired 
	PersonMapper mapper;
	
	public PersonVO findById(Long id) {
		
		logger.info("procurando um pessoa");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("nao existe regristo"));
		
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		return vo;
	}
	
	public List<PersonVO> findAll() {
		
		logger.info("procurando todas pessoas");
		
		var persons = DozerMapper.parseListObjects(repository.findAll(),PersonVO.class);
		
		persons.stream().forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel()));
		
		return persons;
	}

	
	public PersonVO create(PersonVO personVO) {
		
		if(personVO == null) throw new RequiredObjectIsNullException();
		
		logger.info("criando 1 pessoa");
		
		var entity = DozerMapper.parseObject(personVO,Person.class);
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
		
		return vo;
		
	}
	
	public PersonVO update(PersonVO personVO) {
		
		if(personVO == null) throw new RequiredObjectIsNullException();
		
		logger.info("editando 1 pessoa");
		
		var entity = repository.findById(personVO.getId()).orElseThrow(() -> new ResourceNotFoundException("nao existe regristo"));
		
		entity.setAddress(personVO.getAddress());
		entity.setFirstName(personVO.getFirstName());
		entity.setLastName(personVO.getLastName());
		entity.setGender(personVO.getGender());
		
		var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
		
		vo.add(linkTo(methodOn(PersonController.class).findById(vo.getId())).withSelfRel());
		
		return vo;
	}
	
	public void delete(Long id) {
		
		logger.info("deletando 1 pessoa");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("nao existe regristo"));
		
		repository.delete(entity);
	}

	public PersonVOV2 createV2(PersonVOV2 personVOV2) {
		
		logger.info("criando 1 pessoa com V2");
		
		var entity = mapper.convertVoToEntity(personVOV2);
		
		var vo = mapper.convertEntityToVo(repository.save(entity));
		
		return vo;
		
	}
}
