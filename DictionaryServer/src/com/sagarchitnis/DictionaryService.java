package com.sagarchitnis;

/*
Author: Sagar A. Chitnis
email-id: chitnis.sagar@gmail.com
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import javax.net.ServerSocketFactory;
import javax.swing.*;

public class DictionaryService extends JFrame {

    // Identifies the user number connected
    private static int clientCounter = 0;
    public static int portNumber;
    public static String path;

    public static void main(String[] args) throws IOException {

        if (args[0].matches("[0-9]+")) {
            portNumber = Integer.parseInt(args[0]);
        } else {
            showCommandLineError();
        }

        //Create Single Instance of the Dictionary
        SingleDict dict = SingleDict.getInstance();

        //Parse CSV File and Update Dictionary
        dict.csvListToDict(Files.readAllLines(Paths.get("./data/dict.csv")));

        ServerSocketFactory socket_factory = ServerSocketFactory.getDefault();

        try (ServerSocket server = socket_factory.createServerSocket(portNumber)) {
            System.out.println("Waiting for client connection-");

            // Wait for connections
            while (true) {
                Socket client = server.accept();
                clientCounter++;
                System.out.println("Client " + clientCounter + ": Applying for connection!");

                // Start a new thread for a every new connection
                Thread clientThread = new Thread(() -> serveClient(client, dict));
                clientThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showCommandLineError() {
        JOptionPane.showMessageDialog(null, "Please check the connection input parameters on command line");
        System.exit(0);
    }

    private static void serveClient(Socket client, SingleDict dict) {
        try (Socket clientSocket = client) {

            String clientCommand = "NONE";
            DataInputStream dataIPStream = new DataInputStream(clientSocket.getInputStream());
            DataOutputStream dataOPStream = new DataOutputStream(clientSocket.getOutputStream());

            while (!clientCommand.matches("QUIT")) {

                String delimiter = ";";
                String clientMessage[] = dataIPStream.readUTF().split(delimiter);
                clientCommand = clientMessage[0].trim();
                String word = clientMessage[1].trim();
                String wordMeaning = clientMessage[2].trim();

                /*
                Using switch over if-statements as switch statements are faster to compute
                if-statements compile the entire expression whereas switch only compares values
                 */
                switch (clientCommand){
                    case ("QUERY"):
                        System.out.println("Client " + clientCounter + ": " + "Meaning for: " + word);
                        if (dict.queryWord(word.trim()) == null) {
                            dataOPStream.writeUTF("Word not Present. Try adding it");
                        } else {
                            String result = dict.queryWord(word.trim());
                            dataOPStream.writeUTF(result);
                            System.out.println("Successfully Sent Meaning: " + result);
                        }
                        break;

                    case ("ADD"):
                        System.out.println("Client " + clientCounter + ": " + "Request to add Word: " + word);
                        if (dict.addWord(word.trim(), wordMeaning.trim())) {
                            String msg = "Successfully Added Word: " + word + " & Meaning: " + wordMeaning;
                            dataOPStream.writeUTF(msg);
                            System.out.println(msg);
                        } else {
                            String msg = "Word: " + word + " already Present, hence Not Updated";
                            dataOPStream.writeUTF(msg);
                            System.out.println(msg);
                        }
                        break;

                    case ("DELETE"):
                        System.out.println("Client " + clientCounter + ": " + "Requested Delete for Word: " + word);
                        if (dict.queryWord(word.trim()) == null) {
                            dataOPStream.writeUTF("Word not Present to Delete");
                        } else {
                            dict.deleteWord(word.trim());
                            dataOPStream.writeUTF("Successfully deleted Word: " + word);
                            System.out.println("Successfully deleted Word: " + word);
                        }
                        break;
                }
            }
            System.out.println("Client " + clientCounter + ": Exited Application");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
