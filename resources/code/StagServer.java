import com.alexmerz.graphviz.ParseException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.*;
import java.nio.Buffer;
import java.nio.file.Paths;
import java.util.ArrayList;

class  StagServer
{
    Player thePlayer;

    GameState state = new GameState();
    public static void main(String[] args)
    {

//        if(args.length != 2) System.out.println("Usage: java StagServer <entity-file> <action-file>");
//        else new StagServer(args[0], args[1], 8888);
        File entityFilename = Paths.get("data/basic-entities.dot").toAbsolutePath().toFile();
        File actionFilename = Paths.get("data/basic-actions.json").toAbsolutePath().toFile();
//
        new StagServer(entityFilename, actionFilename, 8888);
    }

    public StagServer(File entityFilename, File actionFilename, int portNumber)
    {
        this.thePlayer = new Player();
        GameParser parser = new GameParser(entityFilename, actionFilename, state);
        try {

            parser.parseAction();
            parser.parseEntity();

            // read entityFilename --> parse entity
            ServerSocket ss = new ServerSocket(portNumber);
            System.out.println("Server Listening");
            while(true) acceptNextConnection(ss);
        } catch(IOException ioe) {
            System.err.println(ioe);
        } catch (ParseException | ParserConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private void acceptNextConnection(ServerSocket ss)
    {
        try {
            // Next line will block until a connection is received
            Socket socket = ss.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            processNextCommand(in);
            out.close();
            in.close();
            socket.close();
        } catch(IOException ioe) {
            System.err.println(ioe);
        }
    }

    // probably can put this in tokenizer
    public String getPlayerName(String command) {
        int index = 0;
        String playerName = "";
        for(int i =0; i < command.length(); i++) {
            if(command.charAt(i) == ':') {
                index = i;
                break;
            }
        }

        playerName = command.substring(0, index);

        // test!
//        System.out.println(playerName);

        return playerName;
    }

    private String processNextCommand(BufferedReader in) throws IOException
    {

        String input = in.readLine();
        String returnStatement = "";

        if (input.isEmpty()) {
            System.out.println("Command is empty!");
        }

        input = input.toLowerCase();
        try {
            Player player;
            String playerName = getPlayerName(input);

            // if the player is a new player, add the details for the game to start
//            if (state.playerExist(playerName)) {
                String firstLoc = state.getFirstLoc();
                player = new Player(playerName, firstLoc); // out of scope
                player.setPlayerLocation(firstLoc);
                state.addPlayerIntoMap(playerName);
                state.setCurrentPlayer(playerName);
//            } else {
//                player = state.getCurrentPlayer(playerName);
//                state.setPlayerHashMap(playerName, player);

            // why cannot retrieve location after the else statement ;;
//            }

            Command command = new Command(state, player);
            returnStatement = command.basicCommand(input, player);
        // if there is no match then look for action trigger words

    } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return returnStatement;
    }
}
