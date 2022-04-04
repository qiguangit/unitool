package com.qiguangit.unitool.view.util;

import com.qiguangit.unitool.view.custom.ChoiceBoxCell;
import com.qiguangit.unitool.view.custom.EditingCell;
import javafx.event.EventHandler;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.MapValueFactory;

import java.util.Map;

public class TableViewUtils {

    public static void setColumnMapValueFactory(TableColumn tableColumn, final String name, boolean isEdit) {
        setColumnMapValueFactory(tableColumn, name, isEdit, null);
    }

    public static void setColumnMapValueFactory(TableColumn tableColumn, final String name, boolean isEdit, final Runnable onEditCommitHandle) {
        tableColumn.setCellValueFactory(new MapValueFactory(name));
        tableColumn.setCellFactory(p -> new EditingCell<>());
        if (isEdit) {
            tableColumn.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<Map<String, String>, String>>) t -> {
                ((Map) t.getRowValue()).put(name, t.getNewValue());
                if (onEditCommitHandle != null) {
                    onEditCommitHandle.run();
                }

            });
        }
    }

    public static void setTableColumnMapAsChoiceBoxValueFactory(final TableColumn tableColumn, final String name, final Object[] choiceBoxStrings) {
        tableColumn.setCellValueFactory(new MapValueFactory(name));
        tableColumn.setCellFactory(p -> new ChoiceBoxCell<>(name, choiceBoxStrings));
    }

    public static void setTableColumnMapAsLabelValueFactory(final TableColumn tableColumn, final String name) {
        tableColumn.setCellValueFactory(new MapValueFactory(name));
    }

}
