/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.price;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author cristinadelaguilamartin
 */
public class Advisor extends Agent {
    @Override
    protected void setup() {
        System.out.println("Advisor agent " + getAID().getName() + " is ready.");
        addBehaviour(new ReceiveMessageBehaviour());
    }
    
    private class ReceiveMessageBehaviour extends CyclicBehaviour {
        @Override
        public void action() {
           //El Advisor muestra lo que le ha enviado Interface y lo envía a la Database 
           ACLMessage msg = receive();
           if(msg != null && msg.getSender().getLocalName().equals("Interface")){
              String destination = msg.getContent();
              sendToDatabase(destination);  
            }
           else if (msg != null && msg.getSender().getLocalName().equals("Database")) {
                String content = msg.getContent(); 
                String[] params = content.split(" ");

                if (params.length == 2) { 
                    String month = params[0]; 
                    String destination = params[1];               
                    String recommendation = "The best month to travel to " + destination + " is " + month;
                    sendToInterface(recommendation);
                } else {
                    System.out.println("Error: The message does not contain the two expected parameters");
                }
            }else {
               block();
           }
}
        //Send the destination to the Database 
        private void sendToDatabase(String destination) {
            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
            request.setContent(destination);
            AID databaseAID = new AID("Database", AID.ISLOCALNAME);
            request.addReceiver(databaseAID);
            send(request);
        }
        private void sendToInterface(String recommendation) {
            ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
            reply.setContent(recommendation);
            AID InterfaceAID = new AID("Interface", AID.ISLOCALNAME);
            reply.addReceiver(InterfaceAID);
            send(reply);
        }
}
}
