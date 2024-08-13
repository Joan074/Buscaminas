/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mycompany.buscaminasjoan;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Alert;

/**
 * FXML Controller class
 *
 * @author villa
 */
public class TableViewController implements Initializable {

    @FXML
    private Button botonAdd;
    @FXML
    private Button botonBorrar;
    @FXML
    private Button botonModificar;
    @FXML
    private Button botonVer;
    @FXML
    private TableView<Persona> vistaTabla;
    @FXML
    private TableColumn<Persona, String> nombreColumna;
    @FXML
    private TableColumn<Persona, String> tiempoColumna;
    @FXML
    private TableColumn<Persona, String> dificultadColumna;
    @FXML
    private TableColumn<Persona, String> imagenColumna;

    private ObservableList<Persona> misPersonas;
    private File JugadoresFile;
    Personas personas;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ArrayList<Persona> misDatos = new ArrayList<>();

        JugadoresFile = new File("./src/main/java/com/mycompany/buscaminasjoan/ficheros/jugadores.txt");

        if (JugadoresFile.exists()) {
            // Si existe, leer los datos del archivo y almacenarlos en datosArrayList
            System.out.println("Archivo encontrado, leyendo datos...");

            misDatos = Auxiliar.leerJugadores(JugadoresFile.getPath());
        } else {
            // Si no existe, usar datos por defecto y añadirlos a datosArrayList
            System.out.println("Archivo no encontrado, usando datos por defecto.");
            misDatos.add(new Persona("Elena Oceane", "11:30", "dificil", "./src/main/java/com/mycompany/buscaminasjoan/imagenes/elena.jpg"));
        }

        personas = new Personas(misDatos);

        misPersonas = FXCollections.observableArrayList(misDatos);
        vistaTabla.setItems(misPersonas);

        //Clase de Conveniencia
        nombreColumna.setCellValueFactory(cellData -> cellData.getValue().NombreProperty());
        tiempoColumna.setCellValueFactory(cellData -> cellData.getValue().TiempoProperty());

        //Siempre property
        //Definimos que queremos ver
        dificultadColumna.setCellValueFactory(cellData -> cellData.getValue().DificultadProperty());

