package view;

import entity.Tile;

import javax.swing.*;
import java.awt.*;

public class BoardView extends JPanel {
    public BoardView(Tile tile) {
        JLabel tileLabel;
        setLayout(new BorderLayout());
        ImageIcon img = new ImageIcon("src/resources/" + tile.value + ".png");
        tileLabel = new JLabel(img);
        //    private Tile tile;
        JPanel panel = new JPanel();
        panel.add(tileLabel);
    }

    public BoardView() {

    }
}
