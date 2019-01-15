package edu.java.lab2.interfaces.impls;

import edu.java.lab2.interfaces.IUserList;
import edu.java.lab2.objects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;

public class UserList implements IUserList {

    private ObservableList<User> userList = FXCollections.observableArrayList();
    private static User currentUser;


    public ObservableList<User> getUserList() {
        return userList;
    }

    public User getCurrentUser() {
        return currentUser;
    }
    public void setCurrentUser(User crtUser) {
        currentUser = crtUser;
    }


    @Override
    public void add(User user){
        userList.add(user);
    }

    @Override
    public void delete(User user){
        userList.remove(user);
    }

    @Override
    public void save(){
        FileWriter fr = null;
        try {
            fr = new FileWriter("./src/edu/java/lab2/access/userList.txt");
            for (int i = 0; i < this.getUserList().size(); ++i) {
                fr.write(this.getUserList().get(i).getName() + System.lineSeparator());
                fr.write(this.getUserList().get(i).getLogin() + System.lineSeparator());
                fr.write(this.getUserList().get(i).getPassword() + System.lineSeparator());
                fr.write(this.getUserList().get(i).getRegDate() + System.lineSeparator());
                fr.write(this.getUserList().get(i).getAccessRight() + System.lineSeparator());
                fr.write(System.lineSeparator());
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
