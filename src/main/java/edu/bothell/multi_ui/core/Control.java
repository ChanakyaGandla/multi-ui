package edu.bothell.multi_ui.core;

import org.springframework.stereotype.Service;

import edu.bothell.multi_ui.ui.UI;
import edu.bothell.multi_ui.ui.swing.Swing;

@Service
public class Control {

    private final UI    ui;
    private final Game   g = new Game(this);
    
     
    public Control(){
        this.ui = null;
        System.out.println("CONTROL ACTIVE: for web");
    }

    public Control(Player p){
        this.ui = new Swing(this);
        this.g.addPlayer(p);
        System.out.println("CONTROL ACTIVE: for SWING");
    }
    

    public void launch(){
    
    }

    public State getState(){
        return this.g.getState();
    }

    // pos[0] is X
    public char update(int[] pos, String sId) {
        return g.play(pos, sId);
    }

    public Player getActive() {
        return g.getActive();
    }

    public Player addPlayer(char c, String sId){
        return g.addPlayer(c, sId);
    }

    public char[] getPlayersChar(){
        return g.getPlayersChar();
    }

    public int getPlayerCount(){
        return g.getPlayerCount();
    }

    public int getMaxPlayers() {
        return g.getMaxPlayers();
    }
    
    public int getTurn(){
        return g.getTurn();
    }

    public Location getLocation(int x, int y){
        return g.getLocation(x,y);
    }

    public String[][] getTerrainStrings() {

        Location ls[][] = ((World)g.getState()).getMap();

        String[][] ts = new String[ls.length][];
        for(int y = 0; y < ls.length; y++){
            ts[y] = new String[ls[y].length];
            for(int x = 0; x < ls[y].length; x++){
                ts[y][x] = ls[y][x].getTerrainString() + " ";
                
                for(int w = 0; w < ls[y][x].getWalls().length; w++){
                    ts[y][x] += ( ls[y][x].getWalls()[w] == null )? 
                        "" : " w" + Directions.values()[w];
                } /**/
            }
        }
        
        return ts;
    }

    public String checkConnected(int x, int y) {
        return "" + getLocation(x, y).getMatchTerrain(null);
        //throw new UnsupportedOperationException("Unimplemented method 'checkConnected'");
    }




    public String[][] getWallsStrings() {
        Location ls[][] = ((World)g.getState()).getMap();
        String[][] ws = new String[ls.length][];

        for(int y = 0; y < ls.length; y++){
            ws[y] = new String[ls[y].length];
            for(int x = 0; x < ls[y].length; x++){
                for(int w = 0; w < ls[y][x].getWalls().length; w++){
                    ws[y][x] = " " + ( (Directions.values()[w] == null)? "": Directions.values()[w] );
                }
            }
        }

        return ws;
    }

}
