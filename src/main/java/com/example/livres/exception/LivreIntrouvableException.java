package com.example.livres.exception;

// Une exception est un "signal d'alarme" : elle arrête l'opération
// et explique ce qui ne va pas.
// RuntimeException : on n'est pas obligé de la déclarer partout dans le code.
public class LivreIntrouvableException extends RuntimeException {

    public LivreIntrouvableException() {
        // Le message que verra celui qui appelle l'application
        super("livre inexistant");
    }
}