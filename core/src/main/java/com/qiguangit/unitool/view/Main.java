package com.qiguangit.unitool.view;

import com.qiguangit.unitool.plugin.PluginManager;
import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        PluginManager.getManager().parsePluginJars();
        Application.launch(App.class, args);
    }
}
