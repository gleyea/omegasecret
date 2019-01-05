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
/**
            // add a Behaviour to process incoming messages
            addBehaviour( new CyclicBehaviour( this ) {
                            public void action() {
                                // listen if a greetings message arrives
                                ACLMessage msg = receive( MessageTemplate.MatchPerformative( ACLMessage.INFORM ) );

                                if (msg != null) {
                                    if (HostAgent.GOODBYE.equals( msg.getContent() )) {
                                        // time to go
                                        leaveParty();
                                    }
                                    else if (msg.getContent().startsWith( HostAgent.INTRODUCE )) {
                                        // I am being introduced to another guest
                                        introducing( msg.getContent().substring( msg.getContent().indexOf( " " ) ) );
                                    }
                                    else if (msg.getContent().startsWith( HostAgent.HELLO )) {
                                        // someone saying hello
                                        passRumour( msg.getSender() );
                                    }
                                    else if (msg.getContent().startsWith( HostAgent.RUMOUR )) {
                                        // someone passing a rumour to me
                                        hearRumour();
                                    }
                                    else {
                                        System.out.println( "Guest received unexpected message: " + msg );
                                    }
                                }
                                else {
                                    // if no message is arrived, block the behaviour
                                    block();
                                }
                            }
                        } );**/
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

}
