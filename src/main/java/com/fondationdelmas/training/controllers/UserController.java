package com.fondationdelmas.training.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fondationdelmas.training.dtos.CreateUserRequestDTO;
import com.fondationdelmas.training.dtos.UserResponseDTO;
import com.fondationdelmas.training.services.UserService;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import static net.logstash.logback.argument.StructuredArguments.kv;
import io.micrometer.observation.annotation.Observed;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/users")
@Slf4j
@Tag(name = "User API", description = "Gestion des utilisateurs")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    /**
     * 
     * @param request
     * @return
     */
    @PostMapping
    @Observed(name = "users.create", contextualName = "create-user")
    @Operation(summary = "Créer un utilisateur")
    public ResponseEntity<UserResponseDTO> create(
            @Valid @RequestBody CreateUserRequestDTO request
    ) {
        log.info("HTTP POST /users");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(service.create(request));
    }


    /**
     * 
     * @return
     */
    @GetMapping
    @Operation(summary = "Récupérer l'ensemble des utilisateurs")
    public List<UserResponseDTO> findAll() {
        return service.findAll();
    }


    /**
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un utilisateur par ID")
    public UserResponseDTO findById(@PathVariable Long id) {
        log.info("HTTP GET /users/{id}",
                kv("userId", id)
        );
        return service.findById(id);
    }


    /**
     * 
     * @param id
     * @param request
     * @return
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un utilisateur")
    public UserResponseDTO update(
            @PathVariable Long id,
            @Valid @RequestBody CreateUserRequestDTO request
    ) {
        return service.update(id, request);
    }


    /**
     * 
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Supprimer un utilisateur")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}