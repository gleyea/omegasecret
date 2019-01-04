package omegasecret;

public class NPC extends Personne {
	
	protected double probaFind;
	protected boolean hasOmega= false;
	protected double maximumpercentage =0.25;
	
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
