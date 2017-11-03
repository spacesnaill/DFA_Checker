package com.company;

import java.util.ArrayList;
public class Node {
    Object state;
    ArrayList<Edge> edgeArray; //holds the edges connected to this node
    boolean isFinalState;

    public Node(Object o) {
        state = o;
        edgeArray = new ArrayList<Edge>();
        isFinalState = false;
    }

    public Object getState(){
        return state;
    }

    public Edge getEdge(int n){
        return edgeArray.get(n);
    }

    public ArrayList getEdgeArray(){
        return edgeArray;
    }

    public void setState(Object o){
        state = o;
    }

    //creates a new edge for the node
    public void addEdge(Object o, Node n){
        edgeArray.add(new Edge(o, n));
    }

    public void setFinalState(boolean bool){
        isFinalState = bool;
    }

    public boolean isThisFinalState(){
        return isFinalState;
    }

}
