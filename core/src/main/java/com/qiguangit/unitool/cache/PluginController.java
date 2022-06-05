package com.qiguangit.unitool.cache;

import com.qiguangit.unitool.controller.BaseController;
import javafx.scene.Node;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PluginController {
    private BaseController controller;
    private Node node;
}
