package com.company;

import javafx.util.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class A_star {

    //enter file path here
    private static final String PATH = "/Users/brandonbass/IdeaProjects/8_puzzle_solver/src/";
    private static final int[] GOAL = {1, 2, 3, 8, 0, 4, 7, 6, 5};
    private static final HashMap<Integer, Pair<Integer, Integer>> GOALMAP =
            new HashMap<>(){
                {
                    put(1, new Pair<>(0, 0));
                    put(2, new Pair<>(0, 1));
                    put(3, new Pair<>(0, 2));
                    put(8, new Pair<>(1, 0));
                    put(0, new Pair<>(1, 1));
                    put(4, new Pair<>(1, 2));
                    put(7, new Pair<>(2, 0));
                    put(6, new Pair<>(2, 1));
                    put(5, new Pair<>(2, 2));
                }
            };

    //A* shortest path algorithm
    static Pair<Node, Double> AStar(ArrayList<Integer> start, Heuristic h) {

        //create pQ and visited arrayList, so we don't loop
        pQueue<Node> pq = new pQueue<Node>(0);
        ArrayList<ArrayList<Integer>> visited = new ArrayList<>();
        Node temp = new Node(start, h, GOAL, GOALMAP);
        int count = 1;
        int totalChildren = 0;

        //add start node to pq with priority of heuristic
        pq.enqueue(temp, temp.getf());

        //loop until pq is empty
        while(!pq.isEmpty()){
            //dequeue vertex w/ highest priority and set as visited
            temp = pq.dequeue();
            visited.add(temp.getState());

            // found goal state
            if(temp.gethDisplaced() == 0) return new Pair<>(temp, (double)totalChildren/(double)count);

            //loop through all unvisited neighbors of current
            for(Node n: temp.expand(h, GOAL, GOALMAP, count)){
                if (!isVisited(visited, n.getState())){
                    // enqueue if it hasn't been visited yet
                    totalChildren += 1;
                    enqueue(n, pq);
                }
            }
            // keep track of branching
            count += 1;
        }

        // not solvable (at least on this machine)
        return null;
    }

    // returns true if the node has been visited false, else
    static boolean isVisited(ArrayList<ArrayList<Integer>> visited,
                             ArrayList<Integer> currentState){
        for (ArrayList<Integer> state: visited){
            if (state.equals(currentState)) return true;
        }
        return false;
    }

    // enqueues the node for us, dealing with priority updates
    static void enqueue(Node n, pQueue<Node> pq){
        // check if node is already in pq, if so just update it
        for (Pair<Node, Integer> p: pq.getPq()){
            if (p != null && p.getKey().getState().equals(n.getState())){
                //update the f(n) if needed
                if (n.getf() < p.getKey().getf()){
                    pq.changePriority(p, n, n.getf());
                }
                return;
            }
        }
        // else add it
        pq.enqueue(n, n.getf());
    }

    // reads lines from file and returns an arrayList of start boards
    static ArrayList<ArrayList<Integer>> readLine(String textFile){
        String temp;
        String temp_array[];
        ArrayList<Integer> start;
        ArrayList<ArrayList<Integer>> inputdata = new ArrayList<>();

        try {
            Scanner s = new Scanner(new File(textFile));
            for (int i = 0; i < 15; i ++) {
                start = new ArrayList<>();
                temp = s.nextLine();
                temp_array = temp.split(",");
                for (String string : temp_array) {
                    start.add(Integer.parseInt(string));
                }
                inputdata.add(start);
            }
            return inputdata;
        }catch (Exception e){
            System.out.println(e.toString());
            return null;
        }
    }

    //prints the solution of a node for the heuristic
    public static void printSolution(Node n, int p_num, int set_num, int fileCount){
        try {
            String output = "";
            BufferedWriter writer =
                    new BufferedWriter(new FileWriter(PATH +"OutfileHeuristic" + fileCount + ".txt", true));
            if (set_num % 5 == 1)
                output += ("\nStart of problem set " + p_num);

            output += "\n\tProblem " + p_num + ", Set " + set_num +
                    ", Expanded, " + n.getCount() + "\n" + backtrack(n, "");
            if (set_num == 5)
                output += "\nEnd of problem set " + p_num;

            writer.write(output);
            writer.close();
        } catch (IOException e){
            System.out.println(e);
        }
    }

    //backtrack a node to its solution
    public static String backtrack(Node n, String stringSoFar){
        if (n.getParent() != null)
            stringSoFar = backtrack(n.getParent().getKey(), stringSoFar);
        else
            return  "State: " + n.getState().toString() + "| Action: Start\t" +
        "| h(n): " + n.geth() + "| g(n): " + n.getgVal() + "| iterations: 0";

        return stringSoFar + "\nState: " + n.getState().toString() + "| Action: " +
                n.getParent().getValue().getOutput() +
        "\t| h(n): " + n.geth() + "| g(n): " + n.getgVal() + "| iterations: " + n.getCount();
    }

    //clear output files
    public static void clearFiles(){
        try {
            for (int i = 1; i < 4; i++) {
                BufferedWriter writer =
                        new BufferedWriter(new
                                FileWriter(PATH + "OutfileHeuristic" + i + ".txt"));


                writer.write("");
                writer.close();
            }
        }catch (IOException e){
            System.out.println(e);
        }
    }

    public static void printBranchingFactors(ArrayList<Double> list){
        try {
                BufferedWriter writer =
                        new BufferedWriter(new FileWriter(PATH + "branchingTable.txt"));

                String output = "\tMAN \tDISP\tBOTH\n";
                double temp_average_a;
                double temp_average_b;
                double temp_average_c;

                for (int i = 0; i < 3; i++){
                    //avg first
                    temp_average_a = 0;
                    temp_average_b = 0;
                    temp_average_c = 0;
                    for (int j = 0; j < 5; j++){
                        temp_average_a += list.get(i+j);
                        temp_average_b += list.get(i+j+5);
                        temp_average_c += list.get(i+j+10);
                    }
                    output += (i+1)*5 + "\t" + String.format("%.2f", temp_average_a/5)
                            + "\t" + String.format("%.2f", temp_average_b/5)
                            + "\t" + String.format("%.2f", temp_average_c/5) + "\n";
                }
                writer.write(output);
                writer.close();
            }catch (IOException e){
            System.out.println(e);
        }
    }

    // driver function
    public static void main(String args[]){
        // clear all files first
        clearFiles();
        ArrayList<Double> branchFactors = new ArrayList<>();

        // print to the A* files
        int count = 0;
        int fileCount = 1;
        for (Heuristic h: Heuristic.values()) {
            for (ArrayList<Integer> l : readLine(PATH + "inputFile.txt")){
                Pair<Node, Double> answer = AStar(l, h);
                printSolution(answer.getKey(), count/5 + 1, count%5+1, fileCount);
                branchFactors.add(answer.getValue());
                count += 1;
            }
            count = 0;
            fileCount += 1;
        }

        //print out branching factors
        printBranchingFactors(branchFactors);

    }
}
