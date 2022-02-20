package com.qiguangit.unitool.view.observable;

import java.util.Observable;

public class AppObservable extends Observable {

    public void notifyAppObservers() {
        setChanged();
        notifyObservers();
        clearChanged();
    }
}
