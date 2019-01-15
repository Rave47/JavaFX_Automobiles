package edu.java.lab2.interfaces;

import edu.java.lab2.objects.User;

public interface IUserList {

    void add(User user);
    void delete(User user);

    void save();
}
