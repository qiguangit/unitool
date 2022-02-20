package com.qiguangit.unitool.plugin.generate.controller;

import com.google.gson.Gson;
import com.qiguangit.unitool.plugin.generate.generator.EntityGenerator;
import com.qiguangit.unitool.plugin.generate.generator.HibernateTemplateGenerator;
import com.qiguangit.unitool.plugin.generate.model.HibernateModel;
import com.qiguangit.unitool.plugin.generate.GenerateApp;
import com.qiguangit.unitool.util.BeanUtils;
import com.qiguangit.unitool.view.util.TableViewUtils;
import freemarker.template.TemplateException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

public class GenerateController implements Initializable, Observer {
    @FXML
    private TableView tvContent;
    @FXML
    private TextField tfTableNm;
    @FXML
    private TextField tfPackageNm;
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
    private HibernateModel model;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        GenerateApp.observable.addObserver(this);
        initData();
        initView();
        initListener();
    }

    private void initListener() {
        tfTableNm.textProperty().addListener((observable, oldValue, newValue) -> model.setTableName(newValue));
        tfPackageNm.textProperty().addListener((observable, oldValue, newValue) -> model.setPackageName(newValue));
    }

    private void initData() {
        model = new HibernateModel();
        model.setFields(new ArrayList<>());
        final HashMap<String, Object> newLineData = new HashMap<>();
        newLineData.put("typeColumn", "varchar");
        newLineData.put("notNullColumn", "true");
        newLineData.put("lengthColumn", "0");
        data = FXCollections.observableArrayList(newLineData);
        readCache();
    }

    private void readCache() {
        String property = System.getProperty("user.dir");
        try {
            final byte[] bytes = Files.readAllBytes(Paths.get(property + "/data.json"));
            if (bytes.length > 0) {
                model = new Gson().fromJson(new String(bytes, StandardCharsets.UTF_8), HibernateModel.class);
                tfPackageNm.textProperty().setValue(model.getPackageName());
                tfTableNm.textProperty().setValue(model.getTableName());
                data = FXCollections.observableArrayList();
                model.getFields().forEach(field -> {
                    final Map<String, Object> fields = BeanUtils.bean2Map(field);
                    data.add(fields);
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
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
        newLineData.put("type", "varchar");
        newLineData.put("notNull", "true");
        newLineData.put("length", "0");
        data.add(newLineData);
        tvContent.edit(data.size() - 1, (TableColumn) tvContent.getColumns().get(0));
    }

    public void onGenerate(ActionEvent actionEvent) {
        model.setClassName(tfTableNm.getText());
        tvContent.getItems()
                 .forEach((Consumer<Map<String, Object>>) o -> {
                     final HibernateModel.Field field = BeanUtils.map2Bean(o, HibernateModel.Field.class);
                     model.getFields().add(field);
                 });
        try {
            HibernateTemplateGenerator
                    .getInstance()
                    .generateHbmXml(model, new OutputStreamWriter(System.out));
            EntityGenerator
                    .getInstance()
                    .generateEntity(model, new OutputStreamWriter(System.out));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        final String property = System.getProperty("user.dir");
//        final List<HibernateModel.Field> fields = model.getFields();
//        fields.clear();
//        data.forEach(map-> {
//            final HibernateModel.Field field = BeanUtils.map2Bean(map, HibernateModel.Field.class);
//            fields.add(field);
//        });
//
//        final String jsonModel = new Gson().toJson(model);
//        try {
//            Files.write(Paths.get(property + "/data.json"), jsonModel.getBytes("UTF-8"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("user dir：" + property);
    }
}
