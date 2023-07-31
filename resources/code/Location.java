import java.util.ArrayList;

public class Location extends Entity{

    // initialise the things to be stored:
    // furniture, artefacts,
    ArrayList<Furniture> furnitureList = new ArrayList<>();
    // furniture only need get (no remove and add -> they only exist in the location)
    ArrayList<Artefact> artefactList = new ArrayList<>();
    ArrayList<Character> characterList = new ArrayList<>();
    ArrayList<String> paths = new ArrayList<>();
    // start -> forest
    // cellar -> start

    public Location(String name, String description) {
        super(name, description);
    }

    //--------------------------- GETTERS ---------------------------//

    public ArrayList<Furniture> getFurniture() {
        return furnitureList;
    }

    public ArrayList<Artefact> getArtefact() {
        return artefactList;
    }

    public Artefact getArtefactByName(String artefactName) {
        for (Artefact artefact : artefactList) {
            if (artefact.getName().equalsIgnoreCase(artefactName)) {
                return artefact;
            }
        }
        return null;
    }

    public ArrayList<Character> getCharacter() {
        return characterList;
    }

    public ArrayList<String> getPaths() {
        return paths;
    }

    //--------------------------- ADDER ---------------------------//

    public void addPaths(String locName) {
        paths.add(locName);
    }

    public void addArtefact(Artefact artefact) {
        artefactList.add(artefact);
    }

    public void addFurniture(Furniture furniture) {
        furnitureList.add(furniture);
    }

    public void addCharacter(Character character) {
        characterList.add(character);
    }

    //--------------------------- REMOVE ---------------------------//

    public void removeArtefacts(Artefact artefact) {
        artefactList.remove(artefact);
    }

    // probably dont need cause need to be able to move back to the same place again
//    public void removePaths(String location) {
//        paths.remove(location);
//    }

    //--------------------------- CHECKS ---------------------------//
    public boolean checkIfPathsExist(String locationName) {
        for (String location : paths) {
            if (location.equalsIgnoreCase(locationName)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfArtefactExist(String entityName) {
        for (Artefact artefact: artefactList) {
            if (entityName.equalsIgnoreCase(artefact.getName())) {
                return true;
            }
        }
        return false;
    }
}
