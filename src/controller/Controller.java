package controller;

import model.Tile;
import model.Model;
import view.KeyView;
import view.View;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Controller extends KeyAdapter {
    private Model model;
    private View view;

    public Controller(Model model) {
        this.model = model;
        this.view = new View(this);
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }


    public View getView() {
        return view;
    }

    public Model getModel() {
        return model;
    }

}

