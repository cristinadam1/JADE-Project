/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.price;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author cristinadelaguilamartin
 */
public class Interface extends Agent{
     @Override
    protected void setup() {
        System.out.println("Interface agent " + getAID().getName() + " is ready.");
        addBehaviour(new ReceiveMessageBehaviour());
    }
    
    private class ReceiveMessageBehaviour extends CyclicBehaviour {

        @Override
        public void action() {
          ACLMessage msg = receive();
          if(msg != null && !msg.getSender().getLocalName().equals("Advisor")){              
              ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
              request.addReceiver(new jade.core.AID("Advisor", jade.core.AID.ISLOCALNAME));
              request.setContent(msg.getContent());
              send(request);
          }
          if(msg != null && msg.getSender().getLocalName().equals("Advisor")){
              String recommendation = msg.getContent();
              System.out.println(recommendation);
           }
          else{
              block();
          }
        }
        
}
}

