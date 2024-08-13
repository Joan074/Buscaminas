/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.buscaminasjoan;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author villa
 */
public class AñadirController implements Initializable {

    @FXML
    private TextField textFieldNombre;
    @FXML
    private TextField textFieldTiempo;
    @FXML
    private Button botonCancelar;
    @FXML
    private Button botonAñadir;
    @FXML
    private TextField textFieldDificultad;
    @FXML
    private TextField textFielImagen;

    private boolean cancelar = false;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        botonCancelar.setOnAction(e -> {
            cancelar = true;
            Stage stage = (Stage) botonCancelar.getScene().getWindow();
            stage.close();
        });

        botonAñadir.setOnAction(e -> {
            Stage stage = (Stage) botonCancelar.getScene().getWindow();
            stage.close();
        });
        
    }

    public boolean isCancelar() {
        return cancelar;
    }

    public void initPersona(Persona p) {
       textFieldNombre.setText(p.getNombre());
        textFieldTiempo.setText(p.getTiempo());
        textFieldDificultad.setText(p.getDificultad());
        textFielImagen.setText(p.getFoto());
    }

    @FXML
    private void cancelar(ActionEvent event) {
        // Acción de cancelar si se necesita
    }

    @FXML
    private void añadir(ActionEvent event) {
        // Acción de añadir si se necesita
    }

    public Persona getPersona() {
        String nombre = textFieldNombre.getText();
        String tiempo = textFieldTiempo.getText();
        String dificultad = textFieldDificultad.getText();
        String imagen = "./src/main/java/com/mycompany/buscaminasjoan/imagenes/" + textFielImagen.getText();

        return new Persona(nombre, tiempo, dificultad, imagen);
    }
}
