package com.sagarchitnis;

/*
Author: Sagar A. Chitnis
email-id: chitnis.sagar@gmail.com
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;
import java.io.*;
import java.net.*;

public class QuickFinderDict {
    private JPanel dictBoredPanel;
    private JPanel buttonPanel;
    private JPanel displayPanel;
    private JButton queryWordButton;
    private JButton addWordButton;
    private JButton deleteWordButton;
    private JButton exitAppButton;
    private JTextField wordTextBox;
    private JTextField meaningTextBox;
    private JTextPane queryDisplay;
    private JLabel appName;
    private JLabel wordLabel;
    private JLabel meaningLabel;
    public static Socket clientSocket;
    public DataInputStream ipStream;
    public DataOutputStream opStream;
    public String errorMessage;
    public String command;
    public static String hostOrIP;
    public static Integer portNumber;


    public QuickFinderDict() {

        try {
            ipStream = new DataInputStream(clientSocket.getInputStream());
            opStream = new DataOutputStream(clientSocket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        QuickFinderDict qfd = this;
        qfd.errorMessage = " ";

        queryWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                qfd.errorMessage = qfd.setErrorMessage(actionEvent);

                if (qfd.errorMessage.isBlank()) {
                    try {
                        qfd.opStream.writeUTF("QUERY;" + wordTextBox.getText().trim() + "; ");
                        qfd.queryDisplay.setText(qfd.ipStream.readUTF());
                        qfd.meaningTextBox.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    qfd.queryDisplay.setText(qfd.errorMessage);
                }
            }
        });
        exitAppButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                try {
                    qfd.opStream.writeUTF("QUIT; ; ");
                    qfd.ipStream.close();
                    qfd.opStream.close();
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        addWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                qfd.errorMessage = qfd.setErrorMessage(actionEvent);

                if (qfd.errorMessage.isBlank()) {
                    try {
                        qfd.opStream.writeUTF("ADD;" + wordTextBox.getText().trim() + ";" + meaningTextBox.getText());
                        qfd.queryDisplay.setText(qfd.ipStream.readUTF());
                        qfd.meaningTextBox.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    qfd.queryDisplay.setText(qfd.errorMessage);
                }
            }
        });
        deleteWordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                qfd.errorMessage = qfd.setErrorMessage(actionEvent);

                if (qfd.errorMessage.isBlank()) {
                    try {
                        qfd.opStream.writeUTF("DELETE;" + wordTextBox.getText().trim() + "; ");
                        qfd.queryDisplay.setText(qfd.ipStream.readUTF());
                        qfd.meaningTextBox.setText("");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    qfd.queryDisplay.setText(qfd.errorMessage);
                    qfd.meaningTextBox.setText("");
                }
            }
        });
        wordTextBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                if (!(wordTextBox.getText().trim().matches("[A-Za-z]+"))) {
                    errorMessage = "Please check input for words only";
                    queryDisplay.setText(errorMessage);
                }
            }
        });
    }

    public String setErrorMessage(ActionEvent actionEvent) {

        boolean isWordARegexMatch = (this.wordTextBox.getText().trim().matches("[A-Za-z]+"));
        String clientAction = actionEvent.getActionCommand();

        if (clientAction.matches("Query Word") && !isWordARegexMatch) {
            return "Check Input: Only type a Word";
        }
        if (clientAction.matches("Delete Word") && (!isWordARegexMatch)) {
            return "Check Input: Only type a Word";
        }
        if (clientAction.matches("Add Word")) {
            if (this.wordTextBox.getText().isBlank()) {
                return "Check Input: Word to ADD is Empty";
            }
            if (!isWordARegexMatch) {
                return "Check Input: Only type a Word";
            }
            if (this.meaningTextBox.getText().isBlank()) {
                return "Meaning for Word left Blank";
            }
            if (!this.meaningTextBox.getText().trim().matches("[A-Za-z ]+")) {
                return "Input only words in your meaning";
            }
        }
        return " ";
    }

    public static void main(String[] args) throws IOException {

        if (args.length == 2 && (args[0].matches("localhost") || (args[0].matches("[0-9]{2,3}.[0-9]{2,3}.[0-9]{2,3}.[0-9]{2,3}"))) && (args[1].matches("[0-9]+"))) {
            hostOrIP = args[0];
            portNumber = Integer.parseInt(args[1]);
        } else {
            JOptionPane.showMessageDialog(null, "Please check the connection input parameters on command line");
            System.exit(0);
        }

        try {
            clientSocket = new Socket(hostOrIP, portNumber);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Please check the connection input parameters on command line");
            System.exit(0);
        }

        JFrame frame = new JFrame("DictionaryApp");
        QuickFinderDict dictDisplay = new QuickFinderDict();
        frame.setContentPane(dictDisplay.dictBoredPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

    }

}
