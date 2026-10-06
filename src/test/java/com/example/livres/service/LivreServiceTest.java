package com.example.livres.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.livres.exception.LivreIndisponibleException;
import com.example.livres.exception.LivreIntrouvableException;
import com.example.livres.exception.LivreNonEmprunteException;
import com.example.livres.model.Livre;
import com.example.livres.repositories.LivreRepository;


// On teste le service SEUL, sans base de données ni Spring : c'est rapide.
@ExtendWith(MockitoExtension.class)
class LivreServiceTest {

    // Un "faux classeur" : il ne range rien pour de vrai, on lui dicte ses réponses.
    @Mock
    private LivreRepository livreRepository;

    // Le vrai service, auquel on donne le faux classeur.
    @InjectMocks
    private LivreService livreService;

    // Un livre disponible, utilisé dans les scénarios
    private Livre livreDisponible() {
        Livre livre = new Livre();
        livre.setTitre("Une si longue lettre");
        livre.setAuteur("Mariama Bâ");
        livre.setAnneeSortie(1979);
        return livre;
    }

    @Test
    void emprunter_livreDisponible_leMarqueEmprunte() {
        Livre livre = livreDisponible();
        // On dicte au faux classeur : "le livre 1 existe, le voici"
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        // Et : "quand on enregistre, renvoie ce qu'on t'a donné"
        when(livreRepository.save(any(Livre.class))).thenAnswer(i -> i.getArgument(0));

        Livre resultat = livreService.emprunter(1L);

        assertTrue(resultat.isEmprunte());
    }

    @Test
    void emprunter_livreDejaEmprunte_declencheAlarmeEtNEnregistreRien() {
        Livre livre = livreDisponible();
        livre.setEmprunte(true);
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));

        assertThrows(LivreIndisponibleException.class, () -> livreService.emprunter(1L));

        // On vérifie aussi que rien n'a été enregistré
        verify(livreRepository, never()).save(any());
    }

    @Test
    void emprunter_livreInexistant_declencheAlarme() {
        // "le livre 99 n'existe pas" : le classeur renvoie une boîte vide
        when(livreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(LivreIntrouvableException.class, () -> livreService.emprunter(99L));
    }

    @Test
    void rendre_livreEmprunte_leRemetDisponible() {
        Livre livre = livreDisponible();
        livre.setEmprunte(true);
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));
        when(livreRepository.save(any(Livre.class))).thenAnswer(i -> i.getArgument(0));

        Livre resultat = livreService.rendre(1L);

        assertFalse(resultat.isEmprunte());
    }

    @Test
    void rendre_livreNonEmprunte_declencheAlarme() {
        Livre livre = livreDisponible(); // emprunte vaut false par défaut
        when(livreRepository.findById(1L)).thenReturn(Optional.of(livre));

        assertThrows(LivreNonEmprunteException.class, () -> livreService.rendre(1L));
    }
}