package br.fiap.smartsus.controllers;

import br.fiap.smartsus.model.Clinica;
import br.fiap.smartsus.repository.ClinicaRepository;

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
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/clinicas")    
public class ClinicaController {

    @Autowired
    private ClinicaRepository clinicaRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    public EntityModel<Clinica> postClinica(@RequestBody Clinica clinica) {
        Clinica savedClinica = clinicaRepository.save(clinica);
        return EntityModel.of(savedClinica, linkTo(methodOn(ClinicaController.class).getByIdClinica(savedClinica.getId())).withSelfRel());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("")
    public CollectionModel<EntityModel<Clinica>> getClinica(Pageable pageable) {
        Page<Clinica> page = clinicaRepository.findAll(pageable);
        List<EntityModel<Clinica>> clinics = page.getContent().stream()
            .map(clinica -> EntityModel.of(clinica,
                linkTo(methodOn(ClinicaController.class).getByIdClinica(clinica.getId())).withSelfRel(),
                linkTo(methodOn(ClinicaController.class).putClinica(clinica.getId(), null)).withRel("updateClinica"),
                linkTo(methodOn(ClinicaController.class).deleteClinica(clinica.getId())).withRel("deleteClinica")
            ))
            .collect(Collectors.toList());
        CollectionModel<EntityModel<Clinica>> resources = CollectionModel.of(clinics);
        resources.add(linkTo(methodOn(ClinicaController.class).getClinica(pageable)).withSelfRel());
        return resources;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public EntityModel<Clinica> getByIdClinica(@PathVariable Long id) {
        Optional<Clinica> clinica = clinicaRepository.findById(id);
        if (clinica.isPresent()) {
            return EntityModel.of(clinica.get(),
            linkTo(methodOn(ClinicaController.class).getByIdClinica(id)).withSelfRel(),
            linkTo(methodOn(ClinicaController.class).getClinica(Pageable.unpaged())).withRel("clinicas"));
        } else {
            return EntityModel.of(new Clinica());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public EntityModel<Clinica> putClinica(@PathVariable Long id, @RequestBody Clinica clinicaUpdated) {
        Optional<Clinica> optionalClinica = clinicaRepository.findById(id);
        if (optionalClinica.isPresent()) {
            Clinica clinica = optionalClinica.get();
            clinica.setNome(clinicaUpdated.getNome());
            clinica.setEspecialidade(clinicaUpdated.getEspecialidade());
            clinica.setPreco(clinicaUpdated.getPreco());
            Clinica clinicaSaved = clinicaRepository.save(clinica);
            return EntityModel.of(clinicaSaved, linkTo(methodOn(ClinicaController.class).getByIdClinica(clinicaSaved.getId())).withSelfRel());
        } else {
            return EntityModel.of(new Clinica());
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClinica(@PathVariable Long id) {
        Optional<Clinica> optionalClinica = clinicaRepository.findById(id);
        if (optionalClinica.isPresent()) {
            clinicaRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}