package com.mycompany.buscaminasjoan;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.MouseButton;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.control.Alert.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.scene.control.TableView;
import java.awt.Color;
import javafx.scene.control.MenuItem;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class BuscaminasController implements Initializable {

    @FXML
    private Label labelMine;
    @FXML
    private Label labelTime;
    @FXML
    private ComboBox<Dificultad> cboxDif;

    private GridPane gridpane = new GridPane();
    @FXML
    private BorderPane borderPane;

    // Variables para el juego
    int contBandera = 0;
    int[][] tabla;
    boolean[][] tablaBoolean;
    boolean[][] tablaVisitadas;
    private Timeline timeline;
    private int segundos = 0, minutos = 0;
    Button boton;
    int minasAcertadas = 0;
    private boolean juegoTerminado = false;

    // Ruta para la imagen de fondo
    File imageFile = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/fondo.png");
    String fileLocation = imageFile.toURI().toString();
    Image image = new Image(fileLocation, 950, 760, true, true);
    ImageView imageView = new ImageView(image);

    @FXML
    private Button exit;
    @FXML
    private ImageView imagenExit;
    @FXML
    private ImageView imagenInfo;
    @FXML
    private Button info;
    @FXML
    private Button botonRanking;
    @FXML
    private ImageView imagenRanking;

    // Controlador del TableView (Para el ranking)
    private TableViewController tableView;
    @FXML
    private MenuItem botonReiniciar;
    @FXML
    private MenuItem botonPersonalizado;
    @FXML
    private MenuItem botonComoJugar;

    // Método que se ejecuta al inicializar el controlador
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializar las opciones de dificultad
        cboxDif.getItems().addAll(
                Dificultad.FACIL,
                Dificultad.INTERMEDIO,
                Dificultad.DIFICIL,
                Dificultad.PERSONALIZADO
        );
        cboxDif.setOnAction(this::saberPersonalizadaDificultad);

        // Agregar la imagen al centro del BorderPane
        borderPane.setCenter(imageView);

        // Configurar el menú y los botones de la interfaz
        menu();

        exit.setOnAction(e -> {
            Stage stage = (Stage) exit.getScene().getWindow();
            stage.close();
        });

        info.setOnAction(e -> {
            acercaDe();
        });

        botonPersonalizado.setOnAction(e -> {
            configurarDificultadPersonalizada();
        });

        botonComoJugar.setOnAction(e -> {
            intrucciones();
        });
    }

    // Maneja la selección de dificultad personalizada
    private void saberPersonalizadaDificultad(ActionEvent event) {
        Dificultad dificultadSeleccionada = cboxDif.getValue();
        if (dificultadSeleccionada == Dificultad.PERSONALIZADO) {
            configurarDificultadPersonalizada();
        } else {
            crearTabla(dificultadSeleccionada);
        }
    }

    // Configuración de dificultad personalizada
    private void configurarDificultadPersonalizada() {
        TextInputDialog dialogoFilas = new TextInputDialog();
        dialogoFilas.setTitle("Configuración Personalizada");
        dialogoFilas.setHeaderText("Ingresa el número de filas (No debe ser mayor de 20)");
        Optional<String> resultadoFilas = dialogoFilas.showAndWait();

        if (!resultadoFilas.isPresent()) {
            return; // El usuario canceló el diálogo
        }

        int filas, columnas, minas;

        filas = Integer.parseInt(resultadoFilas.get());
        if (filas > 20) {
            mostrarError("Número de filas inválido");
            return;
        }

        TextInputDialog dialogoColumnas = new TextInputDialog();
        dialogoColumnas.setTitle("Configuración Personalizada");
        dialogoColumnas.setHeaderText("Ingresa el número de columnas (No debe ser mayor de 32)");
        Optional<String> resultadoColumnas = dialogoColumnas.showAndWait();

        if (!resultadoColumnas.isPresent()) {
            return; // El usuario canceló el diálogo
        }

        columnas = Integer.parseInt(resultadoColumnas.get());
        if (columnas > 32) {
            mostrarError("Número de columnas inválido");
            return;
        }

        TextInputDialog dialogoMinas = new TextInputDialog();
        dialogoMinas.setTitle("Configuración Personalizada");
        dialogoMinas.setHeaderText("Ingresa el número de minas (No debe ser mayor de: " + (filas * columnas - 1) + ")");
        Optional<String> resultadoMinas = dialogoMinas.showAndWait();

        if (!resultadoMinas.isPresent()) {
            return; // El usuario canceló el diálogo
        }

        minas = Integer.parseInt(resultadoMinas.get());

        if (minas >= filas * columnas) {
            mostrarError("Número de minas inválido");
            return;
        }

        Dificultad.PERSONALIZADO.setFilas(filas);
        Dificultad.PERSONALIZADO.setColumnas(columnas);
        Dificultad.PERSONALIZADO.setMinas(minas);

        crearTabla(Dificultad.PERSONALIZADO);
    }

     // Mostrar mensaje de error para la dificultad personalizada
    private void mostrarError(String error) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        String frase = error;

        alert.setTitle("Error");
        alert.setHeaderText(frase);
        alert.showAndWait();
    }

    // Método para crear la tabla de juego
    public void crearTabla(Dificultad dificultad) {
        botonReiniciar.setOnAction(exit -> {
            crearTabla((dificultad));
        });
        resetearContador();
        contador();
        imageView.setOpacity(0.5);

        System.out.println(dificultad.name() + " " + dificultad.toString());
        juegoTerminado = false;
        minasAcertadas = 0;

        // Limpiar la tabla anterior y reiniciar el contador de banderas
        gridpane.getChildren().clear();
        contBandera = 0;
        labelMine.setText("Mines: " + contBandera + "/" + dificultad.getMinas());

        int filas = dificultad.getFilas();
        int columnas = dificultad.getColumnas();

        tablaBoolean = new boolean[filas][columnas];
        tabla = new int[filas][columnas];
        tablaVisitadas = new boolean[filas][columnas];

        gridpane.setAlignment(Pos.CENTER);

        // Crear un StackPane para contener tanto el GridPane como la imagen de fondo
        StackPane stackPane = new StackPane();

        // Establecer la imagen de fondo en el StackPane
        ImageView imagenView = new ImageView(image);

        //imagenView.setOpacity(0.5);
        stackPane.getChildren().add(imagenView);

        // Agregar el GridPane al StackPane para que esté en la parte superior
        stackPane.getChildren().add(gridpane);

        // Establecer el StackPane en el centro del BorderPane
        borderPane.setCenter(stackPane);

        // Crear los botones y añadir eventos de clic
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                Button boton = new Button();
                boton.setPrefSize(30, 30);

                 // Colores alternados para los botones
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        boton.setStyle("-fx-background-color: #E2B9FF; -fx-text-fill: black;");
                    } else {
                        boton.setStyle("-fx-background-color: #CE89FF; -fx-text-fill: black;");
                    }
                } else {
                    if (j % 2 == 0) {
                        boton.setStyle("-fx-background-color: #CE89FF; -fx-text-fill: black;");
                    } else {
                        boton.setStyle("-fx-background-color: #E2B9FF; -fx-text-fill: black;");
                    }

                }
                gridpane.add(boton, j, i);

                boton.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if (juegoTerminado) {
                            return; // No hacer nada si el juego ha terminado
                        }
                        Button clickedButton = (Button) mouseEvent.getSource();
                        int fila = GridPane.getRowIndex(clickedButton);
                        int columna = GridPane.getColumnIndex(clickedButton);

                        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                            if (clickedButton.getGraphic() == null) {
                                clickedButton.setDisable(true);
                                clickedButton.setOpacity(0.8);

                                // Si se hace clic en una mina, mostrar la mina y finalizar el juego
                                if (tabla[fila][columna] == 9) {
                                    mostrarMinas();
                                    System.out.println("Mine clicked at (" + fila + ", " + columna + ")");
                                    bloquearTablero();
                                    gameOver(dificultad);
                                } else {
                                    // Mostrar el número de minas alrededor
                                    int minasAlrededor = contarMinasAlrededor(tabla, fila, columna);
                                    if (minasAlrededor == 0) {
                                        clickedButton.setText(String.valueOf(""));
                                        // Descubrir las casillas vacías adyacentes
                                        descubrirCasillasVacias(tabla, tablaVisitadas, fila, columna);
                                    } else {
                                        clickedButton.setText(String.valueOf(minasAlrededor));
                                    }
                                }
                            }
                        } else if (mouseEvent.getButton().equals(MouseButton.SECONDARY)) {
                            // Manejar el clic derecho para marcar/desmarcar con bandera
                            if (clickedButton.getGraphic() != null) {
                                clickedButton.setGraphic(null);
                                contBandera--;

                                if (tabla[fila][columna] == 9) {
                                    minasAcertadas--;
                                }
                            } else {
                                File imageFile = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/bandera.jpg");
                                String fileLocation = imageFile.toURI().toString();
                                Image image = new Image(fileLocation, 12, 12, true, true);
                                ImageView imageView = new ImageView(image);

                                clickedButton.setGraphic(imageView);
                                contBandera++;

                                if (tabla[fila][columna] == 9) {
                                    minasAcertadas++;
                                }
                            }
                            labelMine.setText("Mines: " + contBandera + "/" + dificultad.getMinas());
                        }

                        if (minasAcertadas == dificultad.getMinas()) {
                            bloquearTablero();
                            victoria(dificultad);
                        }
                    }
                });

            }
        }
        // Añadir las minas después de crear la tabla
        añadirMinas(dificultad);
    }

    // Método para añadir minas aleatoriamente
    public void añadirMinas(Dificultad dificultad) {
        int minas = dificultad.getMinas();
        int filas = dificultad.getFilas();
        int columnas = dificultad.getColumnas();

        while (minas > 0) {
            int fila = (int) (Math.random() * filas);
            int columna = (int) (Math.random() * columnas);
            if (!tablaBoolean[fila][columna]) {
                tablaBoolean[fila][columna] = true;
                tabla[fila][columna] = 9;
                minas--;
            }
        }
    }

    // Contar el número de minas alrededor de una celda específica
    public int contarMinasAlrededor(int[][] tabla, int fila, int columna) {
        // Si la celda actual contiene una mina, devolver -1
        if (tabla[fila][columna] == 9) {
            return -1;
        }

        // Definir las coordenadas de las celdas adyacentes
        int[][] adyacentes = {
            {-1, -1}, {-1, 0}, {-1, 1}, // Arriba
            {0, -1}, {0, 1}, // Izquierda y derecha
            {1, -1}, {1, 0}, {1, 1} // Abajo
        };

        // Inicializar el contador de minas adyacentes
        int contador = 0;

        // Iterar sobre las celdas adyacentes
        for (int[] minas : adyacentes) {
            // Calcular las coordenadas de la celda adyacente
            int filaAdyacente = fila + minas[0];
            int columnaAdyacente = columna + minas[1];

            // Verificar si la celda adyacente está dentro de los límites del tablero
            if (filaAdyacente >= 0 && filaAdyacente < tabla.length
                    && columnaAdyacente >= 0 && columnaAdyacente < tabla[0].length) {
                // Si la celda adyacente contiene una mina, incrementar el contador
                if (tabla[filaAdyacente][columnaAdyacente] == 9) {
                    contador++;
                }
            }
        }

        // Devolver el número de minas adyacentes a la celda actual
        return contador;
    }

    // Función para descubrir casillas vacías y sus adyacentes
    public void descubrirCasillasVacias(int[][] tabla, boolean[][] visitadas, int fila, int columna) {
        // Verificar si la celda actual ya ha sido visitada
        if (visitadas[fila][columna]) {
            return;
        }

        // Marcar la celda actual como visitada
        visitadas[fila][columna] = true;

        // Obtener el botón correspondiente a la celda actual
        Button boton = (Button) getNodeByRowColumnIndex(fila, columna, gridpane);
        if (boton != null) {
            boton.setDisable(true);
            boton.setOpacity(0.8);
        }

        // Si la celda actual contiene una mina, no continuar
        if (tabla[fila][columna] == 9) {
            return;
        }

        // Contar el número de minas alrededor de la celda actual
        int minasAlrededor = contarMinasAlrededor(tabla, fila, columna);

        // Si no hay minas alrededor, descubrir las celdas adyacentes
        if (minasAlrededor == 0) {
            if (boton != null) {
                boton.setText("");
            }

            // Definir las coordenadas de las celdas adyacentes
            int[][] adyacentes = {
                {-1, -1}, {-1, 0}, {-1, 1}, // Arriba
                {0, -1}, {0, 1}, // Izquierda y derecha
                {1, -1}, {1, 0}, {1, 1} // Abajo
            };

            // Explorar las celdas adyacentes
            for (int[] minas : adyacentes) {
                int filaAdyacente = fila + minas[0];
                int columnaAdyacente = columna + minas[1];

                if (filaAdyacente >= 0 && filaAdyacente < tabla.length
                        && columnaAdyacente >= 0 && columnaAdyacente < tabla[0].length) {
                    descubrirCasillasVacias(tabla, visitadas, filaAdyacente, columnaAdyacente);
                }
            }
        } else {
            // Si hay minas alrededor, mostrar el número de minas
            if (boton != null) {
                boton.setText(String.valueOf(minasAlrededor));
            }
        }
    }

    // Método para iniciar el contador de tiempo
    public void contador() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (segundos == 59) {
                    minutos++;
                    segundos = 0;
                    Platform.runLater(() -> labelTime.setText("Time: " + minutos + ":" + segundos));
                } else {
                    if (segundos < 9) {
                        Platform.runLater(() -> labelTime.setText("Time: " + minutos + ":0" + segundos));
                    } else {
                        Platform.runLater(() -> labelTime.setText("Time: " + minutos + ":" + segundos));
                    }
                    segundos++;
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    // Método para detener el contador de tiempo
    public void pararContador() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    // Método para manejar el fin del juego
    public void gameOver(Dificultad dificultad) {
        pararContador();
        bloquearTablero();
        alertaDerrota(dificultad);
        System.out.println("Game Over");
    }

    // Método para manejar el fin del juego
    public void victoria(Dificultad dificultad) {
        pararContador();
        bloquearTablero();
        alertaVictoria(dificultad);
    }

    // Método para bloquear el tablero
    private void bloquearTablero() {
        juegoTerminado = true; // Marcamos que el juego ha terminado

        // Recorremos todas las casillas en el tablero
        for (Node node : gridpane.getChildren()) {
            Button button = (Button) node;

            // Añadimos un filtro para capturar cualquier evento de clic
            button.addEventFilter(MouseEvent.ANY, Event::consume);
            // Esto hace que cualquier interacción con los botones sea ignorada,
            // como si los botones estuvieran "apagados", pero sin cambiar su apariencia y no se vean opacos
        }
    }

    // Método para mostrar todas las minas
    public void mostrarMinas() {
        for (int i = 0; i < tabla.length; i++) {
            for (int j = 0; j < tabla[i].length; j++) {
                if (tablaBoolean[i][j]) {
                    Button minaBoton = (Button) getNodeByRowColumnIndex(i, j, gridpane);
                    if (minaBoton != null && minaBoton.getGraphic() == null) {
                        minaBoton.setGraphic(new ImageView(new Image("file:./src/main/java/com/mycompany/buscaminasjoan/imagenes/mina.png", 12, 12, true, true)));
                    }
                }
            }
        }
    }

    // Método para obtener un nodo por su fila y columna en el GridPane
    private Node getNodeByRowColumnIndex(int row, int column, GridPane gridPane) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                return node;
            }
        }
        return null;
    }

