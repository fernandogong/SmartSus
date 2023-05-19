package br.fiap.smartsus.controllers;

import br.fiap.smartsus.model.Agendamento;
import br.fiap.smartsus.repository.AgendamentoRepository;

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

import java.util.List;
import java.util.Optional;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.CollectionModel;

import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;

@RestController
@RequestMapping("/agendamentos")    
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Secured("ROLE_ADMIN")
    @PostMapping("")
    public EntityModel<Agendamento> postAgendamento(@RequestBody Agendamento agendamento) {
        Agendamento savedAgendamento = agendamentoRepository.save(agendamento);
        return EntityModel.of(savedAgendamento, linkTo(methodOn(AgendamentoController.class).getByIdAgendamento(savedAgendamento.getId())).withSelfRel());
    }

    @GetMapping("")
    public CollectionModel<EntityModel<Agendamento>> getAgendamento(Pageable pageable) {
        Page<Agendamento> page = agendamentoRepository.findAll(pageable);
        List<EntityModel<Agendamento>> clinics = page.getContent().stream()
            .map(agendamento -> EntityModel.of(agendamento,
                linkTo(methodOn(AgendamentoController.class).getByIdAgendamento(agendamento.getId())).withSelfRel(),
                linkTo(methodOn(AgendamentoController.class).putAgendamento(agendamento.getId(), null)).withRel("updateAgendamento"),
                linkTo(methodOn(AgendamentoController.class).deleteAgendamento(agendamento.getId())).withRel("deleteAgendamento")
            ))
            .collect(Collectors.toList());
        CollectionModel<EntityModel<Agendamento>> resources = CollectionModel.of(clinics);
        resources.add(linkTo(methodOn(AgendamentoController.class).getAgendamento(pageable)).withSelfRel());
        return resources;
    }
    
    @GetMapping("/{id}")
    public EntityModel<Agendamento> getByIdAgendamento(@PathVariable Long id) {
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        if (agendamento.isPresent()) {
            return EntityModel.of(agendamento.get(),
            linkTo(methodOn(AgendamentoController.class).getByIdAgendamento(id)).withSelfRel(),
            linkTo(methodOn(AgendamentoController.class).getAgendamento(Pageable.unpaged())).withRel("agendamentos"));
        } else {
            return EntityModel.of(new Agendamento());
        }
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public EntityModel<Agendamento> putAgendamento(@PathVariable Long id, @RequestBody Agendamento agendamentoUpdated) {
        Optional<Agendamento> optionalAgendamento = agendamentoRepository.findById(id);
        if (optionalAgendamento.isPresent()) {
            Agendamento agendamento = optionalAgendamento.get();
            agendamento.setAgendamentoData(agendamentoUpdated.getAgendamentoData());
            Agendamento agendamentoSaved = agendamentoRepository.save(agendamento);
            return EntityModel.of(agendamentoSaved, linkTo(methodOn(AgendamentoController.class).getByIdAgendamento(agendamentoSaved.getId())).withSelfRel());
        } else {
            return EntityModel.of(new Agendamento());
        }
    }

    @Secured("ROLE_ADMIN")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAgendamento(@PathVariable Long id) {
        Optional<Agendamento> optionalAgendamento = agendamentoRepository.findById(id);
        if (optionalAgendamento.isPresent()) {
            agendamentoRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}