package com.company;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.io.PrintWriter;
import java.util.Scanner;
import javax.swing.JOptionPane;


public class Main {

    private static ArrayList<Node> stateArray = new ArrayList<Node>();
    private static String startingState = null;
    private static Node startingStateNode = null;

    public static void main(String[] args) {
	// write your code here
//        Node testState1 = new Node("q1");
//        Node testState2 = new Node("q2");
//        testState1.addEdge("a", testState2);
//        testState1.addEdge("b", testState1);
//        Node statePointer = testState1;
//        String testSequence = "a";
        //Scanner keyboardInput = new Scanner(System.in);
        //System.out.println("Enter the file name for the DFA.");
        String fileName = JOptionPane.showInputDialog("Please enter the file name for the DFA.");
        //System.out.println("Thank you. Please wait.");
        parseDFA(fileName);
        JOptionPane.showMessageDialog(null, "DFA assembled.", "", JOptionPane.INFORMATION_MESSAGE);
        //System.out.println("");
        //System.out.println("Please enter the name for the input file.");
        fileName = JOptionPane.showInputDialog("Please enter the file name for the input strings.");
        //System.out.println("Thank you. Please enter a name for the output file.");
        String outputFile = JOptionPane.showInputDialog("Thank you. Please enter the file name for the output file.");

//        String fileName = "..//COSC485_P1_DFA.txt";
//
//        parseDFA(fileName);
//
//        fileName = "..//COSC485_P1_Strings.txt";

        //System.out.println(checkString("bbbbbbbbbb"));

        try{
            PrintWriter writer = new PrintWriter(outputFile, "UTF-8");
            try(BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                //ArrayList<String> parser = new ArrayList<String>();
                while ((line = br.readLine()) != null) {
                    if(line.length() == 0){
                        continue;
                    }
                    else if(checkString(line)){
                        writer.println(line + " is accepted.");
                    }
                    else{
                        writer.println(line + " is rejected.");
                    }
                    writer.println(" ");
                }
            }
            catch(IOException e){
                JOptionPane.showMessageDialog(null, e, "Sorry! An error occured", JOptionPane.ERROR_MESSAGE);
                System.exit(0);

            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e, "Sorry! An error occured", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }

        JOptionPane.showMessageDialog(null, "Operations complete. Have a nice day", "Finished", JOptionPane.INFORMATION_MESSAGE);
    }

    //putting the input strings through the DFA
    public static boolean checkString(String line) {

        //String line;
        Node nodePointer = null;

        nodePointer = startingStateNode;

        String[] stringArray = line.split("");
        for (int i = 0; i < stringArray.length; i++) {
            boolean matchFound = false;
            for (int j = 0; j < nodePointer.getEdgeArray().size(); j++) {
                if (nodePointer.getEdge(j).getAlphabet().equals(stringArray[i])) {
                    nodePointer = nodePointer.getEdge(j).getNextState();
                    matchFound = true;
                    break;
                } else {
                    continue;
                }
            }//end of nested for loop
            if (matchFound == false) {
                return matchFound;
            }
        }//end of outer for loop
        //if the node we land on is a final state, we accept this string if not we deny it
        if (nodePointer.isFinalState == true) {
            return true;
        } else {
            return false;
        }
    }

    //offset1 is the offset in the beginning, offset2 is the offset at the send
    public static String extractString(String s, char start, int offset1, int offset2){
        s = s.replace(" ", "");
        s = s.substring(s.indexOf(start) + offset1, s.length() + offset2);
        return s;
    }

    //parses the DFA file into something actually usable
    public static void parseDFA(String fileName){
        try(BufferedReader br = new BufferedReader(new FileReader(fileName))){
            String line;
            //ArrayList<String> parser = new ArrayList<String>();


            while ((line = br.readLine()) != null){
                //if line starts with M, skip it as it's not needed
                if(line.startsWith("M") == true){
                    continue;
                }
                //if line starts with States, fill out stateArray with Nodes made from the states
                else if(line.startsWith("States") == true){
//                    line = line.replace(" ", "");
//                    line = line.substring(line.indexOf('{') + 1, line.length() - 2);
                    line = extractString(line, '{', 1, -2);
                    String[] parser = line.split(",");
                    for(int i = 0; i < parser.length; i++){
                        stateArray.add(new Node(parser[i]));
                    }
                }//end of States
                //if line starts with Starting, this is the starting state and we need to remember this
                else if(line.startsWith("Starting") == true){
//                    line = line.replace(" ", "");
//                    line = line.substring(line.indexOf('=') + 1, line.length() - 1);
                    line = extractString(line, '=', 1, -1);
                    startingState = line;
                    for(int i = 0; i < stateArray.size(); i++){
                        if(stateArray.get(i).getState().equals(startingState)){
                            startingStateNode = stateArray.get(i);
                        }
                    }
                }//end of Starting State
                //if line starts with Final, these are the final states
                else if(line.startsWith("Final") == true){
//                    line = line.replace(" ", "");
//                    line = line.substring(line.indexOf('{') + 1, line.length() - 2);
                    line = extractString(line, '{', 1, -2);
                    String[] parser = line.split(",");
                    //go through each state, find the state in stateArray, and set isFinalState to true
                    for(int i = 0; i < parser.length; i++){
                        for(int j = 0; j < stateArray.size(); j++){
                            if(parser[i].equals((String)stateArray.get(j).getState())){
                                stateArray.get(j).setFinalState(true);
                            }
                        }
                    }
                }//end of Final state
                //if the line starts with Transition, these are the transition functions
                else if(line.startsWith("Transition")){
                    //it is multiple lines so we need a while loop
                    while(!((line = br.readLine().trim()).equals("}"))){
                        //these will keep track of the states we use for the edge
                        Node firstState = null;
                        Node secondState = null;

                        String transitionAlphabet = ""; //this will keep track of the alphabet for the transition


                        //we need to extract the necessary information from each line of the transition functions
                        line = line.trim();
                        if(line.endsWith(",")){
                            line = extractString(line, '(', 1, -2);
                        }
                        else{
                            line = extractString(line, '(', 1, -1);
                        }


                        String[] parser = line.split(",");

                        //checking for the start of the transition
                        for(int i = 0; i < stateArray.size(); i++){
                            if(stateArray.get(i).getState().equals(parser[0])){
                                firstState = stateArray.get(i);
                            }
                        }

                        //setting the alphabet for the tranisition
                        transitionAlphabet = parser[1];

                        //checking for the destination of the transition
                        for(int i = 0; i < stateArray.size(); i++ ){
                            if(stateArray.get(i).getState().equals(parser[2])){
                                secondState = stateArray.get(i);
                            }
                        }
                        //setting up the edge
                        firstState.addEdge(transitionAlphabet, secondState);
                    }
                }
            }
        }
        catch(IOException e){
            JOptionPane.showMessageDialog(null, e, "Sorry! An error occured", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }


}
