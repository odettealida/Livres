package com.example.livres.exception;

// Même principe : ce signal d'alarme se déclenche quand
// on essaie d'emprunter un livre déjà emprunté.
public class LivreIndisponibleException extends RuntimeException {

    public LivreIndisponibleException() {
        super("livre indisponible");
    }
}