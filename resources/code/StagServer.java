import com.alexmerz.graphviz.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.nio.file.Paths;
import java.util.ArrayList;

class  StagServer {
    Player thePlayer;
    String firstLocation;
    GameState state = new GameState();

    public static void main(String[] args) {

//        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
//        else new StagServer(args[0], args[1], 8888);
        File entityFilename = Paths.get("data/basic-entities.dot").toAbsolutePath().toFile();
        File actionFilename = Paths.get("data/basic-actions.json").toAbsolutePath().toFile();
//
        new StagServer(entityFilename, actionFilename, 8888);
    }

    public StagServer(File entityFilename, File actionFilename, int portNumber) {
        GameParser parser = new GameParser(entityFilename, actionFilename, state);
        try {
            parser.parseAction();
            parser.parseEntity();

            // read entityFilename --> parse entity
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while (true) acceptNextConnection(ss);
        } catch (IOException ioe) {
            System.err.println(ioe);
        } catch (ParseException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void acceptNextConnection(ServerSocket ss) {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            String incomingCommand = in.readLine();
            String result = processNextCommand(incomingCommand);
            out.write(result);
            out.close();
            in.close();
            socket.close();
        } catch (IOException ioe) {
            System.err.println(ioe);
        }
    }

    // probably can put this in tokenizer
    public String getPlayerName(String command) {
        int index = 0;
        String playerName = "";
        for (int i = 0; i < command.length(); i++) {
            if (command.charAt(i) == ':') {
                index = i;
                break;
            }
        }

        playerName = command.substring(0, index);

        // test!
//        System.out.println(playerName);

        return playerName;
    }

    private String processNextCommand(String input) throws IOException
    {
        String returnStatement;
        input = input.toLowerCase();

        if (input.isEmpty()) {
            System.out.println("Command is empty!");
        }

        try {
            String playerName = getPlayerName(input);

            // if the player is a new player, add the details for the game to start
            if (!state.playerExist(playerName)) {
            this.firstLocation = state.getFirstLoc();
            this.thePlayer = new Player(playerName, firstLocation);

            thePlayer.setPlayerLocation(firstLocation);
            state.addPlayerIntoMap(playerName);
            state.setCurrentPlayer(thePlayer);
            } else {
                thePlayer = state.getCurrentPlayer();
                state.setPlayerHashMap(playerName, thePlayer);

//            // why cannot retrieve location after the else statement ;;
            }

            Command command = new Command(state, thePlayer);
            returnStatement = command.basicCommand(input, thePlayer);
            // if there is no match then look for action trigger words

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnStatement;
    }
}