package com.qiguangit.unitool.plugin.generate;

import com.qiguangit.unitool.controller.BaseController;
import com.qiguangit.unitool.view.observable.AppObservable;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GenerateApp extends Application {

    public static final AppObservable observable = new AppObservable();

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setBuilderFactory(new JavaFXBuilderFactory());
        loader.setLocation(getClass().getResource("/resources/fxml/generateLayout.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("代码生成器");
        Rectangle2D screenRectangle = Screen.getPrimary().getBounds();
        double width = screenRectangle.getWidth();
        double height = screenRectangle.getHeight();
        Scene scene = new Scene(root, width * 3 / 4, height * 3 / 4);
        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(e -> {
            observable.notifyAppObservers();
            final BaseController controller = loader.getController();
            controller.onCloseAction();
        });
        primaryStage.show();
    }
}
