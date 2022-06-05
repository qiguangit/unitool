package com.qiguangit.unitool.plugin.generate.controller;

import com.qiguangit.unitool.controller.BaseController;
import com.qiguangit.unitool.plugin.SystemVariable;
import com.qiguangit.unitool.plugin.generate.util.VariableUtils;
import com.qiguangit.unitool.view.util.TableViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TemplateController extends BaseController {
    @FXML
    private TableView tvTemplates;
    @FXML
    private TableColumn checkColumn;
    @FXML
    private TableColumn idxColumn;
    @FXML
    private TableColumn templateColumn;
    @FXML
    private TableColumn commentColumn;

    private ObservableList<Map<String, Object>> data;

    @Override
    protected void initData() {
        data = FXCollections.observableArrayList();
        File templateFile = new File(SystemVariable.get(SystemVariable.KEY_TEMPLATE_PATH));
        final File[] files = templateFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f =  files[i];
            Map<String, Object> map = new HashMap<>();
            map.put("idx", i + 1);
            map.put("check", "false");
            map.put("template", f.getName());
            map.put("comment", VariableUtils.getComment(f.getName()));
            data.add(map);
        }

    }

    @Override
    protected void initView() {
        TableViewUtils.setTableColumnMapAsCheckBoxValueFactory(checkColumn, "check");
        TableViewUtils.setTableColumnMapAsLabelValueFactory(templateColumn, "template");
        TableViewUtils.setTableColumnMapAsLabelValueFactory(commentColumn, "comment");
        TableViewUtils.setTableColumnMapAsLabelValueFactory(idxColumn, "idx");
        tvTemplates.setItems(data);
        tvTemplates.setEditable(true);
    }

    @Override
    protected void initListener() {

    }
}
