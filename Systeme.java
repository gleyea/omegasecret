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
	private int numberOfPeople = 1000;
	private int numberOfSpy=1;
	private int sumOfSecretDuSystem=0;
	private double percentCivil=0.5;
	private double percentMilitary=0.2;
	private double percentSoldier=0.25;
	//private double percentPolitician=0.05;
	private Personne[] table;
	private Spy[] spytable;
	private int sizetable=1000;
	
	public Systeme () {
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
