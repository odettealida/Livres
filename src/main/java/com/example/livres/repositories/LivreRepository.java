package com.example.livres.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.livres.model.Livre;

public interface LivreRepository extends JpaRepository<Livre,Long> {

}
