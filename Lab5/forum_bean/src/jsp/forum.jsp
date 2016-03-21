<html>
<head><title>Exempel 3</title></head>
<body>
<center>

<%@ page import="java.util.ArrayList" %>

<jsp:useBean class="bean.Forum" id="forum" scope="application"/>
<jsp:useBean class="bean.User" id="user" scope="session"/>
<jsp:useBean class="bean.Post" id="post" scope="request"/>

<jsp:setProperty name="forum" property="*"/>
<jsp:setProperty name="user" property="*"/>
<jsp:setProperty name="post" property="*"/>

<% if(session.isNew()){ %>
<h1>Ny session!</h1>
<form>
Nickname<input type="text" name="nickname"><br>
Email<input type="text" name="email">
<input type="submit"></form>

<%
}
if(request.getParameter("email")!=null){
%>
<h1>Ny användare</h1>
<form>
Text: <input type="text" name="text">
<input type="submit"></form>


<%
}
if(request.getParameter("text")!=null){
%>
<h1>Nytt inlägg</h1>
<%
post.setNickname(user.getNickname());
forum.addPost(post);
ArrayList posts = forum.getPosts();
for(int i = 0; i < posts.size(); i++){
post = (bean.Post)posts.get(i);
%>
<b><%=post.getText()%></b><br>
<i><%=post.getNickname()%></i><br>
<%
}
%>
<form>
Text: <input type="text" name="text">
<input type="submit"></form>
<%
}
%>


</center>
</body>
</html>
