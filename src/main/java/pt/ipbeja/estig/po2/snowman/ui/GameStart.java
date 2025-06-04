// Autor: 16399 - Hugo Jeremias
package pt.ipbeja.estig.po2.snowman.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.Optional;


public class GameStart extends Application {
    @Override
    /*public void start(Stage stage) {

        int level = showLevelSelectionDialog(stage);

        Label movesLabel = new Label("Jogadas:");
        movesLabel.setWrapText(true);

        VBox contentBox = new VBox(movesLabel);
        contentBox.setFillWidth(true);

        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(100);

        GameBoard gameBoard = new GameBoard(movesLabel, level); // Adiciona o nível ao construtor

        VBox root = new VBox();
        root.getChildren().addAll(gameBoard, scrollPane);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("A Good Snowman is Hard to Build");
        stage.show();

        gameBoard.requestFocus();

    }*/
    public void start(Stage stage) {
        // Diálogo para nome e nível
        Dialog<Pair<String, Integer>> dialog = new Dialog<>();
        dialog.setTitle("Configuração do Jogo");

        Label nameLabel = new Label("Nome:");
        TextField nameField = new TextField();
        nameField.setPromptText("Insira o seu nome");

        Label levelLabel = new Label("Nível:");
        javafx.scene.control.ChoiceBox<Integer> levelChoice = new javafx.scene.control.ChoiceBox<>();
        levelChoice.getItems().addAll(1, 2);
        levelChoice.setValue(1);

        VBox dialogContent = new VBox(10, nameLabel, nameField, levelLabel, levelChoice);
        dialog.getDialogPane().setContent(dialogContent);
        dialog.getDialogPane().getButtonTypes().addAll(javafx.scene.control.ButtonType.OK);

        dialog.setResultConverter(btn -> {
            if (btn == javafx.scene.control.ButtonType.OK) {
                return new javafx.util.Pair<>(nameField.getText(), levelChoice.getValue());
            }
            return null;
        });

        Optional<Pair<String, Integer>> result = dialog.showAndWait();
        String playerName = result.map(Pair::getKey).orElse("Jogador");
        int level = result.map(Pair::getValue).orElse(1);

        // Painel de informações à direita
        Label playerNameLabel = new Label("Jogador: " + playerName);
        Label scoreLabel = new Label("Pontuação: ");

        // Legenda visual
        VBox legendBox = new VBox(5);
        legendBox.getChildren().add(new Label("Legenda:"));

        // Bola de neve pequena (azul)
        HBox snowballLegend = new HBox(5, new Circle(8, javafx.scene.paint.Color.LIGHTBLUE), new Label("Bola de neve pequena"));
        // Monstro (vermelho)
        HBox monsterLegend = new HBox(5, new Circle(8, javafx.scene.paint.Color.RED), new Label("Monstro"));
        // Neve (branco)
        HBox snowLegend = new HBox(5, new Rectangle(16, 16, javafx.scene.paint.Color.WHITE), new Label("Neve"));
        // Bloco (preto)
        HBox blockLegend = new HBox(5, new Rectangle(16, 16, javafx.scene.paint.Color.BLACK), new Label("Bloco"));
        // Setas
        HBox arrowsLegend = new HBox(5, new Label("\u2190 \u2191 \u2192 \u2193"), new Label("Mover personagem"));
        // Objetivo
        HBox goalLegend = new HBox(5, new Label("🎯"), new Label("Construir o boneco de neve!"));

        legendBox.getChildren().addAll(snowballLegend, monsterLegend, snowLegend, blockLegend, arrowsLegend, goalLegend);

        VBox infoPane = new VBox(10, playerNameLabel, scoreLabel, legendBox);
        infoPane.setStyle("-fx-padding: 10; -fx-background-color: #f0f0f0;");

        // Painel de jogadas
        Label movesLabel = new Label("Jogadas:");
        movesLabel.setWrapText(true);
        VBox contentBox = new VBox(movesLabel);
        //contentBox.setFillWidth(true);
        ScrollPane scrollPane = new ScrollPane(contentBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(100);
        scrollPane.setMaxWidth(550);

        GameBoard gameBoard = new GameBoard(movesLabel, level, scoreLabel);
// Tabuleiro e painel de jogadas juntos
        VBox leftPane = new VBox(gameBoard, scrollPane);
        leftPane.setMaxWidth(400); // Limita a largura máxima do painel esquerdo

// Layout principal: tabuleiro à esquerda, info à direita
        HBox root = new HBox(10, leftPane, infoPane);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("A Good Snowman is Hard to Build");
        stage.show();

        gameBoard.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }

    private int showLevelSelectionDialog(Stage stage) {
        javafx.scene.control.ChoiceDialog<Integer> dialog = new javafx.scene.control.ChoiceDialog<>(1, 1, 2);
        dialog.setTitle("Selecionar Nível");
        dialog.setHeaderText("Escolha o nível para jogar:");
        dialog.setContentText("Nível:");
        Optional<Integer> result = dialog.showAndWait();
        return result.orElse(1);
    }
}
