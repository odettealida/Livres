package com.example.livres.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.livres.model.Livre;
import com.example.livres.service.LivreService;

import jakarta.validation.Valid;

// @RestController : "cette classe reçoit des demandes web et répond en JSON".
// @RequestMapping("/livres") : toutes les adresses commencent par /livres.
@RestController
@RequestMapping("/livres")
public class LivreController {

    // Le controller ne fait pas de règles : il passe la demande à la secrétaire (le service).
    private final LivreService livreService;

    public LivreController(LivreService livreService) {
        this.livreService = livreService;
    }

    // GET /livres : voir tous les livres
    @GetMapping
    public List<Livre> lister() {
        return livreService.lister();
    }

    // GET /livres/5 : voir le livre numéro 5.
    // @PathVariable : prend le "5" dans l'adresse et le donne à la méthode.
    @GetMapping("/{id}")
    public Livre consulter(@PathVariable Long id) {
        return livreService.consulter(id);
    }

    // POST /livres : ajouter un livre.
    // @RequestBody : les données du livre arrivent dans la demande.
    // @Valid : Spring vérifie les règles (@NotBlank, @NotNull...) avant d'entrer.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Livre ajouter(@Valid @RequestBody Livre livre) {
        return livreService.ajouter(livre);
    }

    // PUT /livres/5 : modifier le livre numéro 5
    @PutMapping("/{id}")
    public Livre modifier(@PathVariable Long id, @Valid @RequestBody Livre livre) {
        return livreService.modifier(id, livre);
    }

    // DELETE /livres/5 : supprimer le livre numéro 5
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void supprimer(@PathVariable Long id) {
        livreService.supprimer(id);
    }

    // POST /livres/5/emprunter : emprunter le livre numéro 5
    @PostMapping("/{id}/emprunter")
    public Livre emprunter(@PathVariable Long id) {
        return livreService.emprunter(id);
    }
    
    // POST /livres/5/rendre : rendre le livre numéro 5
    @PostMapping("/{id}/rendre")
    public Livre rendre(@PathVariable Long id) {
        return livreService.rendre(id);
    }
    
    
    
}