package edu.bothell.multi_ui.core;

import java.util.ArrayList;


public class Game {
    private final int                  MAX_PLAYERS = 3;
    private final ArrayList<Player>    p;
    private final State                s;
    private int                        turn;
    private Player                     active;
    private Directions                 dir;
    private boolean                    gameRunning = true;
    private boolean                    over;

    public Game(Control c){
        this.turn = 0;
        this.s = new World();
        this.p = new ArrayList<>();
    }
    
    public Player addPlayer(Player p){
        this.p.add(p);
        if(this.active == null) active = p;

        return p;
    }

    public Player addPlayer(char c, String sId){
        Player p = new Player(c);
        p.setSId(sId);
        return addPlayer(p);
    }

    public char[] getPlayersChar(){
        char[] pcs = new char[p.size()];
        for(int i = 0; i < pcs.length; i++) 
            pcs[i] = p.get(i).getChar();
        
        return pcs;
    }
    
    public boolean isValid(int[] pos, String sId){

        System.out.println("isVAlid?"+s.getIt(pos)+"|" + sId+"|" + active.getSId()+"|");


        int len = this.getState().getIt().length-1;

        if (pos[0] > len && pos[1] > len) return false;
        return s.isOpen(pos) && active.getSId().equals(sId);

    }

    public char play(int[] pos, String sId){

        //debounces / checks
        printState();
        // if is not valid, return nothing, terminate.
        if (this.isGameRunning()){
            if(!isValid(pos, sId)) return ' ';

            //progress game
            
            //inc turns
            this.turn++;
            //set active player characters to the position
            this.s.setIt(active.getChar(), pos[0], pos[1]);
            // check if the active player won, set is running based off of lose or win
            this.setGameRunning(!this.checkWin(active.getChar()));

            if (!this.isGameRunning()){
                stopGame();
                return ' ';
            }

            //switch the active player to the next item in the list
            this.active = p.get( turn % p.size() );
            // 


            //returns active character
        } 
        return active.getChar();
    }

    public void stopGame(){
        int len = this.getState().getIt().length-1;
        for(int y = 0; y < len+1; y++){
            for(int x = 0; x < len+1; x++){
                this.s.setIt(active.getChar(), x, y);
            }
        }
    }


    public boolean checkWin(char c){
        if (checkDiag(c, true, false)) return true;
        if (checkDiag(c, false, false)) return true;
        if (checkHoriz(c, false, false)) return true;
        return checkHoriz(c, true, false);
    }

    public boolean checkDiag(char c, boolean dir, boolean log){
        boolean check = true;
        int len = this.getState().getIt().length-1;
        int r;
        for(int i = 0; i < len+1; i++){

            if (log){
                if (dir){
                    System.out.println(i + "x " + i + " y");
                } else {
                    System.out.println(i + "x " + (len-i) + " y");
                }
            }

            if (dir){
                r = this.getState().getIt(i,i);
            } else {
                r = this.getState().getIt(i,len-i);
            }

            if (r != c){
                check = false;
            }

        }
        return check;
    }   

    public boolean checkHoriz(char c, boolean dir, boolean log){
        boolean check;
        int len = this.getState().getIt().length-1;
        int r;

        for(int y = 0; y < len+1; y++){
            check = true;
            for(int x = 0; x < len+1; x++){
                if (log){
                    if (dir){
                        System.out.println(x + "x " + y + " y  dir = true");
                    } else {
                        System.out.println(y + "x " + x + " y  dir = false");
                    }
                }

                if (dir){
                    r = this.getState().getIt(x, y);
                } else {
                    r = this.getState().getIt(y, x);
                }

                if (r != c){
                    check = false;
                }
            }   
            if (check){
                return check;
            }
        }
        return false;
    }

    public Player getActive() {
        return this.active;
    }

    public State getState() {
        return this.s;
    }

    public void printState(){
        int len = getState().getIt().length;
        for(int i = 0; i < len*len; i++){
            System.out.print(getState().getIt(i/len, i%len));
        }
        System.out.println("");
    }

    public Location getLocation(int x, int y) {
        return ((World)s).getLocation(x, y);
    }

    public int getMaxPlayers() {
        return MAX_PLAYERS;
    }

    public int getPlayerCount() {
        return p.size();
    }

    public ArrayList<Player> getPlayers(){
        return this.p;
    }

    public int getTurn(){
        return this.turn;
    }

    public boolean isGameRunning() {
        return gameRunning;
    }

    public void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }
}
