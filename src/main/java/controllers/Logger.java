package controllers;

import controllers.dao.CreepyGuyDAO;
import controllers.dao.LoginAccesDAO;
import views.View;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class Logger {
    private View view;
    private String dbPass;
    private String dbUser;
    private Connection connection;
    private boolean userInDatabase;

    public Logger() {
        view = new View();
        dbPass = view.getInputString("DB Pass?");
        dbUser = view.getInputString("DB User?");
        connection = new Connector().connect(dbUser, dbPass);
        userInDatabase = false;
    }

    public void logIn(){
        UserController userController = logUser();
        if (!userInDatabase && userController == null) {
            view.print("User with that email does not exist");
        }
        else if (userController != null) {
            userController.startUserSession();
        }
    }

    private UserController logUser(){
        String email = view.getInputString("Enter email: ");
        String password = view.getInputString("Enter Password");
        List<Integer> loginData = new LoginAccesDAO(connection).readLoginData(email, password);
        int accessLevel = loginData.get(0);
        int id = loginData.get(1);
        return createUserController(accessLevel, id);
    }

    private UserController createUserController(int acessLevel, int id){
        if (acessLevel == 1){
            CodecoolerDao codecoolerDao = new CodecoolerDao(connection);
            return new CodecoolerController(id, codecoolerDao);
        }
        else if (acessLevel == 2){
            MentorDao mentorDao = new MentorDao(connection);
            return new MentorController(id, mentorDao);
        }
        else if (acessLevel == 3){
            CreepyGuyDAO creepyGuyDAO = new CreepyGuyDAO(connection);
            return new CreepyGuyController(id, creepyGuyDAO);
        }
        return null;
    }
}
