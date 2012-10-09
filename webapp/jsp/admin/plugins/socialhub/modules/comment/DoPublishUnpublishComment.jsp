<%@ page errorPage="../../../../ErrorPage.jsp" %>
<jsp:useBean id="socialHubComment" scope="session" class="fr.paris.lutece.plugins.socialhub.modules.comment.web.CommentJspBean" />
<% 
 	response.sendRedirect( socialHubComment.doPublishUnpublishComment( request ) );
%>
