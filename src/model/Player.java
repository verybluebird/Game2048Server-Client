package model;


import controller.Controller;

public class Player extends Model {
    public Controller controller = new Controller(this);
    private final String Player_Name;

    public Player(String Player_Name_in) {
        Player_Name = Player_Name_in;
    }

    public String get_Player_Name() {
        return Player_Name;
    }

    public int get_player_score() {
        return score;
    }
}
