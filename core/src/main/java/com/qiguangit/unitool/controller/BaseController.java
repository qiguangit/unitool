package com.qiguangit.unitool.controller;

import javafx.fxml.Initializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected abstract void initData();
    protected abstract void initView();
    protected abstract void initListener();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initData();
        initView();
        initListener();
    }

    public void onCloseAction() {
        logger.info(getClass().getName() + " close.");
    }
}
