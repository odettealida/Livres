package com.example.livres.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.livres.exception.LivreIndisponibleException;
import com.example.livres.exception.LivreIntrouvableException;
import com.example.livres.exception.LivreNonEmprunteException;
import com.example.livres.model.Livre;
import com.example.livres.repositories.LivreRepository;

// @Service : dit à Spring "cette classe contient les règles de l'application".
// C'est la secrétaire de la bibliothèque : elle applique les règles.
// @Transactional : toute l'opération réussit, ou rien n'est enregistré.
// Pas de travail fait à moitié.
@Service
@Transactional
public class LivreService {

    // Le classeur d'archives : le service s'en sert pour ranger et retrouver les livres.
    // "final" : une fois donné, on ne peut plus le remplacer.
    private final LivreRepository livreRepository;

    // Spring fournit le classeur tout seul quand il crée le service.
    // On appelle ça l'injection de dépendances.
    public LivreService(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }

    // Ajouter : on range un nouveau livre dans le classeur.
    // save() crée la fiche et lui donne automatiquement un id.
    public Livre ajouter(Livre livre) {
        return livreRepository.save(livre);
    }

    // Modifier : on retrouve le livre, puis on change SEULEMENT
    // le titre, l'auteur et l'année.
    // On ne touche jamais à "emprunte", sinon on pourrait contourner
    // la règle de l'emprunt en modifiant un livre.
    public Livre modifier(Long id, Livre nouveau) {
        Livre livre = consulter(id); // si le livre n'existe pas, l'alarme se déclenche ici
        livre.setTitre(nouveau.getTitre());
        livre.setAuteur(nouveau.getAuteur());
        livre.setAnneeSortie(nouveau.getAnneeSortie());
        return livreRepository.save(livre);
    }

    // Supprimer : on vérifie d'abord que le livre existe, puis on le retire.
    public void supprimer(Long id) {
        Livre livre = consulter(id);
        livreRepository.delete(livre);
    }

    // Lister : on renvoie tous les livres du classeur.
    // readOnly = true : on ne fait que lire, on ne modifie rien.
    @Transactional(readOnly = true)
    public List<Livre> lister() {
        return livreRepository.findAll();
    }

    // Consulter : on cherche un livre par son id.
    // findById renvoie un "Optional" : une boîte qui contient
    // soit le livre, soit rien.
    // orElseThrow : si la boîte est vide, on déclenche l'alarme "livre inexistant".
    @Transactional(readOnly = true)
    public Livre consulter(Long id) {
        return livreRepository.findById(id)
                .orElseThrow(LivreIntrouvableException::new);
    }

    // Emprunter : c'est ici que vit ta règle métier. Trois étapes :
    public Livre emprunter(Long id) {
        // Étape 1 : chercher le livre (erreur "livre inexistant" s'il n'existe pas)
        Livre livre = consulter(id);

        // Étape 2 : s'il est déjà emprunté, on refuse
        if (livre.isEmprunte()) {
            throw new LivreIndisponibleException();
        }

        // Étape 3 : sinon, on le marque comme emprunté et on enregistre.
        // Sans cette ligne, un deuxième étudiant pourrait emprunter le même livre.
        livre.setEmprunte(true);
        return livreRepository.save(livre);
    }
    
    // Rendre : c'est l'inverse d'emprunter. Trois étapes :
    public Livre rendre(Long id) {
        // Étape 1 : chercher le livre (erreur "livre inexistant" s'il n'existe pas)
        Livre livre = consulter(id);

        // Étape 2 : si le livre n'est pas emprunté, on ne peut pas le rendre
        if (!livre.isEmprunte()) {
            throw new LivreNonEmprunteException();
        }

        // Étape 3 : sinon, on le remet disponible et on enregistre
        livre.setEmprunte(false);
        return livreRepository.save(livre);
    }
    
    
    
}