package fr.paris.lutece.plugins.extend.modules.comment.service;

import java.util.List;


/**
 * Interface of listeners of comments
 */
public interface ICommentListener
{
    /**
     * Notify the creation of a comment
     * @param strIdExtendableResource The id of the extendable resource
     *            associated with the created comment
     * @param bPublished True if the created comment is published, false
     *            otherwise
     */
    void createComment( String strIdExtendableResource, boolean bPublished );

    /**
     * Notify the publication or unpublication of a comment
     * @param strIdExtendableResource The id of the extendable resource
     *            associated with the modified comment
     * @param bPublished True if the comment was published, false if it was
     *            unpublished
     */
    void publishComment( String strIdExtendableResource, boolean bPublished );

    /**
     * Notify the removal of a comment
     * @param strIdExtendableResource The id of the extendable resource
     *            associated with the removed comment
     * @param listIdRemovedComment The list of ids of removed comments
     */
    void deleteComment( String strIdExtendableResource, List<Integer> listIdRemovedComment );
}
