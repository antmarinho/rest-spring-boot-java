package br.com.acgm.services;

import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
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
import jakarta.transaction.Transactional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service //spring instancia o objeto onde for usado
public class PersonServices {
	
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired 
	PersonRepository repository;
	
	@Autowired 
	PagedResourcesAssembler<PersonVO> assembler;
	
	@Autowired 
	PersonMapper mapper;
	
	public PersonVO findById(Long id) {
		
		logger.info("procurando um pessoa");
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("nao existe regristo"));
		
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
		return vo;
	}
	
	public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
		
		logger.info("procurando todas pessoas");
		
		var personPage = repository.findAll(pageable);
		
		var personVOPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		
		personVOPage.map((p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel())));

		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(),pageable.getPageSize(),"asc")).withSelfRel();
		
		return assembler.toModel(personVOPage,link);
	}
	
	public PagedModel<EntityModel<PersonVO>> findPersonByName(String firstName, Pageable pageable) {
		
		logger.info("procurando todas pessoas");
		
		var personPage = repository.findPersonByName(firstName,pageable);
		
		var personVOPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
		
		personVOPage.map((p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getId())).withSelfRel())));
		
		Link link = linkTo(methodOn(PersonController.class).findAll(pageable.getPageNumber(),pageable.getPageSize(),"asc")).withSelfRel();
		
		return assembler.toModel(personVOPage,link);
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
	
	@Transactional //para query de modificacao ou add precisa desse e do @Modifying na query no repositorio
	public PersonVO disablePerson(Long id) {
		
		logger.info("Desabilitando uma pessoa");
		
		repository.disablePerson(id);
		
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("nao existe regristo"));
		
		var vo = DozerMapper.parseObject(entity, PersonVO.class);
		
		vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
		
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
