<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>WPI Suite Admin Console</title>
<%!
//the first index of the fields array must be the unique
//identifier for this object
String coreusertitle = "CoreUser";
String coreuserpath = "core/user";
String[] coreuser = {
		"username",
		"name",
		"password",
		"idNum"
};
int coreuserlength = 4;

String coreprojecttitle = "CoreProject";
String coreprojectpath = "core/project";
String[] coreproject = {
		"idNum",
		"name"
};
int coreprojectlength = 2;

public String createModelField(String title, String path, String[] args, int length)
{
	String text = "<h3>"+title+"</h3>";
	for(int i = 0; i < length; i++)
	{
		text = text + "<label>" + args[i] +": </label><input id="+args[i]+title+" maxlength=\'40\' /><br />\n";
	}
	text += "<button onclick=\""+title+"('create')\">create "+title+"</button>\n";
	text += "<button onclick=\""+title+"('read')\">read "+title+"</button>\n";
	text += "<button onclick=\""+title+"('update')\">update "+title+"</button>\n";
	text += "<button onclick=\""+title+"('delete')\">delete "+title+"</button><br />\n";
	text += "<textarea id="+title+"textArea rows=\"5\" cols=\"40\"></textarea>\n";
	return text + "<br />";
}
public String createModelScript(String title, String path, String[] args, int length)
{
	String scriptText = "<script type=\"text/javascript\" >\n";
	scriptText += "function "+title+"(action){\n";
	scriptText += "send = new Object();\n";
	for(int i = 0; i < length; i++)
	{
		scriptText += "send."+args[i]+"= document.getElementById(\""+args[i]+title+"\").value\n";
	}
	scriptText += "jsend = JSON.stringify(send);\n";
	scriptText += "var request = new XMLHttpRequest();\n" +
			"request.onreadystatechange = function() {\n" +
				"if(request.readyState == 4 && request.status > 0) {\n" +
					"document.getElementById(\""+title+"textArea\").value = request.responseText;\n" +
				"}\n" +
			"};\n";
	scriptText += "if(action == 'create'){ \n"+
				"request.open('PUT', \"API/"+path+"/\", true)\n"+
		    	"request.send(jsend);\n" +
			"}else if(action == 'read'){ \n" +
				"request.open('GET', \"API/"+path+"/\"+send."+args[0]+", true)\n"+
		    	"request.send();\n" +
			"}else if(action == 'update'){ \n" +
				"request.open('POST', \"API/"+path+"/\"+send."+args[0]+", true)\n"+
		    	"request.send(jsend);\n" +
			"}else if(action == 'delete'){ \n" +
				"request.open('DELETE', \"API/"+path+"/\"+send."+args[0]+", true)\n"+
		    	"request.send();\n" +
			"}}\n";
			
	scriptText += "</script>";	
	return scriptText;
}
%>
<%= createModelScript(coreusertitle, coreuserpath, coreuser, coreuserlength) %>
<%= createModelScript(coreprojecttitle, coreprojectpath, coreproject, coreprojectlength) %>
</head>
<body>
<h1>WPI Suite Admin Console</h1>

<script type="text/javascript">


function login()
{
	//generate unencoded authentication header
	var authString = document.getElementById("loginusername").value + ":" + document.getElementById("loginpassword").value;
	//Base64 encode the header
	authString = window.btoa(authString);
	//add the word Basic plus a space
	authString = 'Basic ' + authString;
	
	//create new XHR
	var xml = new XMLHttpRequest();
	
	//define behavior for when the response is recieved
	xml.onreadystatechange = function()
	{
		if(xml.readyState == 4)//wait until response is available
		{
			document.getElementById("loginresponsespan").innerHTML = xml.statusText;
		}
	};
	
	//setup reuqest to POST to /API/Login
	xml.open('POST','API/login',false);
	//set the request header
	xml.setRequestHeader('Authorization', authString);
	//send the request
	xml.send();             
}

function roleChange()
{
	//create new XHR
	var xml = new XMLHttpRequest();
	//define behavior for when the response is recieved
	xml.onreadystatechange = function()
	{
		if(xml.readyState == 4)//wait until response is available
		{
			document.getElementById("rolechangeresponse").innerHTML = xml.statusText;
		}
		
	};
	
	
	var user = new Object();
	user.username = document.getElementById("rolechangeusername").value;
	user.role = document.getElementById("roleselector").options[document.getElementById("roleselector").selectedIndex].value;
	
	var juser = JSON.stringify(user);
	if(user.username == "")
	{return;}
	
	//setup reuqest to POST to /core/user
	xml.open('POST','API/core/user/'+user.username,false);
	//send the request
	xml.send(juser); 
}

function teamMember(action)
{
	//create new XHR
	var xml = new XMLHttpRequest();
	//define behavior for when the response is recieved
	xml.onreadystatechange = function()
	{
		if(xml.readyState == 4)//wait until response is available
		{
			document.getElementById("projectteamresponse").innerHTML = xml.statusText;
		}
		
	};
	
	document.getElementById("projectteamresponse").innerHTML = action;
	
	var proj = document.getElementById("projectteamid").value;
	var user = document.getElementById("projectteamname").value;
	
	if(user == "")
	{return;}
	
	//make the user name into a json array of 1 string
	var users = new Array(1);
	users[0] = user;
	var juser = JSON.stringify(users);
	
	//setup reuqest to POST to /API/Login
	xml.open('PUT','API/Advanced/core/project/'+proj+'/'+action,false);
	//send the request
	xml.send(juser); 
}

</script>

<h4>Login:</h4>
Username:<input type="text" id="loginusername"></input><br>
Password:<input type="password" id="loginpassword"></input><br>
<input type="button" value="Submit" onclick="login()"><span id="loginresponsespan"></span>

<%= createModelField(coreusertitle, coreuserpath, coreuser, coreuserlength) %>
<h4>Role Change:</h4>
Username:<input type="text" id="rolechangeusername"></input><br>
Role:<select id="roleselector"><option value="USER">User</option><option value="ADMIN">Admin</option></select>
<input type="button" value="Submit" onclick="roleChange()"><span id="rolechangeresponse"></span>

<%= createModelField(coreprojecttitle, coreprojectpath, coreproject, coreprojectlength) %>
<h4>Team Members:</h4>
Project idNum:<input type="text" id="projectteamid"></input><br>
Username:<input type="text" id="projectteamname"></input><br>
<input type="button" value="Add Team Member" onclick="teamMember('add')"><input type="button" value="Remove Team Member" onclick="teamMember('remove')"><span id="projectteamresponse"></span>

</body>
</html>