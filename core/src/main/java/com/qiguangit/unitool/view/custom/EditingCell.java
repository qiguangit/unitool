package com.qiguangit.unitool.view.custom;

import javafx.collections.ObservableList;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

public class EditingCell<T> extends TableCell<T, String> {
    private TextField textField;

    public EditingCell() {
    }


    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
            getGraphic().requestFocus();
        }
    }


    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }


    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            setText(getString());
            setGraphic(null);
        }
    }


    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.focusedProperty().addListener((ob, old, now) -> {
            if (!now) {
                commitEdit(textField.getText());
            }
        });
        textField.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.TAB) {
                TableView<T> tableView = getTableView();
                int columnIndex = tableView.getEditingCell().getColumn();
                int rowIndex = tableView.getEditingCell().getRow();
                ObservableList columns = tableView.getColumns();
                ObservableList items = tableView.getItems();
                commitEdit(textField.getText());
                if (columnIndex < columns.size() - 1) {
                    TableColumn column =  getTableColumnByPosition(tableView, columnIndex + 1);
                    tableView.edit(rowIndex, column);
                } else if (rowIndex < items.size() - 1) {
                    TableColumn column =  getTableColumnByPosition(tableView, 0);
                    tableView.edit(rowIndex + 1, column);
                }
            }
        });
    }

    private TableColumn getTableColumnByPosition(TableView tableView, int position) {
        return (TableColumn) tableView.getColumns().get(position);
    }


    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
