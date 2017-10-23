package org.inframincer.servicebasics;

import java.util.Observable;

/**
 * Created by yoon on 2017. 10. 23..
 */

public class HelloObservable extends Observable {

    private static HelloObservable sHelloObservable;

    public static HelloObservable getInstance() {
        if (sHelloObservable == null) {
            sHelloObservable = new HelloObservable();
        }
        return sHelloObservable;
    }

    private HelloObservable() {
    }

    public void updateValue(Object arg) {
        synchronized (this) {
            setChanged();
            notifyObservers(arg);
        }
    }
}
