package database;

import java.util.Date;

public class Defect implements TNG {
	private int id;
	private String reportedBy;
	private Date submitted; 
	private String description;

	public Defect(int anID, String reporter, Date dateSubmitted, String aDescription) {
		this.id=anID;
		this.reportedBy=reporter;
		this.submitted = dateSubmitted;
		this.description = aDescription;
	}
	
	public String toString(){
		return "Defect "+id+" Reported by " + reportedBy +" at " +submitted;
	}
	
	public int getID(){
		return this.id;
	}
	public String getReportedBy(){
		return this.reportedBy;
	}
}
