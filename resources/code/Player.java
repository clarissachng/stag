import java.util.ArrayList;
public class Player extends Entity{

    private ArrayList<Artefact> inventory = new ArrayList<>();
    private String location;
    public Player(String name, String firstLocation) {
        super(name, firstLocation);
        this.location = firstLocation;
    }

    //--------------------------- INVENTORY  ---------------------------//
    // inventory: need to get, add artefact and remove artefact
    public ArrayList<Artefact> getPlayerInv(){
        return inventory;
    }

    public void addArtefactToPlayerInv(Artefact artefact){
        inventory.add(artefact);
    }

    public void dropArtefactFromPlayerInv(Artefact artefact){
        inventory.remove(artefact);
    }
    public Artefact getArtefactFromInv(String artefactName) {
        for (Artefact artefact: inventory) {
            if(artefact.getName().equalsIgnoreCase(artefactName)) {
                return artefact;
            }
        }
        return null;
    }

    public boolean checkArtefactInInv(String artefactName) {
        for (Artefact artefact: inventory) {
            return artefact.getName().equalsIgnoreCase(artefactName);
        }
        return false;
    }
    //--------------------------- LOCATION  ---------------------------//
    // set the location for the player and get the location for player
    // need to get cause need to know what is the player at
    // -> so can access to the entities in the location
    public void setPlayerLocation(String locationName){
        location = locationName;
    }

    public String getPlayerLocation() {
        return location;
    }
}
