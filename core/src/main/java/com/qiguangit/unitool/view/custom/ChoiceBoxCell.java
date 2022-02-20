package com.qiguangit.unitool.view.custom;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.Map;

public class ChoiceBoxCell<T> extends TableCell<T, String> {
    private T name;
    private Object[] choiceBoxStrings;
    private ChoiceBox<Object> choiceBox;

    public ChoiceBoxCell(final T name, final Object[] choiceBoxStrings) {
        this.name = name;
        this.choiceBoxStrings = choiceBoxStrings;
    }

    @Override
    public void startEdit() {
        if (!isEmpty()) {
            super.startEdit();
            createTextField();
            choiceBox.show();
            setText(null);
            getGraphic().requestFocus();
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            this.setText((String)null);
            this.setGraphic((Node)null);
        } else {
            setText(getString());
//            setGraphic(null);
        }
    }

    private void createTextField() {
        choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(choiceBoxStrings);
        ObservableList<T> tableData = getTableView().getItems();
        choiceBox.setValue(((Map)tableData.get(this.getIndex())).get(name));
        choiceBox.valueProperty().addListener((obVal, oldVal, newVal) -> {
            ((Map)tableData.get(this.getIndex())).put(name, newVal);
        });
        this.setGraphic(choiceBox);
        choiceBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        choiceBox.focusedProperty().addListener((ob, old, now) -> {
            if (!now) {
                commitEdit((String) choiceBox.getValue());
            }
        });
//        choiceBox.setOnKeyPressed(e -> {
//            if (e.getCode() == KeyCode.TAB) {
//                final TableView<T> tableView = getTableView();
//                final int columnIndex = tableView.getEditingCell().getColumn();
//                final int rowIndex = tableView.getEditingCell().getRow();
//                final ObservableList columns = tableView.getColumns();
//                final ObservableList items = tableView.getItems();
//

//                if (columnIndex < columns.size() - 1) {
//                    TableColumn column =  getTableColumnByPosition(tableView, columnIndex + 1);
//                    tableView.edit(rowIndex, column);
//                } else if (rowIndex < items.size() - 1) {
//                    TableColumn column =  getTableColumnByPosition(tableView, 0);
//                    tableView.edit(rowIndex + 1, column);
//                }
//            }
//        });
    }

    private TableColumn getTableColumnByPosition(TableView tableView, int position) {
        return (TableColumn) tableView.getColumns().get(position);
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}
