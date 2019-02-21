package com.company;

public enum Action {
    UP("Up\t"), DOWN("Down"), LEFT("Left"), RIGHT("Right");

    private String output;

    public String getOutput(){
        return this.output;
    }
    Action(String output){
        this.output = output;
    }
}
