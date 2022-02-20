package com.qiguangit.unitool.plugin.generate;

import com.qiguangit.unitool.view.observable.AppObservable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GenerateApp extends Application {

    public static final AppObservable observable = new AppObservable();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/resources/fxml/generateLayout.fxml"));
        primaryStage.setTitle("代码生成器");
        final Scene scene = new Scene(root, 700, 800);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            observable.notifyAppObservers();
            System.out.println("监听到关闭事件" + e.getEventType().getName());
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
