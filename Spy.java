package omegasecret;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.Profile;

import jade.wrapper.PlatformController;
import jade.wrapper.AgentController;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;

import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.DFService;
import jade.domain.FIPAException;

import javax.swing.*;
import java.util.*;
import java.text.NumberFormat;

public class Spy extends Personne {
	private double probaMultiplier=1;
	private double sommeDesSecrets=1000;
	private AID system;

	public Spy () {
		numberOfSecret = 0;
        System.out.println( "Spy created");

	}
	
    protected void setup() {
        try {
            // create the agent descrption of itself
            ServiceDescription sd = new ServiceDescription();
            sd.setType( "Spy" );
            sd.setName( "SpyServiceDescription" );
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            dfd.addServices( sd );

            // register the description with the DF
            DFService.register( this, dfd );

            ACLMessage hello = new ACLMessage( ACLMessage.INFORM );
            hello.setContent( Systeme.HELLO );
            hello.addReceiver( new AID( "systeme", AID.ISLOCALNAME ) );
            send( hello );
            // add a Behaviour to process incoming messages
            addBehaviour( new CyclicBehaviour( this ) {
                            public void action() {
                                // listen if a greetings message arrives
                                ACLMessage msg = receive();
                                if (msg != null) {
                                    if (Systeme.GOODBYE.equals(msg.getContent()) || msg.getContent().startsWith("found") ) {
                                        // time to go
                                        leave();
                                    }
                                    if (msg.getContent().startsWith("Pong")) {
                                    	String numberOfSecret_str = msg.getContent().substring( msg.getContent().indexOf( " " ) + 1 );
                                    	numberOfSecret += Integer.parseInt(numberOfSecret_str);
                                    	Update();
                                    	continueAsking();
                                    	}
                                    else if (!msg.getContent().startsWith( "true" ) && !msg.getContent().startsWith( "false" )){
                                        // I am being introduced to another guest
                                    	searchSecret(msg.getContent());
                                    	system = msg.getSender();
                                    }
                                }

                                else {
                                    // if no message is arrived, block the behaviour
                                    block();
                                }
                            }
                        } );
        }
        catch (Exception e) {
            System.out.println( "Saw exception in Spy: " + e );
            e.printStackTrace();
        }

    }
    
	public void Update() {
		probaMultiplier = 1* (1+(numberOfSecret/sommeDesSecrets));
		double test = Math.random();
		if (test <= 0.5) {
			test = 0.5;
		}
		if ((numberOfSecret/sommeDesSecrets) > test) {
	        ACLMessage ask = new ACLMessage( ACLMessage.INFORM );
            ask.setContent( Systeme.OMEGAFOUND );
	        ask.addReceiver( system );
	        send( ask );  
		}
	}
	
    public void continueAsking() {
        ACLMessage ask = new ACLMessage( ACLMessage.INFORM );
        ask.setContent( "next" );
        ask.addReceiver( system );
        send( ask );  
    }
    
    public void searchSecret(String guestId) { 
        AID aID = new AID( guestId, AID.ISGUID); 
        ACLMessage ask = new ACLMessage( ACLMessage.INFORM );
        ask.setContent( "asking" + " " +  probaMultiplier );
        ask.addReceiver( aID);
        send( ask );    
        }
    
    public void leave() {
        try {
            DFService.deregister( this );
            doDelete();
        }
        catch (FIPAException e) {
            System.err.println( "Saw FIPAException while leaving party: " + e );
            e.printStackTrace();
        }
    }
}
