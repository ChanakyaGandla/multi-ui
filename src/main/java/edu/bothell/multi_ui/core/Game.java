package edu.bothell.multi_ui.core;

import java.util.ArrayList;


public class Game {
    private final int                  MAX_PLAYERS = 3;
    private final ArrayList<Player>    p;
    private final State                s;
    private int                        turn;
    private Player                     active;

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
        return s.isOpen(pos) && active.getSId().equals(sId);
    }

    public char play(int[] pos, String sId){
        if(!isValid(pos, sId)) return ' ';
        turn++;
        this.s.setIt(active.getChar(), pos[0], pos[1]);
        this.active = p.get( turn % p.size() );

        return active.getChar();
    }

    public Player getActive() {
        return this.active;
    }

    public State getState() {
        return this.s;
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
    public boolean checkWin(Player checkPlayer, int turns, State state){
        if ()
        if (checkLines(checkPlayer, state, true)) return true; 
        if (checkLines(checkPlayer, state, false)) return true; 
        if (checkDiags(checkPlayer, state, true)) return true; 
        return checkDiags(checkPlayer, state, false);
    }

    public boolean checkTie(int turns, State state){
        if (int turns )
    }
    public boolean checkLines(Player checkPlayer, State state, boolean flip){
        boolean check = true;
        char checkedTile;
        char[][] s = state.getIt();
        char playerChar = checkPlayer.getChar();
        for(int y = 0; y < s.length; y++){
            check = true;
            for(int x = 0; x < s[0].length; y++){
                if (flip){
                    checkedTile = s[y][x];
                } else {
                    checkedTile = s[x][y];
                }
                if (checkedTile == playerChar){
                    check = false;
                }
            }
            if (check){
                return true;
            }
        }
        return false;
    }
    public boolean checkDiags(Player checkPlayer, State state, boolean flip){
        boolean check = true;
        char checkedTile;
        char[][] s = state.getIt();
        char playerChar = checkPlayer.getChar();

        for(int i = 0; i < s.length; i++){
            if (flip){
                checkedTile = s[i][i];
            } else {
                checkedTile = s[(s.length-1)-i][i];
            }
            if (checkedTile == playerChar){
                check = false;
            }
        }
        return check;
    }
}
