package edu.bothell.multi_ui.ui;

import edu.bothell.multi_ui.core.Control;

import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class SocketHandle extends TextWebSocketHandler {
    // PROPERTIES -----------------------------------------------------------------
    private final Control                                c;
    private final CopyOnWriteArrayList<String>           s;
    private final CopyOnWriteArrayList<WebSocketSession> ws;

    // CONSTRUCTOR ----------------------------------------------------------------
    public SocketHandle(Control c) {
        this.c  = c;
        this.s  = new CopyOnWriteArrayList<>();
        this.ws = new CopyOnWriteArrayList<>();

    }

    // MAPPINGS -------------------------------------------------------------------
    @GetMapping("/socket")
    @ResponseBody
    public ResponseEntity<char[]> getSocket( HttpSession sesion, Model m){
        String sId = sesion.getId();

        if(!s.contains(sId) && s.size() <= c.getMaxPlayers() ){
            this.s.add(sId);
            c.addPlayer( (char)(s.size() + '1'), sId );
        }
        
        return ResponseEntity.ok(c.getPlayersChar());
    }

    // METHODS --------------------------------------------------------------------
    private void broadcastGameState() throws IOException{
        // Create the shared state object from the game logic
        Transit sharedState = new Transit(
            c.getState().getIt(), 
            c.getActive().getChar(), 
            c.getTerrainStrings(),
            c.getOver(),
            c.getTurns()
        );        

        // Convert the shared state to JSON
        String sJson = new ObjectMapper().writeValueAsString(sharedState);

        // Broadcast the JSON to all connected clients
        for (WebSocketSession session : ws) {
            try{
                if (session.isOpen()) 
                    session.sendMessage(new TextMessage(sJson));
            }
            catch(IOException e){
                System.err.println("Error sending to session: " + session.getId());
            }
            
        }
    }

    // OVERRIDES --------------------------------------------------------------------
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        System.out.println("Received: " + payload);
        
        broadcastGameState();

        // Process player action based on payload
        if (payload.startsWith("move:")) {
            String[] coords = payload.substring(5).split(",");
            int x = Integer.parseInt(coords[0]);
            int y = Integer.parseInt(coords[1]);

            // Update the game state in Control
            c.update(new int[]{x, y}, session.getId());

            // Broadcast the updated game state
            broadcastGameState();
        }
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        ws.add(session);
        System.out.println("WebSocket connection established: " + session.getId());
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        ws.remove(session);
        System.out.println("WebSocket connection closed: " + session.getId());
    }


}
