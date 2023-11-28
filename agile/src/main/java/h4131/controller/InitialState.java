package h4131.controller;

import h4131.view.WindowBuilder;

public class InitialState implements State{
    
    @Override
    public void loadGlobalTour(Controller c, WindowBuilder w){
        System.out.println("initial state : load global tour");
    }
}
