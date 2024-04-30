package br.com.one.controllers;

import br.com.one.data.vo.v1.UserVO;
import br.com.one.model.User;
import br.com.one.services.UserServices;
import br.com.one.util.MediaType;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Usuario", description = "Endpoint para controle de usuarios")
@RequestMapping("/api/usuario/v1")
public class UsuarioController {

    @Autowired
    private UserServices services;

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<PagedModel<EntityModel<UserVO>>> findByAll(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                                     @RequestParam(value = "size", defaultValue = "12") Integer size,
                                                                     @RequestParam(value = "direction", defaultValue = "asc") String direction) {
        var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "userName"));

        return ResponseEntity.ok(services.findAll(pageable));
    }

    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public UserVO findById(@PathVariable(value = "id") Long id) throws Exception {
        return services.findById(id);
    }

    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public UserVO cadastrar(@RequestBody User usuario) throws Exception {
        return services.create(usuario);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public UserVO atualizar(@RequestBody User usuario) throws Exception {
        return services.update(usuario);
    }

    @DeleteMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<?> deleteUsuario(@PathVariable(value = "id") Long id) throws Exception {
        services.delete(id);

        return ResponseEntity.noContent().build();
    }

}
