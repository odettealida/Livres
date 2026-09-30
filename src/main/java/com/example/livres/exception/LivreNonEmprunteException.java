package com.example.livres.exception;

// Alarme déclenchée quand on essaie de rendre un livre
// que personne n'a emprunté.
public class LivreNonEmprunteException extends RuntimeException {

    public LivreNonEmprunteException() {
        super("livre non emprunté");
    }
}