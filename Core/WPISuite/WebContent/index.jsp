<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>WPI Suite Admin Console</title>
</head>
<body>
<h1>WPI Suite Admin Console</h1>
<%!
//the first index of the fields array must be the unique
//identifier for this object
String coreusertitle = "CoreUser";
String coreuserpath = "core/user";
String[] coreuser = {
		"username",
		"name",
		"password",
		"id"
};
int coreuserlength = 4;

%>
<%!
public String createModelField(String title, String path, String[] args, int length)
{
	String text = "<h3>"+title+"</h3>";
	for(int i = 0; i < length; i++)
	{
		text = text + "<label>" + args[i] +": </label><input id="+args[i]+title+" maxlength=\'40\' /><br />";
	}
	text += "<button onclick=\""+title+"(create)\">create "+title+"</button>";
	text += "<button onclick=\""+title+"(read)\">read "+title+"</button>";
	text += "<button onclick=\""+title+"(update)\">update "+title+"</button>";
	text += "<button onclick=\""+title+"(delete)\">delete "+title+"</button><br />";
	text += "<textarea id="+title+" rows=\"5\" cols=\"40\"></textarea>";
	
	text += "<script type=\"text/javascript\" >";
	text += "function "+title+"(){";
	text += "send = new Object();";
	for(int i = 0; i < length; i++)
	{
		text += "send."+args[i]+"= document.getElementById(\""+args[i]+title+"\").value";
	}
			
	text += "</script>";
	
	return text + "<br />";
}
%>

<%= createModelField(coreusertitle, coreuserpath, coreuser, coreuserlength) %>
</body>
</html>