// Método para resetear el contador
    public void resetearContador() {
        pararContador(); // Detener el Timeline actual si está corriendo
        segundos = 0;    // Reiniciar los segundos
        minutos = 0;     // Reiniciar los minutos
        // Actualizar el labelTime en el hilo de la UI
        Platform.runLater(() -> labelTime.setText("Time: " + minutos + ":00"));
    }

    public void alertaDerrota(Dificultad dificultad) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Derrota ");
        alert.setHeaderText("Has perdido");
// ó nullsi no queremos cabecera 
        String contenido = "No lograste ganar" + "\n"
                + "Minas encontradas: " + minasAcertadas + "/" + dificultad.getMinas();

        alert.setContentText(contenido);
        alert.showAndWait();
    }

    public void alertaVictoria(Dificultad dificultad) {
        // Crear una alerta de tipo confirmación
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Victoria ");
        alert.setHeaderText("Enhorabuena has Ganado!");

        // Formatear el tiempo transcurrido en el formato mm:ss
        String tiempoTranscurrido = String.format("%02d:%02d", minutos, segundos);

        // Crear el contenido del mensaje de la alerta
        String contenido = "Tiempo en ganar: " + tiempoTranscurrido + "\n"
                + "Minas encontradas: " + minasAcertadas + "/" + dificultad.getMinas();

        alert.setContentText(contenido);

        // Mostrar la alerta y esperar la respuesta del usuario
        Optional<ButtonType> result = alert.showAndWait();

        // Verificar si el usuario presionó el botón OK
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Crear un diálogo de entrada de texto para pedir el nombre del jugador
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Nombre del Jugador");
            dialog.setHeaderText("Has ganado!");
            dialog.setContentText("Por favor, ingresa tu nombre:");

            // Mostrar el diálogo y esperar la entrada del usuario
            Optional<String> nombreResult = dialog.showAndWait();

            // Verificar si el usuario ingresó un nombre válido (no vacío)
            if (nombreResult.isPresent() && !nombreResult.get().trim().isEmpty()) {
                String nombre = nombreResult.get();
                // Crear una nueva instancia de Persona con los datos del jugador
                Persona nuevaPersona = new Persona(nombre, tiempoTranscurrido, dificultad.name().toLowerCase(), "./src/main/java/com/mycompany/buscaminasjoan/imagenes/ranking.png");

                // Llamar al método para agregar la nueva persona al TableView
                tableView.agregarPersona(nuevaPersona);
            } else {
                // Si el nombre es inválido (vacío), mostrar una alerta de error
                Alert errorAlert = new Alert(AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText("Nombre inválido");
                errorAlert.setContentText("El nombre no puede estar vacío.");
                errorAlert.showAndWait();
            }
        }
    }

    public void setTableViewController(TableViewController tableViewController) {
        this.tableView = tableViewController;
    }

    // Método para manejar el evento de selección de dificultad
    @FXML
    private void mandarDif(ActionEvent event) {
        resetearContador();
        crearTabla(cboxDif.getValue());
        contador();
    }

    public void menu() {
        File imageFile = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/exit.png");
        String fileLocation = imageFile.toURI().toString();
        Image image = new Image(fileLocation, 520, 526, true, true);
        imagenExit.setImage(image);

        File imageFile1 = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/info.png");
        String fileLocation1 = imageFile1.toURI().toString();
        Image image1 = new Image(fileLocation1, 520, 526, true, true);
        imagenInfo.setImage(image1);

        File imageFile2 = new File("./src/main/java/com/mycompany/buscaminasjoan/imagenes/ranking.png");
        String fileLocation2 = imageFile2.toURI().toString();
        Image image2 = new Image(fileLocation2, 520, 526, true, true);
        imagenRanking.setImage(image2);
    }

    public void acercaDe() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Acerca de mi");
        alert.setHeaderText("Informacion");

        String informacion = "Proyecto creado por: Joan Villanuea Rodriguez                       "
                + "Correo: villanuevajoan12345@gmail.com";
        alert.setContentText(informacion);
        alert.showAndWait();
    }

    @FXML
    private void abrirRanking(ActionEvent event) throws IOException {
        FXMLLoader miCargador = new FXMLLoader(
                getClass().getClassLoader().getResource(
                        "com/mycompany/buscaminasjoan/ranking.fxml")
        );

        Parent root = miCargador.load();

        RankingController controladorRanking = miCargador.<RankingController>getController();
        controladorRanking.setPersonas(new ArrayList<>(tableView.getMisPersonas()));

        Scene scene = new Scene(root, 450, 650);
        Stage stage = new Stage();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Ver datos persona");
        stage.initModality(Modality.APPLICATION_MODAL); // La ventana se muestra en modal
        stage.showAndWait(); // Espera a que se cierre la segunda ventana
    }

    public static void intrucciones() {
        String intrucciones = "Cómo jugar al Buscaminas:\n\n"
                + "1. El objetivo del juego es descubrir todas las casillas que no contienen minas.\n"
                + "2. Usa el botón izquierdo del ratón para revelar una casilla.\n"
                + "3. Si revelas una casilla con una mina, pierdes el juego.\n"
                + "4. Los números en las casillas reveladas indican cuántas minas hay en las casillas adyacentes.\n"
                + "5. Usa el botón derecho del ratón para marcar una casilla que crees que contiene una mina.\n"
                + "6. Despeja todas las casillas sin minas para ganar el juego.\n\n"
                + "¡Buena suerte!";

        JOptionPane.showMessageDialog(null, intrucciones, "Instrucciones de Buscaminas", JOptionPane.INFORMATION_MESSAGE);
    }
}
