package net.oneconsultoria.cbocontrol.services;

import jakarta.transaction.Transactional;
import net.oneconsultoria.cbocontrol.controllers.PersonController;
import net.oneconsultoria.cbocontrol.exceptions.RequiredObjectisNullException;
import net.oneconsultoria.cbocontrol.exceptions.ResponseEntityExceptionHandler;
import net.oneconsultoria.cbocontrol.mapper.DozerMapper;
import net.oneconsultoria.cbocontrol.mapper.custom.PersonMapper;
import net.oneconsultoria.cbocontrol.model.Person;
import net.oneconsultoria.cbocontrol.repositories.PersonRepository;
import net.oneconsultoria.cbocontrol.data.vo.v1.PersonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Service
public class PersonServices {
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonServices.class.getName());

    @Autowired
    PersonRepository repository;

    @Autowired
    PersonMapper mapper;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;

    public PersonVO findById(Long id) throws Exception {
        logger.info("Finding one person!");

        var entity = repository.findById(id).orElseThrow(() ->
                new ResponseEntityExceptionHandler("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PagedModel<EntityModel<PersonVO>> findByAll(Pageable pageable) {
        logger.info("Finding All persons!");

        var personPage = repository.findAll(pageable);
        return getListVos(pageable, personPage);
    }
     public PagedModel<EntityModel<PersonVO>> findPersonsByName(String firstName, Pageable pageable) {
        logger.info("Finding All persons!");

        var personPage = repository.findPersonsByName(firstName, pageable);
         return getListVos(pageable, personPage);
     }

    private PagedModel<EntityModel<PersonVO>> getListVos(Pageable pageable, Page<Person> personPage) {
        var personVosPage = personPage.map(p -> DozerMapper.parseObject(p, PersonVO.class));
        personVosPage.map(p -> {
            try {
                return p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Link link = linkTo(methodOn(PersonController.class)
                .findByAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }

    public PersonVO create(PersonVO p) throws Exception {
        if(p == null)
            throw new RequiredObjectisNullException();

        logger.info("creating a person!");

        var entity = DozerMapper.parseObject(p, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public PersonVO update(PersonVO p) throws Exception {

        if(p == null)
            throw new RequiredObjectisNullException();

        logger.info("Updating a person!");

        var entity = repository.findById(p.getKey()).orElseThrow(() ->
                new ResponseEntityExceptionHandler("No records found for this ID"));

        entity.setFirstName(p.getFirstName());
        entity.setLastName(p.getLastName());
        entity.setAddress(p.getAddress());
        entity.setGender(p.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    @Transactional
    public PersonVO disablePerson(Long id) throws Exception {
        logger.info("Disabling Person by id!");
        repository.disablePerson(id);

        var entity = repository.findById(id).orElseThrow(() ->
                new ResponseEntityExceptionHandler("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, PersonVO.class);

        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }
    public void delete(Long id){
        logger.info("Deleting a person!");

        var entity = repository.findById(id).orElseThrow(() ->
                new ResponseEntityExceptionHandler("No records found for this ID"));

        repository.delete(entity);
    }
}