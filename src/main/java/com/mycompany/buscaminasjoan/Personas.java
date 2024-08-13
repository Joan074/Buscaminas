/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminasjoan;

import java.util.ArrayList;

/**
 *
 * @author villa
 */
public class Personas {
     public ArrayList<Persona> personas;

    public Personas(ArrayList<Persona> personas) {
        this.personas = personas;
    }

    public void addJugador(Persona persona) {
        this.personas.add(persona);
    }

    public ArrayList<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(ArrayList<Persona> personas) {
        this.personas = personas;
    }

}
