package edu.java.lab2.interfaces.impls;

import edu.java.lab2.interfaces.IAutoData;
import edu.java.lab2.objects.Automobile;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.FileChooser;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;


public class AutoData implements IAutoData {

    private ObservableList<Automobile> dataList = FXCollections.observableArrayList();

    private File file;

    private boolean openCheck = true;
    private boolean saveCheck = true;

    public static final Logger log = Logger.getLogger(AutoData.class);

    public ObservableList<Automobile> getDataList() {
        return dataList;
    }

    public File getFile() {
        return file;
    }
    public void resetFile() {
        file = null;
    }
    public boolean getOpenCheck() {
        return openCheck;
    }
    public boolean getSaveCheck() { return saveCheck; }


    @Override
    public void open() {

        FileChooser choice = new FileChooser();
        choice.setTitle("Открыть файл");
        choice.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        choice.setInitialDirectory(new File("./src/edu/java/lab2/data"));
        file = choice.showOpenDialog(null);
        if (file != null) {
            dataList.clear();
            try {
                Scanner scan = new Scanner(file);
                openCheck = true;
                while (scan.hasNext()) {
                    try {
                        Automobile automobile = new Automobile();
                        automobile.setProducer(scan.nextLine());
                        automobile.setModel(scan.nextLine());
                        automobile.setRelease(scan.nextLine());
                        automobile.setBirthplace(scan.nextLine());
                        automobile.setOwner(scan.nextLine());

                        dataList.add(automobile);

                        scan.nextLine();
                    } catch (java.util.NoSuchElementException ex){
                        log.error(ex.toString());
                        openCheck = false;
                        return;
                    }
                }
            } catch (IOException ex) {
                log.error(ex.toString());
                System.out.println(ex.getMessage());
            }
        }
    }

    @Override
    public void save(){
        FileWriter fr = null;
        try {
            fr = new FileWriter(file);
            for (int i = 0; i < this.getDataList().size(); ++i) {
                fr.write(this.getDataList().get(i).getProducer() + System.lineSeparator());
                fr.write(this.getDataList().get(i).getModel() + System.lineSeparator());
                fr.write(this.getDataList().get(i).getRelease() + System.lineSeparator());
                fr.write(this.getDataList().get(i).getBirthplace() + System.lineSeparator());
                fr.write(this.getDataList().get(i).getOwner() + System.lineSeparator());
                fr.write(System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
            log.error("Error " + e.toString());
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
                log.error("Error " + e.toString());
            }

        }
        saveCheck = true;
    }

    @Override
    public void saveAs(){

        FileChooser choice = new FileChooser();
        choice.setTitle("Сохранить файл");
        choice.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt"));
        choice.setInitialDirectory(new File("./src/edu/java/lab2/data"));
        File newFile = choice.showSaveDialog(null);
        if(newFile != null) {
            file = newFile;
            save();
            saveCheck = true;
        } else saveCheck = false;

    }

    @Override
    public void add(Automobile automobile) {
        dataList.add(automobile);
    }

    @Override
    public void delete(Automobile automobile) {
        dataList.remove(automobile);
    }

    @Override
    public void deleteAll(){
        dataList.clear();
    }

}
