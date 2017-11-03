package com.company;


public class Edge {

    Object alphabet; //contains the "weight" of the edge, ie the alphabet associated with it
    Node nextState; //contains the next state that the edge is connected to

    public Edge(Object o, Node n){
        alphabet = o;
        nextState = n;
    }

    public Node getNextState(){
        return nextState;
    }

    public Object getAlphabet(){
        return alphabet;
    }

    public void setNextState(Node n){
        nextState = n;
    }

    public void setAlphabet(Object o){
        alphabet = o;
    }


}
