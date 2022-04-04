package com.qiguangit.unitool.view;

import com.qiguangit.unitool.plugin.PluginManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(getClass().getResource("/resources/main.fxml"));
        primaryStage.setTitle("unitool");
        final Scene scene = new Scene(root, 700, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        PluginManager.getManager().parsePluginJars();
        launch(args);
    }
}
