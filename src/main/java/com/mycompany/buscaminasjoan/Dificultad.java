/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.buscaminasjoan;

/**
 *
 * @author joavilrod2
 */
public enum Dificultad {
    FACIL(8, 8, 10), INTERMEDIO(16, 16, 40), DIFICIL(16, 30, 99), PERSONALIZADO(0, 0, 0);

    public int filas;
    public int columnas;
    public int minas;

    private Dificultad(int filas, int columnas, int minas) {
        this.filas = filas;
        this.columnas = columnas;
        this.minas = minas;
    }

    public int getFilas() {
        return filas;
    }

    public void setFilas(int filas) {
        this.filas = filas;
    }

    public int getColumnas() {
        return columnas;
    }

    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }

    public int getMinas() {
        return minas;
    }

    public void setMinas(int minas) {
        this.minas = minas;
    }

    public static void setPersonalizado(int filas, int columnas, int minas) {
        PERSONALIZADO.setFilas(filas);
        PERSONALIZADO.setColumnas(columnas);
        PERSONALIZADO.setMinas(minas);
    }

}
