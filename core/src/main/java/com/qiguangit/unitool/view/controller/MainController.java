package com.qiguangit.unitool.view.controller;

import com.qiguangit.unitool.plugin.PluginManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private MenuItem menuExit;

    @FXML
    private Menu menuPlugin;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        PluginManager.getManager()
                .getPluginJarInfos()
                .forEach((s, info) -> {
                    info.getFxConfigurations().forEach(config -> {
                        MenuItem menuItem = new Menu(config.getTitle());
                        menuItem.setOnAction(handler -> {
                            try {
                                final Node node = FXMLLoader.load(getClass().getResource(config.getFxmlPath()));
                                Tab tab = new Tab();
                                tab.contentProperty().setValue(node);
                                tab.setText(config.getTitle());
                                tabPane.getTabs().add(tab);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        menuPlugin.getItems().add(menuItem);
                    });
                });
    }

    public void onMenuConsoleListener(ActionEvent actionEvent) {

    }

    public void onMenuExitListener(ActionEvent actionEvent) {
    }

    public void onMenuPluginListener(ActionEvent actionEvent) {
    }
}
