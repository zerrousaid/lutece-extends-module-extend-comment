package fr.paris.lutece.plugins.extend.modules.comment.service;

/**
 * Interface of listeners of comments
 */
public interface ICommentListener
{
    /**
     * Notify the creation of a comment
     * @param strIdExtendableResource The id of the extendable resource
     *            associated with the created comment
     */
    void createComment( String strIdExtendableResource );

    /**
     * Notify the modification of a comment
     * @param strIdExtendableResource The id of the extendable resource
     *            associated with the modified comment
     */
    void updateComment( String strIdExtendableResource );

    /**
     * Notify the removal of a comment
     * @param strIdExtendableResource The id of the extendable resource
     *            associated with the removed comment
     */
    void deleteComment( String strIdExtendableResource );
}
