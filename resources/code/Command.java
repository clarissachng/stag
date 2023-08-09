import java.util.*;

public class Command {

    GameState state;
    Player player;
    String playerLocationName;

    public Command(GameState state, Player player) {
        this.state = state;
        this.player = player;
        // need to get the location -> for the game to know what things to retrieve
        this.playerLocationName = player.getPlayerLocation();
    }

    public boolean checkIsBasicCommand(ArrayList<String> splitMessage) {
        ArrayList<String> commandList = new ArrayList<>(Arrays.asList("inv", "inventory", "get", "drop", "goto", "look"));
        for (String s : splitMessage) {
            if (commandList.contains(s)) return true;
        }
        return false;
    }

    public ArrayList<String> splitCommand(String command) {
        command = command.toLowerCase();
        return new ArrayList<>(Arrays.asList(command.split(" ")));
    }

    public String getEntityName(String command) {
        ArrayList<String> split = splitCommand(command);
        return String.join(" ", split.subList(2, split.size()));
    }

    public String processCommand(String command){
        String output = "";
        ArrayList<String> splitMessage = splitCommand(command);

        if (checkIsBasicCommand(splitMessage)) {
            for (String s : splitMessage) {
                switch(s) {
                    case "inventory" -> {
                        output = "inventory";
                        return output;
                    }
                    case "inv" -> {
                        output = "inv";
                        return output;
                    }
                    case "get" -> {
                        output = "get";
                        return output;
                    }
                    case "goto" -> {
                        output = "goto";
                        return output;
                    }
                    case "drop" -> {
                        output = "drop";
                        return  output;
                    }
                    case "look" -> {
                        output = "look";
                        return output;
                    }
                }

            }
        }
        return output;
    }

    public String basicCommand(String command, Player player) {
        String triggerCommand = processCommand(command);

        switch (triggerCommand) {
            case "inv", "inventory" -> {
                return invCommand(player);
            }
            case "get" -> {
                return getCommand(player, command);
            }
            case "drop" -> {
                return dropCommand(player, command);
            }
            case "goto" -> {
                return gotoCommand(player, command);
            }
            case "look" -> {
                return lookCommand();
            }
            default -> {
                String errorMessage = "Invalid Command";
                System.out.println(errorMessage);
            }
        }
        return null;
    }

    private Location getCurrLocation() {
        return state.getLocation(playerLocationName);
    }

    private Location getNewLocation(String newLocName) {
        return state.getLocation(newLocName);
    }

    // get currloc
    // get artefacts from location

    private void showAllEntity(Location currentLoc, StringBuilder output) {
        ArrayList<Artefact> currLocArtefact = currentLoc.getArtefact();
        showCurrentArtefact(currLocArtefact, output);

        ArrayList<Furniture> currLocFurniture = currentLoc.getFurniture();
        showCurrentFurniture(currLocFurniture, output);

        ArrayList<Character> currLocCharacter = currentLoc.getCharacter();
        showCurrentCharacter(currLocCharacter, output);

        ArrayList<String> currLocPaths = currentLoc.getPaths();
        showCurrentPaths(currLocPaths, output);
    }

    private void showCurrentArtefact(ArrayList<Artefact> currLocArtefact, StringBuilder output) {
        for (Artefact artefact: currLocArtefact) {
            String artefactDescription = artefact.getDescription();
            output.append(artefactDescription);
            output.append("\n");
        }
    }

    private void showCurrentFurniture(ArrayList<Furniture> currLocFurniture, StringBuilder output) {
        for (Furniture furniture: currLocFurniture) {
            String furnitureDescription = furniture.getDescription();
            output.append(furnitureDescription);
            output.append("\n");
        }
    }

    private void showCurrentCharacter(ArrayList<Character> currLocCharacter, StringBuilder output) {
        for (Character character: currLocCharacter) {
            String characterDescription = character.getDescription();
            output.append(characterDescription);
            output.append("\n");
        }
    }

    private void showCurrentPaths(ArrayList<String> currLocPaths, StringBuilder output) {
        if(!currLocPaths.isEmpty()) {
            output.append("You can see from here:");
            output.append("\n");
            for (String paths: currLocPaths) {
                output.append(paths);
                output.append("\n");
            }
        }

    }

    public String invCommand(Player player) {

        // get player's inventory
        ArrayList<Artefact> invArtefacts = player.getPlayerInv();
        StringBuilder invOutput = new StringBuilder();

        if (!invArtefacts.isEmpty()) {
            invOutput.append("You have these items in inventory: \n");

            // print out player's inventory
            for (Artefact artefact: invArtefacts) {
                invOutput.append(artefact.getDescription());
                invOutput.append("\n");
            }
        }
        else {
            invOutput.append("Your inventory is empty :(\n");
        }
        return invOutput.toString();
    }

    public String getCommand(Player player, String command) {
        // get current player location
        Location currentLoc = getCurrLocation();
        StringBuilder getOutput = new StringBuilder();

        // check if entity exists in location
        // remove artefact from location
        // add artefact in player inv
        String artefactName = getEntityName(command);

        if(currentLoc.checkIfArtefactExist(artefactName)) {
            Artefact getArtefact = currentLoc.getArtefactByName(artefactName);
            currentLoc.removeArtefacts(getArtefact);
            player.addArtefactToPlayerInv(getArtefact);
            getOutput.append("You picked up a ");
            getOutput.append(artefactName);
        }
        else {
            getOutput.append("Failed to find the artefact in current location\n");
        }
        return getOutput.toString();
    }

    public String dropCommand(Player player, String command) {
        Location currentLoc = getCurrLocation();
        StringBuilder dropOutput = new StringBuilder();

        String artefactName = getEntityName(command);

        if(player.checkArtefactInInv(artefactName)) {
            Artefact dropArtefact = player.getArtefactFromInv(artefactName);
            currentLoc.addArtefact(dropArtefact);
            player.dropArtefactFromPlayerInv(dropArtefact);
            dropOutput.append("You dropped a ");
            dropOutput.append(artefactName);
        }
        else {
            dropOutput.append("Artefact is not in inventory\n");
        }

        return dropOutput.toString();
    }

    public String gotoCommand(Player player, String command) {
        Location currentLoc = getCurrLocation();
        StringBuilder gotoOutput = new StringBuilder();

        String locationName = getEntityName(command);

        if(currentLoc.checkIfPathsExist(locationName)) {
            player.setPlayerLocation(locationName);
            Location newLocation = getNewLocation(locationName);
            gotoOutput.append("You are in: \n");
            gotoOutput.append(newLocation.getDescription()).append("\n");
            gotoOutput.append("You can see: ");
            gotoOutput.append("\n");
            showAllEntity(newLocation, gotoOutput);
        }
        else {
            gotoOutput.append("Path does not exist\n");
        }

        return gotoOutput.toString();
    }

    public String lookCommand() {
        Location currLocation = getCurrLocation(); // use updated
        StringBuilder lookOutput = new StringBuilder();

        lookOutput.append("You are in: \n");
        lookOutput.append(currLocation.getDescription()).append("\n");
        lookOutput.append("You can see: \n");
        showAllEntity(currLocation, lookOutput);

        return lookOutput.toString();
    }
}
