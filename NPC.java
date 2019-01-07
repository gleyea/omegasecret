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

public class NPC extends Personne {
	
	protected double probaFind;
	protected boolean hasOmega= false;
	protected double maximumpercentage =0.25;	
	
    protected void setup() {
        try {
            // create the agent descrption of itself
            ServiceDescription sd = new ServiceDescription();
            sd.setType( "Civil" );
            sd.setName( "CivilServiceDescription" );
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            dfd.addServices( sd );

            // register the description with the DF
            DFService.register( this, dfd );

            // add a Behaviour to process incoming messages
            addBehaviour( new CyclicBehaviour( this ) {
                            public void action() {
                                // listen if a greetings message arrives
                                ACLMessage msg = receive();

                                if (msg != null) {
                                    if (Systeme.GOODBYE.equals( msg.getContent() )) {
                                        // time to go
                                        leaveGuest();
                                    }
                                    if (msg.getContent().startsWith( "asking" )) {
                                    	String multiplier_str = msg.getContent().substring( msg.getContent().indexOf( " " ) + 1 );
                                    	double found = Double.parseDouble(multiplier_str);
                                    	found = Math.random() * found;
                                    	System.out.println(found);
                                    	if (found < probaFind) {
                                    		spyFound(msg.getSender());
                                    	}
            							answer(msg.getSender());
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
            System.out.println( "Saw exception in Civil: " + e );
            e.printStackTrace();
        }

    }
	
	
	
	public double getprobaFind() {
		return probaFind;
	}
	
	public void modifyNumberOfSecret(int modify) {
		numberOfSecret -= modify;
	}
	public boolean gethasOmega() {
		return hasOmega;
	}
	
	public int secretObtained() {
		double random = Math.random();
		int numberobtained=(int)(random*(numberOfSecret+1)*maximumpercentage);
		if (random*maximumpercentage*(numberOfSecret+1) != numberobtained) {
			numberobtained+=1;
		}
		modifyNumberOfSecret(numberobtained);
		return numberobtained;
	}
	
	public void spyFound(AID sender) {
        ACLMessage hello = new ACLMessage( ACLMessage.INFORM );
        hello.setContent( Systeme.SPYFOUND);
        hello.addReceiver( new AID( "systeme", AID.ISLOCALNAME ) );
        send( hello );
        ACLMessage reply = new ACLMessage( ACLMessage.INFORM );
        reply.setContent( "found" );
        reply.addReceiver( sender);
        send (reply);
	}
	public void answer(AID sender) {
        ACLMessage reply = new ACLMessage( ACLMessage.INFORM );
        reply.setContent( "Pong" + " " + numberOfSecret );
        reply.addReceiver( sender);
        send (reply);
	}
	
    protected void leaveGuest() {
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
