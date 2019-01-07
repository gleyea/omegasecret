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
	
    public final static String OMEGAFOUND = "OMEGAFOUND";
    public final static String SPYFOUND = "SPYFOUND";
    public final static String SECRETDISCOVERED = "SECRETDISCOVERED";
    public final static String GOODBYE = "GOODBYE";
    public final static String HELLO = "HELLO";

	
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
	
    private int tour = 0;
    private int stop = 0;
    private boolean guestsCreation = true;
    private boolean spyWait = false;
    private int wins = 0;
    private int numberOfTries = 0;

	
    protected Vector guestList = new Vector();    // NPCs
    protected Vector originalList = new Vector();    // NPCs
    protected Vector spyList = new Vector();    // Spies
    protected double moyenneTours = 0;
    
	
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
            for (int i = 0;  i < numberOfCivil;  i++) {
                    // create a new agent
                String localName = "Civil_"+i;
                AgentController guest = container.createNewAgent(localName, "omegasecret.Civil", null);
                guest.start();
                guestList.add( new AID(localName, AID.ISLOCALNAME) );
                originalList.add( new AID(localName, AID.ISLOCALNAME) );

                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

            }
            for (int i = 0;  i < numberOfMilitary;  i++) {
                        // create a new agent
                String localName = "Military_"+i;
                AgentController guest = container.createNewAgent(localName, "omegasecret.Military", null);
                guest.start();
                guestList.add( new AID(localName, AID.ISLOCALNAME) );
                originalList.add( new AID(localName, AID.ISLOCALNAME) );
                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

                    // keep the guest's ID on a local list
            }
                    
            for (int i = 0;  i < numberOfSoldier;  i++) {
                        // create a new agent
                String localName = "Soldier_"+i;
                AgentController guest = container.createNewAgent(localName, "omegasecret.Soldier", null);
                guest.start();
                guestList.add( new AID(localName, AID.ISLOCALNAME) );
                originalList.add( new AID(localName, AID.ISLOCALNAME) );
                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

                    // keep the guest's ID on a local list
            }
                    
            for (int i = 0;  i < numberOfPolitician;  i++) {
                        // create a new agent
                String localName = "Politician_"+i;
                AgentController guest = container.createNewAgent(localName, "omegasecret.Politician", null);
                guest.start();
                guestList.add( new AID(localName, AID.ISLOCALNAME) );
                originalList.add( new AID(localName, AID.ISLOCALNAME) );
                        //Agent guest = new GuestAgent();
                        //guest.doStart( "guest_" + i );

                        // keep the guest's ID on a local list
            }
            
            for (int i = 0;  i < numberOfSpy;  i++) {
                // create a new agent
            	String localName = "Spy_"+i;
            	AgentController spy = container.createNewAgent(localName, "omegasecret.Spy", null);
            	spy.start();
            	spyList.add( new AID(localName, AID.ISLOCALNAME) );
            	//Agent guest = new GuestAgent();
            	//guest.doStart( "guest_" + i );

            // keep the guest's ID on a local list
            }            
            // add a Behaviour to handle messages from guests
            addBehaviour( new CyclicBehaviour( this ) {      	
                            public void action() {
                            	
                                ACLMessage msg = receive();
                                /**
                                for (int i = 0; i < numberOfSpy; i++) {
                                	while (!spyWait) {
                                		if (!spyWait || stop == 0 || guestList.size() == 0) {
                                			conversation(i);
                                		}
                                	}
                                }**/
                                if (tour == 98) {
                                	//terminateSysteme();
                                	stop = 1;

                                }
                                if (msg != null && stop == 0) {
                                    if (HELLO.equals( msg.getContent() )) {
                            			conversation(msg.getSender());
                                        tour++;
                                    }
                                    else if (SPYFOUND.contentEquals((msg.getContent()))) {
                                    	System.out.println("Un espion a été trouvé en : " + tour + "tours!");
                                    	numberOfSpy--;
                                    	if (numberOfSpy == 0) {
                                        	System.out.println("Tous les espions ont été trouvés en : " + tour + "tours!");
                                        	endSecret();
                                        	if (numberOfTries < 30) {
                                            	retry();
                                            	moyenneTours+=tour;
                                            	numberOfTries++;
                                        	}
                                        	else {
                                        		System.out.println("Nombre de tours total : " + moyenneTours/30);
                                        		System.out.println("Pourcentage de victoires : " + wins/30);
                                        	}
                                    	}
                                    }
                                    else if (OMEGAFOUND.contentEquals((msg.getContent()))) {
                                    	System.out.println("Le secret OMEGA a été trouvé en : " + tour + "tours!");
                                    	endSecret();
                                    	if (numberOfTries < 30) {
                                        	retry();
                                        	wins++;
                                        	numberOfTries++;
                                    	}
                                    	else {
                                    		System.out.println("Nombre de tours total : " + moyenneTours/30);
                                    		System.out.println("Pourcentage de victoires : " + wins/30);
                                    	}

                                    }
                                    else if (msg.getContent().startsWith("next")) {
                                		conversation(msg.getSender());
                                        tour++;
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
            System.out.println( "Saw exception in HostAgent: " + e );
            e.printStackTrace();
        }

    }
	
    public void retry() {
    	tour = 0;
    	try {
    		PlatformController container = getContainerController(); // get a container controller for creating new agents
    		// create N guest agents
    		for (int i = 0;  i < numberOfCivil;  i++) {
                // create a new agent
    			String localName = "Civil_"+i;
    			AgentController guest = container.createNewAgent(localName, "omegasecret.Civil", null);
    			guest.start();
    			guestList.add( new AID(localName, AID.ISLOCALNAME) );
    			originalList.add( new AID(localName, AID.ISLOCALNAME) );

                //Agent guest = new GuestAgent();
                //guest.doStart( "guest_" + i );

    		}
    		for (int i = 0;  i < numberOfMilitary;  i++) {
                    // create a new agent
    			String localName = "Military_"+i;
    			AgentController guest = container.createNewAgent(localName, "omegasecret.Military", null);
    			guest.start();
    			guestList.add( new AID(localName, AID.ISLOCALNAME) );
    			originalList.add( new AID(localName, AID.ISLOCALNAME) );
                //Agent guest = new GuestAgent();
                //guest.doStart( "guest_" + i );

                // keep the guest's ID on a local list
    		}
                
    		for (int i = 0;  i < numberOfSoldier;  i++) {
                    // create a new agent
    			String localName = "Soldier_"+i;
    			AgentController guest = container.createNewAgent(localName, "omegasecret.Soldier", null);
    			guest.start();
    			guestList.add( new AID(localName, AID.ISLOCALNAME) );
    			originalList.add( new AID(localName, AID.ISLOCALNAME) );
                //Agent guest = new GuestAgent();
                //guest.doStart( "guest_" + i );

                // keep the guest's ID on a local list
    		}
                
    		for (int i = 0;  i < numberOfPolitician;  i++) {
                    // create a new agent
    			String localName = "Politician_"+i;
    			AgentController guest = container.createNewAgent(localName, "omegasecret.Politician", null);
    			guest.start();
    			guestList.add( new AID(localName, AID.ISLOCALNAME) );
            	originalList.add( new AID(localName, AID.ISLOCALNAME) );
                    //Agent guest = new GuestAgent();
                    //guest.doStart( "guest_" + i );

            	// keep the guest's ID on a local list
    		}
        
    		for (int i = 0;  i < numberOfSpy;  i++) {
    			// create a new agent
    			String localName = "Spy_"+i;
    			AgentController spy = container.createNewAgent(localName, "omegasecret.Spy", null);
    			spy.start();
    			spyList.add( new AID(localName, AID.ISLOCALNAME) );
    			//Agent guest = new GuestAgent();
    			//guest.doStart( "guest_" + i );

    		}
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
	
	public void conversation(AID spySender) {
		int chosenGuest = (int) (Math.random() * (guestList.size()-1));
		AID spy = (AID) guestList.get(chosenGuest);
        ACLMessage guestId = new ACLMessage( ACLMessage.INFORM );
        guestId.setContent( spy.getName() );
        guestId.addReceiver( spySender );
        send( guestId );
        guestList.remove(chosenGuest);
        numberOfPeople--;
        spyWait = false;
	}
	
	public void terminateSysteme() {
        try {
            if (!guestList.isEmpty() || !spyList.isEmpty()) {
                endSecret();
            }

            DFService.deregister( this );
            doDelete();
        }
        catch (Exception e) {
            System.err.println( "Saw FIPAException while terminating: " + e );
            e.printStackTrace();
        }
	}
	
	public void endSecret() {
        // send a message to all guests to tell them to leave
        for (Iterator i = originalList.iterator();  i.hasNext();  ) {
            ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
            msg.setContent( GOODBYE );

            msg.addReceiver( (AID) i.next() );

            send(msg);
        }

        for (Iterator i = spyList.iterator();  i.hasNext();  ) {
            ACLMessage msg = new ACLMessage( ACLMessage.INFORM );
            msg.setContent( GOODBYE );
            msg.addReceiver( (AID) i.next() );

            send(msg);
        }
        guestList.clear();
        originalList.clear();
        spyList.clear();
	}
}
