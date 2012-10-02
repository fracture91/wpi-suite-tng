package wpisuite.core;

import javax.servlet.http.*;
import javax.servlet.*;

import com.db4o.ObjectServer;
import com.db4o.cs.Db4oClientServer;

public class DB4OServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	static String WPI_TNG_DB ="WPISuite_TNG";
	
	public DB4OServlet(){
		ObjectServer server = Db4oClientServer.openServer(Db4oClientServer
				.newServerConfiguration(), WPI_TNG_DB, 8081);
		server.grantAccess("bgaffey", "password");
	}

}
