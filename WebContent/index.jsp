<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="com.gc.temple.dto.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Temple Registration</title>
<link rel="stylesheet" href="css/formstyle1.css" type="text/css">
<!-- Ref: http://www.w3schools.com/w3css/tryit.asp?filename=tryw3css_lists_avatar -->
<link rel="stylesheet" href="http://www.w3schools.com/lib/w3.css">
</head>
<body>
	<div class="form-style-10">
		<h1>
			Welcome to Bharatiya Temple Library!<span>Member Check-In</span>
		</h1>
		<%-- <%= request.getAttribute("result") %> --%>
		<ul class="w3-ul w3-card-4">
		<%
			List<Patron> results = (List<Patron>) request.getAttribute("result");
			for (Patron patron : results) {
		%>
		
		  <li class="w3-padding-hor-16">
		    <span onclick="this.parentElement.style.display='none'" 
		    class="w3-closebtn w3-padding w3-margin-right w3-medium">x</span>
		    <img src="/TempleWeb/img/<%=patron.getImagePath() %>" class="w3-left w3-circle w3-margin-right" style="width:60px">
		    <span class="w3-xlarge"><%=patron.getFirstName()%> <nbsp> <%=patron.getLastName() %></span><br>
		    <span><%=patron.getContactInfo().getAddress() %></span><br>
		    <span><%=patron.getContactInfo().getCity() %>, <nbsp><%=patron.getContactInfo().getState()%>, <nbsp><%=patron.getContactInfo().getZip()%></span><br>
		    <span><%=patron.getContactInfo().getPhoneNumber() %></span><br>
		    <span>Member:<%=patron.isMember() %></span>
		  </li>		
		<%
			}
		%>
		</ul>
	</div>

</body>
</html>