        imagenColumna.setCellValueFactory(cellData -> cellData.getValue().FotoProperty());
        imagenColumna.setCellFactory(columna -> {
            return new TableCell<Persona, String>() {
                private ImageView view = new ImageView();

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setGraphic(null);
                    } else {
                        //Carga el archivo que contiene la imagen
                        File imageFile = new File(item);
                        String fileLocation = imageFile.toURI().toString();
                        Image image = new Image(fileLocation, 40, 40, true, true);

                        //envia la imagen
                        view.setImage(image);
                        setGraphic(view);
                    }
                }

            };

        });

    }

    public ObservableList<Persona> getMisPersonas() {
        return misPersonas;
    }
   

    @FXML
    private void verdatos(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(
                getClass().getClassLoader().getResource(
                        "com/mycompany/buscaminasjoan/ranking.fxml")
        );

        Parent root = miCargador.load();

        RankingController controladorRanking = miCargador.<RankingController>getController();
        controladorRanking.setPersonas(new ArrayList<>(misPersonas));

        Scene scene = new Scene(root, 450, 650);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Ver datos persona");
        stage.initModality(Modality.APPLICATION_MODAL); // La ventana se muestra en modal
        stage.showAndWait(); // Espera a que se cierre la segunda ventana
    }

    @FXML
    private void añadir(ActionEvent event) throws IOException {
        Button boton = (Button) event.getSource();

        FXMLLoader miCargador = new FXMLLoader(
                getClass().getClassLoader().getResource(
                        "com/mycompany/buscaminasjoan/añadir.fxml")
        );

        Parent root = miCargador.load();

        AñadirController controladorPersona = miCargador.<AñadirController>getController();

        // Nueva persona
        Persona persona = new Persona("", "", "", "");

        // Llamar al método initPersona
        controladorPersona.initPersona(persona);
        Scene scene = new Scene(root, 500, 300);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Añadir persona");
        stage.initModality(Modality.APPLICATION_MODAL); // La ventana se muestra en modal
        stage.showAndWait(); // Espera a que se cierre la segunda ventana

        if (!controladorPersona.isCancelar()) {
            Persona nuevaPersona = controladorPersona.getPersona();
            if ((!nuevaPersona.getNombre().isEmpty())
                    && (nuevaPersona.getNombre().trim().length() != 0)
                    && (!nuevaPersona.getTiempo().isEmpty())
                    && (nuevaPersona.getTiempo().trim().length() != 0)
                    && (!nuevaPersona.getDificultad().isEmpty())
                    && (nuevaPersona.getDificultad().trim().length() != 0)
                    && (!nuevaPersona.getFoto().isEmpty())
                    && (nuevaPersona.getFoto().trim().length() != 0)) {

                String tiempo = nuevaPersona.getTiempo();

// Expresión regular para verificar el formato del tiempo
                String formatoTiempo = "^\\d{1,2}:\\d{2}$";

                for (Persona personaf : vistaTabla.getItems()) {
                    if (personaf.getNombre().equals(nuevaPersona.getNombre())) {
                        nombreRepetido();
                        return;
                    }
                }

                if (!tiempo.matches(formatoTiempo)) {
                    mostrarAlertaT();
                    return;
                }

                if (!nuevaPersona.getDificultad().equals("facil") && !nuevaPersona.getDificultad().equals("intermedio") && !nuevaPersona.getDificultad().equals("dificil")) {
                    if (nuevaPersona.getDificultad().equals("personalizado")) {
                        mostrarAlertaDP();
                        return;
                    }  
                    mostrarAlertaD();
                    return;
                }

                misPersonas.add(nuevaPersona);
                Auxiliar.grabarJugadores(new ArrayList<>(misPersonas));
                vistaTabla.refresh();

            }
        }
    }

    @FXML
    private void modificar(ActionEvent event) throws IOException {
        Button boton = (Button) event.getSource();
        System.out.println(boton.getId());

        FXMLLoader miCargador = new FXMLLoader(
                getClass().getClassLoader().getResource(
                        "com/mycompany/buscaminasjoan/añadir.fxml")
        );

        Parent root = miCargador.load();

        AñadirController controladorPersona
                = miCargador.<AñadirController>getController();

        Persona persona = new Persona("", "", "", "");

        Scene scene = new Scene(root, 500, 300);
        Stage stage = new Stage();

        if (boton.getId().equals("botonAdd")) {
            controladorPersona.initPersona(persona);
            stage.setTitle("Añadir ==>> Persona");
        } else {
            persona = vistaTabla.getSelectionModel().getSelectedItem();
            if (persona == null) {
                return;
            }
            stage.setTitle("Modificar ==>> Persona");
        }

        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();

        if (!controladorPersona.isCancelar()) {
            Persona personaModificada = controladorPersona.getPersona();

            if (personaModificada != null
                    && personaModificada.getNombre() != null && !personaModificada.getNombre().trim().isEmpty()
                    && personaModificada.getTiempo() != null && !personaModificada.getTiempo().trim().isEmpty()
                    && personaModificada.getDificultad() != null && !personaModificada.getDificultad().trim().isEmpty()
                    && personaModificada.getFoto() != null && !personaModificada.getFoto().trim().isEmpty()) {

                String tiempo = personaModificada.getTiempo();

// Expresión regular para verificar el formato del tiempo
                String formatoTiempo = "^\\d{1,2}:\\d{2}$";

                for (Persona personaf : vistaTabla.getItems()) {
                    if (personaf.getNombre().equals(personaModificada.getNombre())) {
                        nombreRepetido();
                        return;
                    }
                }

                if (!tiempo.matches(formatoTiempo)) {
                    mostrarAlertaT();
                    return;
                }

                if (!personaModificada.getDificultad().equals("facil") && !personaModificada.getDificultad().equals("intermedio") && !personaModificada.getDificultad().equals("dificil")) {
                    if (personaModificada.getDificultad().equals("personalizado")) {
                        mostrarAlertaDP();
                        return;
                    }  
                    mostrarAlertaD();
                    return;
                }

                if (boton.getId().equals("botonAñadir")) {
                    misPersonas.add(personaModificada);
                } else {
                    int indice = misPersonas.indexOf(persona);
                    misPersonas.set(indice, personaModificada);
                }
                Auxiliar.grabarJugadores(new ArrayList<>(misPersonas));
                vistaTabla.refresh();
            }
        }
    }

    @FXML
    private void borrar(ActionEvent event) {
        Persona persona = vistaTabla.getSelectionModel().getSelectedItem();

        misPersonas.remove(persona);
        Auxiliar.grabarJugadores(new ArrayList<>(misPersonas));
        vistaTabla.refresh();
    }

    //Metodo para guardar personas desde otras clases
    public void agregarPersona(Persona nuevaPersona) {
        if (nuevaPersona != null
                && nuevaPersona.getNombre() != null && !nuevaPersona.getNombre().trim().isEmpty()
                && nuevaPersona.getTiempo() != null && !nuevaPersona.getTiempo().trim().isEmpty()
                && nuevaPersona.getDificultad() != null && !nuevaPersona.getDificultad().trim().isEmpty()
                && nuevaPersona.getFoto() != null && !nuevaPersona.getFoto().trim().isEmpty()) {

            String tiempo = nuevaPersona.getTiempo();
            String formatoTiempo = "^\\d{1,2}:\\d{2}$";

            for (Persona personaf : vistaTabla.getItems()) {
                if (personaf.getNombre().equals(nuevaPersona.getNombre())) {
                    nombreRepetido();
                    return;
                }
            }

            if (!tiempo.matches(formatoTiempo)) {
                mostrarAlertaT();
                return;
            }

            if (!nuevaPersona.getDificultad().equals("facil") && !nuevaPersona.getDificultad().equals("intermedio") && !nuevaPersona.getDificultad().equals("dificil")) {
                if (nuevaPersona.getDificultad().equals("personalizado")) {
                        mostrarAlertaDP();
                        return;
                    }  
                    mostrarAlertaD();
                    return;
            }

            misPersonas.add(nuevaPersona);
            vistaTabla.refresh();
            Auxiliar.grabarJugadores(new ArrayList<>(misPersonas));
        }
    }

    // Suponiendo que tienes una referencia al TableView

// Método para agregar persona al TableView
    public void agregarPersonaATableView(Persona persona) {
        // Agregar la persona al TableView
        vistaTabla.getItems().add(persona);
    }

    public void mostrarAlertaD() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error en dificultad");
        alert.setHeaderText("Dificultad ERRONEA");
        alert.setContentText("Debe ser una dificultad ya existente: facil, intermedio o dificil");
        alert.showAndWait();
    }

    public void mostrarAlertaT() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Error en tiempo");
        alert.setHeaderText("Formato de tiempo ERRONEO");
        alert.setContentText("Debe ser un formato como estos: 00:00 / 0:00");
        alert.showAndWait();
    }

    public void nombreRepetido() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Nombre repetido");
        alert.setHeaderText("DUPLICADO");
        alert.setContentText("No es posible este usuario, ya que existe otro igual");
        alert.showAndWait();
    }
    
    public void mostrarAlertaDP() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Dificultad no rankeable");
        alert.setHeaderText("Dificultad PERSONALIZADA");
        alert.setContentText("Felicidades por ganar, pero no guardaremos los datos de las partidas personalizadas");
        alert.showAndWait();
    }

}
