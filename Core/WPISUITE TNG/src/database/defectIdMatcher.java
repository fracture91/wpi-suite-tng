package database;


public class defectIdMatcher {
	
	private int id;
	public defectIdMatcher(int primaryID){
		this.id = primaryID;
	}
	
	public boolean match(TNG passedDefect){
		return passedDefect.getID() == this.id;
	}

}
