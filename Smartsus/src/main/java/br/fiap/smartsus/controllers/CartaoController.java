package br.fiap.smartsus.controllers;

import br.fiap.smartsus.model.Cartao;
import br.fiap.smartsus.repository.CartaoRepository;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/cartoes")    
public class CartaoController {

    @Autowired
    private CartaoRepository cartaoRepository;

    @GetMapping("")
    public CollectionModel<EntityModel<Cartao>> getCartao() {
        List<EntityModel<Cartao>> cartoes = cartaoRepository.findAll().stream()
            .map(cartao -> EntityModel.of(cartao,
                Link.of(String.format("/api/cartoes/%d", cartao.getId())).withSelfRel(),
                Link.of(String.format("/api/cartoes/%d", cartao.getId())).withRel("update"),
                Link.of(String.format("/api/cartoes/%d", cartao.getId())).withRel("delete")))
            .collect(Collectors.toList());
        return CollectionModel.of(cartoes, Link.of("/api/cartoes").withSelfRel());
    }
    
    
    @PostMapping("")
    public ResponseEntity<EntityModel<Cartao>> postCartao(@RequestBody Cartao cartao) {
        Cartao novoCartao = cartaoRepository.save(cartao);
        EntityModel<Cartao> cartaoResource = EntityModel.of(novoCartao,
            Link.of(String.format("/api/cartoes/%d", novoCartao.getId())).withSelfRel().withType("GET"),
            Link.of(String.format("/api/cartoes/%d", novoCartao.getId())).withSelfRel().withType("PUT"),
            Link.of(String.format("/api/cartoes/%d", novoCartao.getId())).withSelfRel().withType("DELETE"));
        return ResponseEntity.created(cartaoResource.getRequiredLink("self").toUri()).body(cartaoResource);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Cartao>> getByIdCartao(@PathVariable Long id) {
        Optional<Cartao> cartao = cartaoRepository.findById(id);
        if (cartao.isPresent()) {
            EntityModel<Cartao> cartaoResource = EntityModel.of(cartao.get(),
                Link.of(String.format("/api/cartoes/%d", id)).withSelfRel().withType("GET"),
                Link.of(String.format("/api/cartoes/%d", id)).withSelfRel().withType("PUT"),
                Link.of(String.format("/api/cartoes/%d", id)).withSelfRel().withType("DELETE"));
            return ResponseEntity.ok(cartaoResource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCartao(@PathVariable Long id) {
        Optional<Cartao> optionalCartao = cartaoRepository.findById(id);
        if (optionalCartao.isPresent()) {
            cartaoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
