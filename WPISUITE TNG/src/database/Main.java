package database;

import java.util.Calendar;
import java.util.Date;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class Main {
	// accessDb4o
	
	public static void main(String[] args){
		Examples example = new Examples();
		
		example.dbTest();
	}

}
