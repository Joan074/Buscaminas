/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminasjoan;

import javafx.util.*;
import javafx.beans.property.*;

/**
 *
 * @author joavilrod2
 */
public class Persona {

    private final StringProperty Nombre = new SimpleStringProperty();
    private final StringProperty Tiempo = new SimpleStringProperty();
    private final StringProperty Dificultad = new SimpleStringProperty();
    private final StringProperty foto = new SimpleStringProperty();

    public Persona(String nombre, String tiempo, String dificultad, String foto) {
        this.Nombre.setValue(nombre);
        this.Tiempo.setValue(tiempo);
        this.Dificultad.setValue(dificultad);
        this.foto.setValue(foto);
    }

    public final StringProperty NombreProperty() {
        return this.Nombre;
    }

    public final String getNombre() {
        return this.NombreProperty().get();
    }

    public final void setNombre(final String Nombre) {
        this.NombreProperty().set(Nombre);
    }

    public final StringProperty TiempoProperty() {
        return this.Tiempo;
    }

    public final String getTiempo() {
        return this.TiempoProperty().get();
    }

    public final void setTiempo(final String Tiempo) {
        this.TiempoProperty().set(Tiempo);
    }

    public final StringProperty DificultadProperty() {
        return this.Dificultad;
    }

    public final String getDificultad() {
        return this.DificultadProperty().get();
    }

    public final void setDificultad(final String Dificultad) {
        this.DificultadProperty().set(Dificultad);
    }

    public final StringProperty FotoProperty() {
        return this.foto;
    }

    public final String getFoto() {
        return this.FotoProperty().get();
    }

    public final void setFoto(final String Foto) {
        this.FotoProperty().set(Foto);
    }
}