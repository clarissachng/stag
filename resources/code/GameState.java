import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
public class GameState {
    private String firstLoc;
    private HashMap<String, Location> locationHashMap = new HashMap<>();
    private HashMap<String, Player> playerHashMap  = new HashMap<>();
    private HashMap<String, HashSet<Action>> actions = new HashMap<>();
    private ArrayList<String> triggerWordList = new ArrayList<>();
    private Player currentPlayer;

    //--------------------------- LOCATION  ---------------------------//
    // adding location into the hashmap
    // retrieve location from hashmap -> also used for checking to see if the location exist
    // get and set firstloc for the player + game

    public void addLocation(String name, Location location) {
        locationHashMap.put(name, location);
    }

    public Location getLocation(String name) {
        return locationHashMap.get(name);
    }

    public String getFirstLoc() {
        return firstLoc;
    }

    public void setFirstloc(String name) {
        this.firstLoc = name;
    }

    public HashMap<String, Location> getLocationHashMap() {
        return locationHashMap;
    }

    //--------------------------- PLAYER  ---------------------------//
    // set and get player from hashmap -> for the game setup
    // need to call Player class -> player is null!!

    public void setCurrentPlayer(Player playerName) {
        this.currentPlayer = playerName;
    }

    // if the player alr exist, just get the player
    // from the hashmap itself and continue with the game
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    // check if the player is a new player or alr exist in the game
    public boolean playerExist(String playerName) {
        return playerHashMap.containsKey(playerName);
    }

    // if the player is new, set the playerHashMap
    public void setPlayerHashMap(String playerName,  Player player) {
        this.playerHashMap.put(playerName ,player);
    }

    // new player: add the player into the hashmap
    public void addPlayerIntoMap(String playerName) {
        Player newPlayer = new Player(playerName, firstLoc);
        playerHashMap.put(playerName, newPlayer);
    }

    //--------------------------- ACTION/TRIGGER ---------------------------//
    // mostly for trigger words and action
    // TO DO!!
//    public ArrayList<String> getTriggerWordList(){
//        return triggerWordList;
//    }

    // for game parser, add the trigger words into a list to excute action
    public void addTriggerIntoList(String triggerWords) {
        triggerWordList.add(triggerWords);
    }

    // to parse triggers in game parser add the new action into the action tree map
    public void addActionIntoMap(String trigger, HashSet<Action> action) {
        actions.put(trigger, action);
    }

    // is treemap for efficient?
    // maybe need to get the action by value
    public HashMap<String, HashSet<Action>> getTreeActionMap() {
        return actions;
    }
}
