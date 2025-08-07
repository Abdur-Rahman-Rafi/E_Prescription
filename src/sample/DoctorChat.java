package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class DoctorChat extends Application {

    private ServerSocket serverSocket;
    private VBox messagesArea;
    private Label patientStatusLabel;
    private TextField messageField;

    private Socket clientSocket;
    private DataInputStream fromClient;
    private DataOutputStream toClient;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        messagesArea = new VBox(10);  // Holds chat bubbles, spacing between messages
        messagesArea.setPadding(new Insets(10));
        messagesArea.setStyle("-fx-background-color: #f9f9f9;");
        messagesArea.setMaxHeight(Double.MAX_VALUE);

        ScrollPane scrollPane = new ScrollPane(messagesArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 20px; -fx-padding: 8px 15px;");
        sendButton.setOnAction(event -> sendMessage());
        sendButton.setOnMouseEntered(event -> sendButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 20px; -fx-padding: 8px 15px;"));
        sendButton.setOnMouseExited(event -> sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 20px; -fx-padding: 8px 15px;"));

        messageField = new TextField();
        messageField.setFont(Font.font("Arial", 14));
        messageField.setPromptText("Type your message here...");
        messageField.setStyle("-fx-padding: 10px; -fx-background-radius: 10px; -fx-border-color: #ccc; -fx-border-radius: 10px;");

        HBox messageBox = new HBox(10, messageField, sendButton);
        messageBox.setPadding(new Insets(10));
        messageBox.setAlignment(Pos.CENTER);

        Label messagesLabel = new Label("Messages:");
        messagesLabel.setFont(Font.font("Arial", 16));
        messagesLabel.setTextFill(Color.web("#4CAF50"));

        BorderPane root = new BorderPane();
        root.setCenter(scrollPane);
        root.setBottom(messageBox);
        root.setPadding(new Insets(10));
        root.setBackground(new Background(new BackgroundFill(Color.web("#F5F5F5"), CornerRadii.EMPTY, Insets.EMPTY)));

        GridPane statusPane = new GridPane();
        statusPane.setHgap(10);
        statusPane.setVgap(5);
        statusPane.setPadding(new Insets(10));
        statusPane.setAlignment(Pos.CENTER_LEFT);

        patientStatusLabel = new Label("No Patient is Online");
        patientStatusLabel.setFont(Font.font("Arial", 14));
        patientStatusLabel.setTextFill(Color.RED);
        statusPane.add(new Label("Patient status: "), 0, 0);
        statusPane.add(patientStatusLabel, 1, 0);

        root.setTop(statusPane);

        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.setTitle("Doctor Chat");
        primaryStage.show();

        // start the server socket and listen for connections
        startServer();
    }

    private void startServer() {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(8000);
                Platform.runLater(() -> patientStatusLabel.setText("No Patient is Online..."));
                clientSocket = serverSocket.accept();
                Platform.runLater(() -> {
                    patientStatusLabel.setText("Patient is online");
                    patientStatusLabel.setTextFill(Color.GREEN);
                });

                fromClient = new DataInputStream(clientSocket.getInputStream());
                toClient = new DataOutputStream(clientSocket.getOutputStream());

                while (true) {
                    String message = fromClient.readUTF();
                    Platform.runLater(() -> addMessage(message, "Patient"));
                }
            } catch (IOException e) {
                Platform.runLater(() -> patientStatusLabel.setText("Error: " + e.getMessage()));
            }
        }).start();
    }

    private void sendMessage() {
        try {
            if (clientSocket != null) {
                String message = messageField.getText().trim();
                if (!message.isEmpty()) {
                    toClient.writeUTF(message);
                    Platform.runLater(() -> addMessage(message, "Doctor"));
                    messageField.clear();
                }
            }
        } catch (IOException e) {
            Platform.runLater(() -> patientStatusLabel.setText("Error: " + e.getMessage()));
        }
    }

    // Add message as chat bubble to messagesArea
    private void addMessage(String message, String sender) {
        HBox messageBubble = new HBox();
        messageBubble.setPadding(new Insets(10));

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setFont(Font.font("Arial", 14));
        messageLabel.setPadding(new Insets(10));

        if ("Doctor".equals(sender)) {
            messageBubble.setAlignment(Pos.BASELINE_RIGHT); // Right alignment for doctor's messages
            messageLabel.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        } else {
            messageBubble.setAlignment(Pos.BASELINE_LEFT); // Left alignment for patient's messages
            messageLabel.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: black; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        }

        messageBubble.getChildren().add(messageLabel);
        messagesArea.getChildren().add(messageBubble);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        if (clientSocket != null) {
            clientSocket.close();
        }
        if (serverSocket != null) {
            serverSocket.close();
        }
    }
}
