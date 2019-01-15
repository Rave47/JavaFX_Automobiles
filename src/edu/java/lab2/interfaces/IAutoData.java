package edu.java.lab2.interfaces;

import edu.java.lab2.interfaces.impls.AutoData;
import edu.java.lab2.objects.Automobile;

public interface IAutoData {

    void open();
    void save();
    void saveAs();

    void add(Automobile automobile);
    void delete(Automobile automobile);
    void deleteAll();
}
