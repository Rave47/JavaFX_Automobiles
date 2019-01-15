package edu.java.lab2.thread;

import edu.java.lab2.objects.Automobile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;

public class MultiThreadSearch extends Thread {

    private int startPoint;
    private int endPoint;
    private String field;
    private ObservableList<Automobile> input;
    private ObservableList<Automobile> output;
    private final static Object lock = new Object();

    public static final Logger log = Logger.getLogger(MultiThreadSearch.class);

    public MultiThreadSearch(int startPoint, int endPoint, String field, ObservableList<Automobile> data, ObservableList<Automobile> result){
        this.startPoint = startPoint;
        this.field = field;
        this.input = data;
        this.output = result;
        this.endPoint = endPoint;
    }

    public void run() {

        ObservableList<Automobile> local = FXCollections.observableArrayList();

        log.info(getName() + " search began");

        for(int i = startPoint; i < endPoint; ++i){

//            log.info(getName() + "iteration #" + i);

            Automobile crt = input.get(i);
            if(crt.getProducer().contains(field)){
                local.add(crt);
                continue;
            }
            if(crt.getModel().contains(field)){
                local.add(crt);
                continue;
            }
            if(crt.getRelease().contains(field)){
                local.add(crt);
                continue;
            }
            if(crt.getBirthplace().contains(field)){
                local.add(crt);
                continue;
            }
            if(crt.getOwner().contains(field)){
                local.add(crt);
                continue;
            }
        }
        log.info(getName() + " search completed");

        synchronized (lock){
            log.info(getName() + " output began");
            for (Automobile crt : local){
                output.add(crt);
            }
            log.info(getName() + " output completed");
        }
    }
}
