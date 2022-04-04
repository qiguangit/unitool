package com.qiguangit.unitool.view.controller;

import com.qiguangit.unitool.cache.LRUCache;
import com.qiguangit.unitool.cache.PluginController;
import com.qiguangit.unitool.controller.BaseController;
import com.qiguangit.unitool.plugin.PluginManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
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

    private final LRUCache<String, PluginController> nodeCache = new LRUCache<>(10);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        PluginManager.getManager()
                .getPluginJarInfos()
                .forEach((pluginKey, info) -> {
                    info.getFxConfigurations().forEach(config -> {
                        MenuItem menuItem = new MenuItem(config.getTitle());
                        menuItem.setOnAction(handler -> {
                            try {
                                PluginController pluginController = nodeCache.get(pluginKey);
                                if (pluginController == null) {
                                    FXMLLoader loader = new FXMLLoader();
                                    loader.setBuilderFactory(new JavaFXBuilderFactory());
                                    loader.setLocation(getClass().getResource(config.getFxmlPath()));
                                    Node node = loader.load();
                                    BaseController controller = loader.getController();
                                    pluginController = new PluginController(controller, node);
                                    nodeCache.put(pluginKey, pluginController);
                                }

                                Tab tab = new Tab();
                                tab.contentProperty().setValue(pluginController.getNode());
                                tab.setText(config.getTitle());
                                PluginController finalPluginController = pluginController;
                                tab.setOnCloseRequest(event -> {
                                    finalPluginController.getController().onCloseAction();
                                });
                                tabPane.getTabs().add(tab);
                                tabPane.getSelectionModel().select(tab);
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
        Platform.exit();
    }

    public void onMenuPluginListener(ActionEvent actionEvent) {
    }
}
