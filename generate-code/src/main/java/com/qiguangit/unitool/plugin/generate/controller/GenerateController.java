package com.qiguangit.unitool.plugin.generate.controller;

import com.google.gson.Gson;
import com.qiguangit.unitool.controller.BaseController;
import com.qiguangit.unitool.plugin.generate.generator.TemplateGenerator;
import com.qiguangit.unitool.plugin.generate.model.TableModel;
import com.qiguangit.unitool.plugin.generate.util.VariableUtils;
import com.qiguangit.unitool.util.BeanUtils;
import com.qiguangit.unitool.view.util.TableViewUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class GenerateController extends BaseController {
    @FXML
    private TableView tvContent;
    @FXML
    private TextField tvProjectPath;
    @FXML
    private TextField tfTableNm;
    @FXML
    private TextField tfPackageNm;
    @FXML
    private TableColumn idxColumn;
    @FXML
    private TableColumn attrNameColumn;
    @FXML
    private TableColumn typeColumn;
    @FXML
    private TableColumn notNullColumn;
    @FXML
    private TableColumn commentColumn;
    @FXML
    private TableColumn lengthColumn;
    @FXML
    private TableColumn defaultValueColumn;
    @FXML
    private VBox rootView;
    private ObservableList<Map<String, Object>> data;
    private TableModel model;

    public void initListener() {
        tfTableNm.textProperty().addListener((observable, oldValue, newValue) -> model.setTableName(newValue));
        tfPackageNm.textProperty().addListener((observable, oldValue, newValue) -> model.setPackageName(newValue));
        tvProjectPath.textProperty().addListener((observable, oldValue, newValue) -> {
            model.setProjectPath(newValue);
            VariableUtils.put("projectPath", newValue);
        });
    }

    public void initData() {
        model = new TableModel();
        model.setFields(new ArrayList<>());
        HashMap<String, Object> newLineData = new HashMap<>();
        newLineData.put("typeColumn", "varchar");
        newLineData.put("notNullColumn", "true");
        newLineData.put("lengthColumn", "0");
        data = FXCollections.observableArrayList(newLineData);
        readCache();
    }

    private void readCache() {
        String property = System.getProperty("user.dir");
        try {
            Path path = Paths.get(property + "/data.json");
            if (path.toFile().exists()) {
                byte[] bytes = Files.readAllBytes(path);
                if (bytes.length > 0) {
                    model = new Gson().fromJson(new String(bytes, StandardCharsets.UTF_8), TableModel.class);
                    tfPackageNm.textProperty().setValue(model.getPackageName());
                    tfTableNm.textProperty().setValue(model.getTableName());
                    data = FXCollections.observableArrayList();
                    model.getFields().forEach(field -> {
                        Map<String, Object> fields = BeanUtils.bean2Map(field);
                        fields.put("idx", data.size() + 1);
                        data.add(fields);
                    });
                }
            }
        } catch (IOException e) {
            logger.info("read cache error", e);
        }
    }

    public void initView() {
        TableViewUtils.setTableColumnMapAsLabelValueFactory(idxColumn, "idx");
        TableViewUtils.setColumnMapValueFactory(attrNameColumn, "attrName", true);
        TableViewUtils.setTableColumnMapAsChoiceBoxValueFactory(typeColumn, "type", new Object[] {"int", "varchar", "text", "float"});
        TableViewUtils.setTableColumnMapAsChoiceBoxValueFactory(notNullColumn, "notNull", new Object[]{"true", "false"});
        TableViewUtils.setColumnMapValueFactory(commentColumn, "comment", true);
        TableViewUtils.setColumnMapValueFactory(lengthColumn, "length", true);
        TableViewUtils.setColumnMapValueFactory(defaultValueColumn, "defaultValue", true);
        tvContent.setPlaceholder(new Text("No content in table"));
        tvContent.setItems(data);
        tvContent.setEditable(true);
    }

    public void onAddLine(ActionEvent actionEvent) {
        final HashMap<String, Object> newLineData = new HashMap<>();
        newLineData.put("idx", data.size() + 1);
        newLineData.put("type", "varchar");
        newLineData.put("notNull", "true");
        newLineData.put("length", "0");
        data.add(newLineData);
        tvContent.edit(data.size() - 1, (TableColumn) tvContent.getColumns().get(0));
    }

    public void onGenerate(ActionEvent actionEvent) {
        model.setClassName(tfTableNm.getText());
        model.getFields().clear();
        tvContent.getItems()
                 .forEach((Consumer<Map<String, Object>>) o -> {
                     final TableModel.Field field = BeanUtils.map2Bean(o, TableModel.Field.class);
                     model.getFields().add(field);
                 });
        try {
            String modelStr = new Gson().toJson(model);
            TemplateGenerator
                    .getInstance()
                    .generate(modelStr, "${tableUpperCamelCase}[.xml](hibernate-mapping).ftl", VariableUtils.get("projectPath"))
                    .generate(modelStr, "${tableUpperCamelCase}[.java](entity).ftl", VariableUtils.get("projectPath"));
        } catch (Exception e) {
            logger.error("generate error", e);
        }
    }

    @Override
    public void onCloseAction() {
        super.onCloseAction();
        String property = System.getProperty("user.dir");
        List<TableModel.Field> fields = model.getFields();
        fields.clear();
        data.forEach(map-> {
            TableModel.Field field = BeanUtils.map2Bean(map, TableModel.Field.class);
            fields.add(field);
        });

        String jsonModel = new Gson().toJson(model);
        try {
            Files.write(Paths.get(property + "/data.json"), jsonModel.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            logger.error("cache write error", e);
        }
    }

    public void showDirectoryChooser(ActionEvent actionEvent) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        File selectedDirectory =
                directoryChooser.showDialog(rootView.getScene().getWindow());
        if (selectedDirectory != null) {
            tvProjectPath.setText(selectedDirectory.getPath());
        }
    }

    public void showVariableDialog(ActionEvent actionEvent) {
        try {
            Stage stage = new Stage();

            FXMLLoader loader = new FXMLLoader();
            loader.setBuilderFactory(new JavaFXBuilderFactory());
            VBox vBox = loader.load(getClass().getResourceAsStream("/fxml/variableDialog.fxml"));
            VariableDialogController controller =  loader.getController();
            Scene scene = new Scene(vBox);
            stage.setScene(scene);
            stage.initStyle(StageStyle.DECORATED);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(rootView.getScene().getWindow());
            stage.setOnCloseRequest(e->{
                VariableUtils.clear();
                controller.getData().forEach(map -> {
                    String key = (String) map.get("key");
                    String value = (String) map.get("value");
                    VariableUtils.put(key, value);
                });
            });
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
