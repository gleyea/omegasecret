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

public class Systeme extends Agent {
	
	private int numberOfPeople = 100;
	private int numberOfSpy=1;
	private int numberOfCivil=50;
	private int numberOfMilitary=20;
	private int numberOfSoldier=25;
	private int numberOfPolitician=4;

	private int sumOfSecretDuSystem=0;
	private double percentCivil=0.5;
	private double percentMilitary=0.2;
	private double percentSoldier=0.25;
	private double percentPolitician=0.05;
	private Personne[] table;
	private Spy[] spytable;
	private int sizetable=1000;
	
	
	public Systeme () {/**
		for (int i =0; i<numberOfPeople; i++) {
			if (i<numberOfPeople*percentCivil) {
				table[i]=new Civil();
				sumOfSecretDuSystem+= table[i].numberOfSecret;
			}
			else if (i<numberOfPeople*(percentCivil+percentMilitary)){
				table[i]=new Military();
				sumOfSecretDuSystem+= table[i].numberOfSecret;
			}
			else if (i<numberOfPeople*(percentCivil+percentMilitary+percentSoldier)){
				table[i]=new Soldier();
				sumOfSecretDuSystem+= table[i].numberOfSecret;
			}
			else {
				table[i]=new Politician();
				sumOfSecretDuSystem+= table[i].numberOfSecret;
			}
		}
		for (int i=0; i<numberOfSpy; i++) {
			spytable[i]=new Spy();
			
		}**/
	}
	
    protected void setup() {
        try {
            System.out.println( getLocalName() + " setting up");

            // create the agent descrption of itself
            DFAgentDescription dfd = new DFAgentDescription();
            dfd.setName( getAID() );
            DFService.register( this, dfd );
            PlatformController container = getContainerController(); // get a container controller for creating new agents
            // create N guest agents
            for (int i = 0;  i < numberOfSpy;  i++) {
                    // create a new agent
            String localName = "spy_"+i;
    		AgentController guest = container.createNewAgent(localName, "omegasecret.Spy", null);
    		guest.start();
                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

                    // keep the guest's ID on a local list
                } 
                
                for (int i = 0;  i < numberOfCivil;  i++) {
                    // create a new agent
    		String localName = "Civil_"+i;
    		AgentController guest = container.createNewAgent(localName, "omegasecret.Civil", null);
    		guest.start();
                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

                }
                
                for (int i = 0;  i < numberOfMilitary;  i++) {
                    // create a new agent
    		String localName = "Military_"+i;
    		AgentController guest = container.createNewAgent(localName, "omegasecret.Military", null);
    		guest.start();
                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

                    // keep the guest's ID on a local list
                }
                
                for (int i = 0;  i < numberOfSoldier;  i++) {
                    // create a new agent
    		String localName = "Soldier_"+i;
        	AgentController guest = container.createNewAgent(localName, "omegasecret.Soldier", null);
    		guest.start();
                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

                    // keep the guest's ID on a local list
                }
                
                for (int i = 0;  i < numberOfPolitician;  i++) {
                    // create a new agent
    		String localName = "Politician_"+i;
    		AgentController guest = container.createNewAgent(localName, "omegasecret.Politician", null);
    		guest.start();
                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

                    // keep the guest's ID on a local list
                }
            // add a Behaviour to handle messages from guests
            /**addBehaviour( new CyclicBehaviour( this ) {
                            public void action() {
                                ACLMessage msg = receive();

                                if (msg != null) {
                                    if (msg.getPerformative() == ACLMessage.REQUEST  &&  INTRODUCE.equals( msg.getContent() )) {
                                        // an agent has requested an introduction
                                        doIntroduction( msg.getSender() );
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
            System.out.println( "Saw exception in HostAgent: " + e );
            e.printStackTrace();
        }

    }
	
	public void shuffle(int numberofshuffle) {
		int randomnumberun;
		int randomnumberdeu;
		Personne transit;
		for (int i=0; i<numberofshuffle; i++) {
			randomnumberun=(int)(Math.random()*sizetable);
			randomnumberdeu=(int)(Math.random()*sizetable);
			transit=table[randomnumberun];
			table[randomnumberun]=table[randomnumberdeu];
			table[randomnumberdeu]=transit;
		}
	}
	
	public void sumOfSecret() {
		sumOfSecretDuSystem=0;
		for (int i =0; i<numberOfPeople; i++) {
			sumOfSecretDuSystem+=table[i].getnumberOfSecret();
		}
	}
	
}
