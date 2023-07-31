import com.alexmerz.graphviz.*;
import com.alexmerz.graphviz.objects.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

public class GameParser {
    File entityFile;
    File actionFile;
    GameState state;

    // how to open the file
    // read the dot files in and store them in some kind of DS
    // can put this in any DS
    // for each location --> got string call desc --> set/list of artifact, furniture, players

    // eof --> set up the paths
    // list of ref to other location (ie: start --> forest)
    public GameParser(File entityFile, File actionFile, GameState state) {
        this.entityFile = entityFile;
        this.actionFile = actionFile;
        this.state = state;
    }

    //--------------------------- ENTITY FILE ---------------------------//
    public void parseEntity() throws ParseException, FileNotFoundException {
        Parser entityParser = new Parser();
        FileReader entityReader = new FileReader(entityFile);
        entityParser.parse(entityReader);

        // getting the whole graph from entity file
        ArrayList<Graph> graphs = entityParser.getGraphs();
        // getting two sections of the whole document (Location, Paths)
        ArrayList<Graph> subGraphs = graphs.get(0).getSubgraphs();
        // get the location section of the graph
        ArrayList<Graph> location = subGraphs.get(0).getSubgraphs();

        // test
//        for (Graph g: subGraphs) {
//            System.out.printf(g.getId().getId());
//            System.out.println("\n");
//        }
////
////        // test if can get the locations
//        for (Graph l: location) {
//            System.out.printf(l.getId().getId());
//            System.out.println("\n");
//        }

        // get the first location
        Graph firstLoc = location.get(0);
        ArrayList<Node> firstLocNodes = firstLoc.getNodes(false);
        String firstLocName = firstLocNodes.get(0).getId().getId();
        state.setFirstloc(firstLocName);

        // loop through the sub-graph clusters in location
        for (Graph subgraphClusters : location) {
            ArrayList<Node> locNodeList = subgraphClusters.getNodes(false);

            // adding the location into the game
            Node locNode = locNodeList.get(0);
            String locName = locNode.getId().getId();
            String locDescription = locNode.getAttribute("description");
            Location newLoc = new Location(locName, locDescription);
            state.addLocation(locName,newLoc);

            // List of clusters in location
            ArrayList<Graph> clusters = subgraphClusters.getSubgraphs();

            // loop through each entity in cluster
            for (Graph subgraphEntities : clusters) {
                ArrayList<Node> entityNodes = subgraphEntities.getNodes(false);
                String entity = subgraphEntities.getId().getId();

                for (Node node : entityNodes) {
                    String name = node.getId().getId();
                    String description = node.getAttribute("description");

                    switch (entity) {
                        case "artefacts" -> addArtefact(newLoc, name, description);
                        case "furniture" -> addFurniture(newLoc, name, description);
                        case "characters" -> addCharacter(newLoc, name, description);
                        // error handling
                        default -> System.out.println("Invalid entity " + entity);
                    }
                }
            }
        }
        Graph graphPath = subGraphs.get(1);
        parsePath(graphPath);
    }

    public void parsePath(Graph graphPath) {
        ArrayList<Edge> edges = graphPath.getEdges();
        for (Edge edge: edges) {
            String source = edge.getSource().getNode().getId().getId();
            String target = edge.getTarget().getNode().getId().getId();
            if (checkIfLocationExist(source) && checkIfLocationExist(target)) {
                state.getLocation(source).addPaths(target);
            }

            // test!
//            System.out.printf("Path from %s to %s\n", source, target);
        }
    }

    private boolean checkIfLocationExist(String locationName){
        return state.getLocationHashMap().containsKey(locationName);
    }

    public void addArtefact(Location location, String name, String description) {
        Artefact newArtefact = new Artefact(name, description);
        state.getLocation(location.getName()).addArtefact(newArtefact);
    }

    public void addFurniture(Location location, String name, String description) {
        Furniture newFurniture = new Furniture(name, description);
        state.getLocation(location.getName()).addFurniture(newFurniture);
    }

    public void addCharacter(Location location, String name, String description) {
        Character newCharacter = new Character(name, description);
        state.getLocation(location.getName()).addCharacter(newCharacter);
    }

    public void parseAction() throws ParserConfigurationException, FileNotFoundException {
        try {
            JSONParser actionParser = new JSONParser();
            FileReader actionReader = new FileReader(actionFile);
            JSONObject object = (JSONObject) actionParser.parse(actionReader);

            var getAction = object.get("actions");
            JSONArray actionsArray = (JSONArray) getAction;

            for (var action :actionsArray) {
                Action newAction = new Action();

                // loop through action array to get each array and store them into action
                JSONArray subjects = (JSONArray) ((JSONObject)action).get("subjects");
                parseSubject(subjects, newAction);

                JSONArray consumed = (JSONArray) ((JSONObject)action).get("consumed");
                parseConsumed(consumed, newAction);

                JSONArray produced = (JSONArray) ((JSONObject)action).get("produced");
                parseProduced(produced, newAction);

                JSONArray triggers = (JSONArray) ((JSONObject)action).get("triggers");
                parseTriggers(triggers, newAction, state);

                String narration = (String)((JSONObject)action).get("narration");

//                for (var w : consumed)
//                    System.out.println((String) w);
//                for (var w : triggers)
//                    System.out.println((String) w);
//                for (var w : subjects)
//                    System.out.println((String) w);
//                for (var w : produced)
//                    System.out.println((String) w);
//
//                System.out.println(narration);
            }
        } catch (IOException | org.json.simple.parser.ParseException e) {
            throw new RuntimeException(e);
        }
    }

    // parse subject function
    // loop through subject list
    // add the subject into new action

    public void parseSubject(JSONArray subjectList, Action newAction) {
        for (Object subject: subjectList) {
            newAction.addSubjects(String.valueOf(subject));
        }
    }
    public void parseConsumed(JSONArray consumedList, Action newAction) {
        for (Object consumed: consumedList) {
            newAction.addConsumed(String.valueOf(consumed));
        }
    }
    public void parseProduced(JSONArray producedList, Action newAction) {
        for (Object produced: producedList) {
            newAction.addProduced(String.valueOf(produced));
        }
    }
    public void parseTriggers(JSONArray triggerList, Action newAction, GameState state) {
        for (Object trigger: triggerList) {
            state.addTriggerIntoList(String.valueOf(trigger));
            if(state.getTreeActionMap().containsKey(String.valueOf(trigger))) {
                state.getTreeActionMap().get(String.valueOf(trigger)).add(newAction);
            } else {
                HashSet<Action> action = new HashSet<>();
                state.addTriggerIntoList(String.valueOf(trigger));
                action.add(newAction);
                state.addActionIntoMap(String.valueOf(trigger), action);
            }
        }
    }
}
