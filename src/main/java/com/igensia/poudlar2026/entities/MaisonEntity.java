package com.igensia.poudlar2026.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "Maison")
public class MaisonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "nom", length = 50, nullable = false)
    private String nom;

    @Column(name = "points", nullable = false)
    private Integer points;

    public MaisonEntity() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
