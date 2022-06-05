package com.qiguangit.unitool.plugin.generate.controller;

import com.qiguangit.unitool.controller.BaseController;
import com.qiguangit.unitool.util.SharedPreferences;
import com.qiguangit.unitool.view.util.TableViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class VariableDialogController extends BaseController {

    @FXML
    private TableView tvVariables;
    @FXML
    private TableColumn idxColumn;
    @FXML
    private TableColumn keyColumn;
    @FXML
    private TableColumn valueColumn;

    private ObservableList<Map<String, Object>> data;

    private final SharedPreferences sp = SharedPreferences.getSharedPreferences("generate");

    public ObservableList<Map<String, Object>> getData() {
        return data;
    }

    @Override
    protected void initData() {
        data = FXCollections.observableArrayList();
        AtomicInteger idx = new AtomicInteger();
        sp.getAll().forEach((k, v) -> {
            HashMap<String, Object> newLineData = new HashMap<>();
            newLineData.put("idx", idx.incrementAndGet());
            newLineData.put("key", k);
            newLineData.put("value", v);
            data.add(newLineData);
        });

    }

    @Override
    protected void initView() {
        TableViewUtils.setColumnMapValueFactory(keyColumn, "key", true);
        TableViewUtils.setColumnMapValueFactory(valueColumn, "value", true);
        TableViewUtils.setTableColumnMapAsLabelValueFactory(idxColumn, "idx");
        tvVariables.setItems(data);
        tvVariables.setEditable(true);
    }

    @Override
    protected void initListener() {

    }

    private void addEmptyRow() {
        HashMap<String, Object> newLineData = new HashMap<>();
        newLineData.put("idx", data.size() + 1);
        newLineData.put("key", "");
        newLineData.put("value", "");
        data.add(newLineData);
        tvVariables.edit(data.size() - 1, keyColumn);
    }

    public void onMouseClick(ActionEvent event) {

        Button btn = (Button) event.getTarget();
        switch (btn.getId()) {
            case "btnAdd":
                addEmptyRow();
                break;
            case "btnRemove":
                int selectedIndex = tvVariables.getSelectionModel().getSelectedIndex();
                if (selectedIndex > -1) {
                    data.remove(selectedIndex);
                }
                break;
            default:
                break;
        }

    }
}
