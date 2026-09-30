package com.example.livres.exception;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


// Le "traducteur d'alarmes" : quand une exception se déclenche
// n'importe où, il la transforme en réponse claire pour l'utilisateur.
@RestControllerAdvice
public class GestionErreur {

    // 404 = "introuvable"
    @ExceptionHandler(LivreIntrouvableException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String introuvable(LivreIntrouvableException e) {
        return e.getMessage();
    }

    // 409 = "conflit" : la demande est impossible dans l'état actuel
    @ExceptionHandler(LivreIndisponibleException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String indisponible(LivreIndisponibleException e) {
        return e.getMessage();
    }
    
    // 409 = "conflit" : on ne peut pas rendre un livre qui n'est pas emprunté
    @ExceptionHandler(LivreNonEmprunteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String nonEmprunte(LivreNonEmprunteException e) {
        return e.getMessage();
    }
    
    // 400 = "mauvaise demande" : les données envoyées ne respectent pas les règles.
    // Ce "traducteur" se déclenche quand @Valid refuse un livre dans le controller.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validation(MethodArgumentNotValidException e) {
        // Une Map = une liste de paires "champ -> message".
        Map<String, String> erreurs = new HashMap<>();

        // On parcourt chaque champ en erreur (titre, année...)
        // et on note son message.
        e.getBindingResult().getFieldErrors()
                .forEach(erreur -> erreurs.put(erreur.getField(), erreur.getDefaultMessage()));

        return erreurs;
    }
}
	

