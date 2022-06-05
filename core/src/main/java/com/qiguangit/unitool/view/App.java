package com.qiguangit.unitool.view;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/resources/main.fxml"));
        primaryStage.setTitle("unitool");
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double width = screenRectangle.getWidth();
        double height = screenRectangle.getHeight();
        Scene scene = new Scene(root, width * 3 / 4, height * 3 / 4);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
