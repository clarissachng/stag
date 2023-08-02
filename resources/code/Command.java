import java.util.*;

public class Command {

    GameState state;
    Player player;
    private final String playerLocationName;

    public Command(GameState state, Player player) {
        this.state = state;
        this.player = player;
        // need to get the location -> for the game to know what things to retrieve
        this.playerLocationName = player.getPlayerLocation();
    }

    //--------------------------- COMMAND HANDLING ---------------------------//
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

    public String processCommand(String command){
        String output = "";
        ArrayList<String> splitMessage = splitCommand(command);

        if (checkIsBasicCommand(splitMessage)) {
//            System.out.println("Is Basic Command");
            for (String s : splitMessage) {
                switch(s) {
                    case "inventory" -> {
                        output = "inventory";
//                        System.out.println(output);
                        return output;
                    }
                    case "inv" -> {
                        output = "inv";
//                        System.out.println(output);
                        return output;
                    }
                    case "get" -> {
                        output = "get";
//                        System.out.println(output);
                        return output;
                    }
                    case "goto" -> {
                        output = "goto";
//                        System.out.println(output);
                        return output;
                    }
                    case "drop" -> {
                        output = "drop";
//                        System.out.println(output);
                        return  output;
                    }
                    case "look" -> {
                        output = "look";
//                        System.out.println(output);
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
//                System.out.println("Going to invCommand");
                return invCommand(player);
            }
            case "get" -> {
//                System.out.println("Going to getCommand");
                return getCommand(player);
            }
            case "drop" -> {
//                System.out.println("Going to dropCommand");
                return dropCommand(player);
            }
            case "goto" -> {
//                System.out.println("Going to gotoCommand");
                return gotoCommand(player);
            }
            case "look" -> {
//                System.out.println("Going to lookCommand");
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

    public Artefact getArtefactFromCurrLocation() {
        return null;
    }
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
        System.out.println("reached inv command");

        // get player's inventory
        ArrayList<Artefact> invArtefacts = player.getPlayerInv();
        StringBuilder invOutput = new StringBuilder();
        invOutput.append("You have these items in inventory: \n");

        // print out player's inventory
        for (Artefact artefact: invArtefacts) {
            invOutput.append(artefact.getDescription());
            invOutput.append("\n");
        }
        System.out.println(invOutput);
        return invOutput.toString();
    }

    public String getCommand(Player player) {
        System.out.println("reached get command");
        // get current player location
        Location currentLoc = getCurrLocation();
        StringBuilder getOutput = new StringBuilder();

        // check if entity exists in location
        // remove artefact from location
        // add artefact in player inv
        for(Artefact artefact: currentLoc.getArtefact()) {
            String artefactName = String.valueOf(artefact);
            if(currentLoc.checkIfArtefactExist(artefactName)) {
                Artefact getArtefact = currentLoc.getArtefactByName(artefactName);
                currentLoc.removeArtefacts(getArtefact);
                player.addArtefactToPlayerInv(getArtefact);
                getOutput.append("You picked up a ");
                getOutput.append(artefactName);
                System.out.println(getOutput);
                return getOutput.toString();
            }
        }

        // error handling: if artefact doesnt exist, print error
        // TO DO!
        System.out.println("Failed to find the artefact in current location\n");
        return null;
    }

    public String dropCommand(Player player) {
        System.out.println("reached drop command");
        Location currentLoc = getCurrLocation();
        StringBuilder dropOutput = new StringBuilder();

        // check player's inv
        for(Artefact artefact: player.getPlayerInv()) {
            String artefactName = String.valueOf(artefact);
            if (player.checkArtefactInInv(artefactName)) {
                Artefact dropArtefact = player.getArtefactFromInv(artefactName);
                currentLoc.addArtefact(dropArtefact);
                player.dropArtefactFromPlayerInv(dropArtefact);
                dropOutput.append("You dropped a");
                dropOutput.append(artefactName);
            }
        }
        System.out.println(dropOutput);
        System.out.println("Failed to find the artefact in player's inventory\n");
        return dropOutput.toString();
    }

    public String gotoCommand(Player player) {
        System.out.println("reached goto command");
        Location currentLoc = getCurrLocation();
        StringBuilder gotoOutput = new StringBuilder();
        String locationName = currentLoc.getName();

        if(currentLoc.checkIfPathsExist(locationName)) {
            player.setPlayerLocation(locationName);
            Location newLocation = getNewLocation(locationName);
            gotoOutput.append("You are in ");
            gotoOutput.append(newLocation.getDescription()).append(". ");
            gotoOutput.append("You can see:");
            gotoOutput.append("\n");
            showAllEntity(newLocation, gotoOutput);

        }
        System.out.println(gotoOutput);
        System.out.println("Failed to location new location in path\n");
        return gotoOutput.toString();
    }

    public String lookCommand() {
        System.out.println("reached look command");
        // get player location -> get entities from location
        Location currLocation = getCurrLocation(); // use updated
        StringBuilder lookOutput = new StringBuilder();

        lookOutput.append("You are in ");
        lookOutput.append(currLocation.getDescription()).append(". ");
        lookOutput.append("You can see: ");
        showAllEntity(currLocation, lookOutput);

        System.out.println("Failed to execute look command\n");
        return lookOutput.toString();
    }
}
