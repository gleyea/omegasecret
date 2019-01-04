package omegasecret;

public class Spy extends Personne {
	private double probaMultiplier=1;
	private double sommeDesSecrets=1000;

	public Spy () {
		numberOfSecret = 0;
		
	}
	public void Update() {
		probaMultiplier = 1* (1+(numberOfSecret/sommeDesSecrets));
	}
	
	
}
