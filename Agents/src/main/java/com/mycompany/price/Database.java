/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.price;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cristinadelaguilamartin
 */
public class Database extends Agent{
    @Override
    protected void setup() {
        System.out.println("Database agent " + getAID().getName() + " is ready.");
        addBehaviour(new Database.ReceiveMessageBehaviour());
    }
    
    private class ReceiveMessageBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
           ACLMessage msg = receive();
           if(msg != null && msg.getSender().getLocalName().equals("Advisor")){
              String destination = msg.getContent();
              bestMonth(destination);
            }
           else{
               block();
           }
}
        private void bestMonth(String destino) {
            JSONParser jsonParser = new JSONParser();

            try (FileReader reader = new FileReader("/Users/cristinadelaguilamartin/NetBeansProjects/Agents/src/main/java/com/mycompany/price/data.json")) {
                Object obj = jsonParser.parse(reader);
                if (obj instanceof JSONObject) {
                    JSONObject data = (JSONObject) obj;
                    String month = (String) data.get(destino);  // "destino" is the key for the month

                    if (month != null) {
                        sendToAdvisor(month, destino);
                    } else {
                        System.out.println("Destino no encontrado");
                    }
                } else {
                    System.out.println("Error: el archivo JSON no tiene el formato esperado");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        
        private void sendToAdvisor(String month, String destination) {
            ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
            String content = month + " " + destination;
            reply.setContent(content);
            AID AdvisorAID = new AID("Advisor", AID.ISLOCALNAME);
            reply.addReceiver(AdvisorAID);
            send(reply);
        }
    }
    
}
