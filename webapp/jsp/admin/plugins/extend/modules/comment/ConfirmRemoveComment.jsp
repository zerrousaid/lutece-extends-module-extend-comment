<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:useBean id="extendComment" scope="session" class="fr.paris.lutece.plugins.extend.modules.comment.web.CommentJspBean" />
<% 
 	response.sendRedirect( extendComment.getConfirmRemoveComment( request ) );
%>
