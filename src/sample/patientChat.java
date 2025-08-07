package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class patientChat extends Application {

    private Socket socket;
    private DataInputStream inputFromServer;
    private DataOutputStream outputToServer;

    private Label statusLabel;
    private VBox chatArea;
    private TextField inputField;

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        // Status Label
        statusLabel = new Label("Doctor Status");
        statusLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #4CAF50;");
        root.setTop(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(10));

        // Chat Area (VBox for Messages) within a ScrollPane
        chatArea = new VBox(10);  // Spacing between chat bubbles
        chatArea.setPadding(new Insets(10));
        chatArea.setStyle("-fx-background-color: #f5f5f5;");

        ScrollPane scrollPane = new ScrollPane(chatArea);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent;");
        root.setCenter(scrollPane);

        // Input Field and Send Button
        inputField = new TextField();
        inputField.setFont(javafx.scene.text.Font.font("Arial", 14));
        inputField.setPromptText("Type your message here...");
        inputField.setStyle("-fx-padding: 10px; -fx-background-radius: 10px; -fx-border-color: #ccc; -fx-border-radius: 10px;");

        Button sendButton = new Button("Send");
        sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 20px; -fx-padding: 8px 15px;");
        sendButton.setOnAction(event -> sendMessage());
        sendButton.setOnMouseEntered(event -> sendButton.setStyle("-fx-background-color: #45a049; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 20px; -fx-padding: 8px 15px;"));
        sendButton.setOnMouseExited(event -> sendButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-border-radius: 20px; -fx-padding: 8px 15px;"));

        HBox inputBox = new HBox(10, inputField, sendButton);  // Spacing between input and button
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));
        root.setBottom(inputBox);

        // Create Scene and Set Stage
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Patient Chat");
        primaryStage.show();

        // Connect to Server
        connectToServer("localhost", 8000);
    }

    private void connectToServer(String host, int port) {
        try {
            socket = new Socket(host, port);
            inputFromServer = new DataInputStream(socket.getInputStream());
            outputToServer = new DataOutputStream(socket.getOutputStream());
            Platform.runLater(() -> statusLabel.setText("Doctor is online"));

            // Start a new thread to handle incoming messages
            new Thread(() -> {
                try {
                    while (true) {
                        String message = inputFromServer.readUTF();
                        Platform.runLater(() -> addMessage(message, "Doctor"));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

        } catch (IOException e) {
            Platform.runLater(() -> statusLabel.setText("Doctor is not online"));
        }
    }

    private void sendMessage() {
        try {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                outputToServer.writeUTF(message);
                addMessage(message, "Patient");
                inputField.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add message as chat bubble to the chatArea
    private void addMessage(String message, String sender) {
        HBox messageBubble = new HBox();
        messageBubble.setPadding(new Insets(10));

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setFont(javafx.scene.text.Font.font("Arial", 14));
        messageLabel.setPadding(new Insets(10));

        if ("Patient".equals(sender)) {
            messageBubble.setAlignment(Pos.BASELINE_RIGHT);  // Right alignment for patient's messages
            messageLabel.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        } else {
            messageBubble.setAlignment(Pos.BASELINE_LEFT);  // Left alignment for doctor's messages
            messageLabel.setStyle("-fx-background-color: #e0e0e0; -fx-text-fill: black; -fx-background-radius: 20px; -fx-border-radius: 20px;");
        }

        messageBubble.getChildren().add(messageLabel);
        chatArea.getChildren().add(messageBubble);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
