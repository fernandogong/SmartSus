package br.fiap.smartsus.controllers;

import br.fiap.smartsus.model.Usuario;
import br.fiap.smartsus.repository.UsuarioRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/usuarios")    
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping("")
    public EntityModel<Usuario> postUsuario(@RequestBody Usuario usuario) {
        Usuario savedUsuario = usuarioRepository.save(usuario);
        return EntityModel.of(savedUsuario, linkTo(methodOn(UsuarioController.class).getByIdUsuario(savedUsuario.getId())).withSelfRel());
    }

    @GetMapping("")
    public CollectionModel<EntityModel<Usuario>> getUsuario(Pageable pageable) {
        Page<Usuario> page = usuarioRepository.findAll(pageable);
        List<EntityModel<Usuario>> clinics = page.getContent().stream()
            .map(usuario -> EntityModel.of(usuario,
                linkTo(methodOn(UsuarioController.class).getByIdUsuario(usuario.getId())).withSelfRel(),
                linkTo(methodOn(UsuarioController.class).putUsuario(usuario.getId(), null)).withRel("updateUsuario"),
                linkTo(methodOn(UsuarioController.class).deleteUsuario(usuario.getId())).withRel("deleteUsuario")
            ))
            .collect(Collectors.toList());
        CollectionModel<EntityModel<Usuario>> resources = CollectionModel.of(clinics);
        resources.add(linkTo(methodOn(UsuarioController.class).getUsuario(pageable)).withSelfRel());
        return resources;
    }

    @GetMapping("/{id}")
    public EntityModel<Usuario> getByIdUsuario(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        if (usuario.isPresent()) {
            return EntityModel.of(usuario.get(),
            linkTo(methodOn(UsuarioController.class).getByIdUsuario(id)).withSelfRel(),
            linkTo(methodOn(UsuarioController.class).getUsuario(Pageable.unpaged())).withRel("usuarios"));
        } else {
            return EntityModel.of(new Usuario());
        }

    }

    @PutMapping("/{id}")
    public EntityModel<Usuario> putUsuario(@PathVariable Long id, @RequestBody Usuario usuarioUpdated) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            Usuario usuario = optionalUsuario.get();
            usuario.setEmail(usuarioUpdated.getEmail());
            usuario.setSenha(usuarioUpdated.getSenha());
            usuario.setCelular(usuarioUpdated.getCelular());
            Usuario usuarioSaved = usuarioRepository.save(usuario);
            return EntityModel.of(usuarioSaved, linkTo(methodOn(UsuarioController.class).getByIdUsuario(usuarioSaved.getId())).withSelfRel());
        } else {
            return EntityModel.of(new Usuario());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if (optionalUsuario.isPresent()) {
            usuarioRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}



