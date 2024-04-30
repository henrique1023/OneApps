package br.com.one.services;

import br.com.one.controllers.UsuarioController;
import br.com.one.data.vo.v1.UserVO;
import br.com.one.exceptions.RequiredObjectisNullException;
import br.com.one.exceptions.ResponseEntityExceptionHandler;
import br.com.one.mapper.DozerMapper;
import br.com.one.model.User;
import br.com.one.repositories.UserRepository;
import br.com.one.util.UtilsSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class UserServices implements UserDetailsService {
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(UserServices.class.getName());

    @Autowired
    UserRepository repository;

    @Autowired
    PagedResourcesAssembler<UserVO> assembler;

    public UserServices(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Finding one User by name!");
        var user = repository.findByUsername(username);
        if (user != null){
            return user;
        }else {
            throw new UsernameNotFoundException("" + username + " not found");
        }
    }

    public UserVO findById(Long id) throws Exception {

        var entity = repository.findById(id).orElseThrow(() ->
                new ResponseEntityExceptionHandler("No records found for this ID"));

        var vo = DozerMapper.parseObject(entity, UserVO.class);

        vo.add(linkTo(methodOn(UsuarioController.class).findById(id)).withSelfRel());
        return vo;
    }

    public void delete(Long id) throws Exception {

        var entity = repository.findById(id).orElseThrow(() ->
                new ResponseEntityExceptionHandler("No records found for this ID"));

        repository.delete(entity);
    }

    public PagedModel<EntityModel<UserVO>> findAll(Pageable pageable){
        logger.info("Finding All Users!");

        var userPage = repository.findAll(pageable);
        return getListVos(pageable, userPage);
    }

    public UserVO create(User usuarioNovo) throws Exception {
        if(usuarioNovo == null)
            throw new RequiredObjectisNullException();

        logger.info("Criate one User!");
        usuarioNovo.setPassword(UtilsSecurity.convertPassword(usuarioNovo.getPassword()));
        var vo = DozerMapper.parseObject(repository.save(usuarioNovo), UserVO.class);
        vo.add(linkTo(methodOn(UsuarioController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public UserVO update(User usuarioAtualizacao) throws Exception {
        if(usuarioAtualizacao == null)
            throw new RequiredObjectisNullException();

        logger.info("Update one User!");

        User usuarioBanco = repository.findById(usuarioAtualizacao.getId()).orElseThrow(() ->
                new ResponseEntityExceptionHandler("No records found for this ID"));

        usuarioBanco.setUserName(usuarioAtualizacao.getUserName());
        usuarioBanco.setEmail(usuarioAtualizacao.getEmail());
        usuarioBanco.setEnabled(usuarioAtualizacao.isEnabled());
        if(usuarioAtualizacao.getPassword() != null && !usuarioAtualizacao.getPassword().isBlank())
            usuarioBanco.setPassword(UtilsSecurity.convertPassword(usuarioAtualizacao.getPassword()));

        var vo = DozerMapper.parseObject(repository.save(usuarioBanco), UserVO.class);
        vo.add(linkTo(methodOn(UsuarioController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    private PagedModel<EntityModel<UserVO>> getListVos(Pageable pageable, Page<User> userPage) {
        var usersVosPage = userPage.map(u -> DozerMapper.parseObject(u, UserVO.class));
        usersVosPage.map(u -> {
            try {
                return u.add(linkTo(methodOn(UsuarioController.class)
                        .findById(u.getKey())).withSelfRel());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        Link link = linkTo(methodOn(UsuarioController.class)
                .findByAll(pageable.getPageNumber(), pageable.getPageSize(), "asc")).withSelfRel();

        return assembler.toModel(usersVosPage, link);
    }
}
