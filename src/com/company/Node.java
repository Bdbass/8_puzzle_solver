package com.company;

import javafx.util.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class Node {

    // manage the current state
    private ArrayList<Integer> state;
    private Pair<Node, Action> parent;
    // manage the heuristic functions
    private int hDisplaced;
    private int hManhattan;
    private int gVal;
    private int f;
    private int h;
    private int count;
    private int children;


    //default used for testing
    public Node(ArrayList<Integer> start){
        this.state = start;
    }

    // Constructor of start node
    public Node(ArrayList<Integer> start, Heuristic h, int[] GOAL, HashMap<Integer, Pair<Integer, Integer>> GOALMAP) {
        this.state = start;
        this.parent = null;
        this.sethManhattan(GOALMAP);
        this.sethDisplaced(GOAL);
        this.gVal = 0;
        this.setf(h);
        this.setCount(0);
    }

    //Constructor of all other nodes
    public Node(Pair<Node, Action> parent, Heuristic h, int[] GOAL, HashMap<Integer,
            Pair<Integer, Integer>> GOALMAP, int count) {
        // bad path
        if (!this.setState(parent)) {
            this.state = null;
            return;
        }
        this.setParent(parent);
        this.sethDisplaced(GOAL);
        this.sethManhattan(GOALMAP);
        this.setgVal();
        this.setf(h);
        this.setCount(count);
    }

    // Setters and Getters
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getChildren(){
        return this.children;
    }

    // returns the current state arrayList
    public ArrayList<Integer> getState() {
        return state;
    }

    //sets the state based off the parent node and action
    public boolean setState(Pair<Node, Action> parent) {
        ArrayList<Integer> oldState = new ArrayList<>(parent.getKey().getState());
        int spaceLocation = oldState.indexOf(0);

        switch (parent.getValue()) {
            case DOWN:
                if (spaceLocation > 5) return false;
                swap(oldState, spaceLocation, spaceLocation + 3);
                break;
            case UP:
                if (spaceLocation < 3) return false;
                swap(oldState, spaceLocation, spaceLocation - 3);
                break;
            case LEFT:
                if (spaceLocation % 3 == 0) return false;
                swap(oldState, spaceLocation, spaceLocation -1);
                break;
            case RIGHT:
                if (spaceLocation % 3 == 2) return false;
                swap(oldState, spaceLocation, spaceLocation +1);
                break;
            default:
                return false;
        }
        this.state = oldState;
        return true;
    }

    // returns the parent
    public Pair<Node, Action> getParent() {
        return parent;
    }

    //sets the parent
    public void setParent(Pair<Node, Action> parent) {
        this.parent = parent;
    }

    // returns hDisplaced
    public int gethDisplaced() {
        return hDisplaced;
    }

    // sets hDisplaced, doesn't include space
    public void sethDisplaced(int[] GOAL) {
        this.hDisplaced = 0;
        for (int i = 0; i < GOAL.length; i++) {
            if (i != 4 && GOAL[i] != state.get(i)) this.hDisplaced += 1;
        }
    }

    //returns manhattan distance
    public int gethManhattan() {
        return hManhattan;
    }

    // sets the manhattan distance, based on all blocks except 0
    public void sethManhattan(HashMap<Integer, Pair<Integer, Integer>> GOALMAP) {
        int count = 0;
        int current_num;
        this.hManhattan = 0;

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                current_num = this.getState().get(count);
                if (current_num != 0) {
                    this.hManhattan += Math.abs(GOALMAP.get(current_num).getKey() - row)
                            + Math.abs(GOALMAP.get(current_num).getValue() - col);
                }
                count++;
            }
        }
    }

    // return the g(x) where g(x) = # of tiles moved
    public int getgVal() {
        return gVal;
    }

    // sets the gVal to 1+parent gVal
    public void setgVal() {
        this.gVal = this.getParent().getKey().getgVal()+1;
    }

    // gets f1
    public int getf() {
        return f;
    }

    //returns h
    public int geth(){
        return h;
    }

    // sets f1 to hDisplaced
    public void setf(Heuristic h) {
        switch (h) {
            case DIS:
                this.h = this.gethDisplaced();
                break;
            case MAN:
                this.h = this.gethManhattan();
                break;
            case BOTH:
                this.h = this.gethDisplaced()+ this.gethManhattan();
                break;
        }
        this.f = this.h+this.getgVal();
    }

    // swaps two blocks
    public void swap(ArrayList<Integer> state, int index_a, int index_b) {
        int a = state.get(index_a);
        state.set(index_a, state.get(index_b));
        state.set(index_b, a);
    }

    // expands the node, to find its children
    public ArrayList<Node> expand(Heuristic h, int[] GOAL, HashMap<Integer, Pair<Integer, Integer>> GOALMAP, int count) {
        Node tempNode;
        ArrayList<Node> children = new ArrayList<>();
        for (Action a: Action.values()) {
            tempNode = new Node(new Pair<>(this, a), h, GOAL, GOALMAP, count);
            if (tempNode.getState() != null) {
                children.add(tempNode);
            }
        }
        this.children = children.size();
        return children;
    }

    // prints the board, given the current state
    public void printBoard() {
        int count = 0;
        String s = "";
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                s += this.getState().get(count) + " ";
                count++;
            }
            s += "\n";
        }
        System.out.println(s);
    }

    public static void main(String args[]){
        (new Node(new ArrayList<>(Arrays.asList(2,3,4,1,8,0,7,6,5)))).printBoard();
    }
}
