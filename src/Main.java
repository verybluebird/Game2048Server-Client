import controller.Controller;
import model.Model;
import view.BoardView;

import javax.swing.*;
import java.awt.*;


public class Main {
    public static void main(String[] args) {
        Model model=new Model();
        Controller controller=new Controller(model);
        controller.getView().run();

    }
}
