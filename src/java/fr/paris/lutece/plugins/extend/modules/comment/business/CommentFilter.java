package fr.paris.lutece.plugins.extend.modules.comment.business;
/**
 * 
 * CommentFilter
 *
 */
public class CommentFilter {
	
	private Integer _nCommentState;
	private String _strSortedAttributeName; 
	private Boolean _bAscSort;
	private Boolean _bPinned;
	
	
	
	
	/**
	 * *
	 * @return the comment state set if the results must contain only the comments which are in the state specify in parameters
	 */
	public Integer getCommentState() {
		return _nCommentState;
	}
	/**
	 * 
	 * @param nCommentState set if the results must contain only the comments which are in the state specify in parameters
	 */
	public void setCommentState(Integer nCommentState) {
		this._nCommentState = nCommentState;
	}
	/**
	 * 
	 * @return the sorted attribute name
	 */
	public String getSortedAttributeName() {
		return _strSortedAttributeName;
	}
	/**
	 * 
	 * @param _strSortedAttributeName the sorted attribute name
	 */
	public void setSortedAttributeName(String _strSortedAttributeName) {
		this._strSortedAttributeName = _strSortedAttributeName;
	}
	/**
	 * 
	 * @return true if the result must be sorted by asc sort
	 */
	public Boolean getAscSort() {
		return _bAscSort;
	}
	/**
	 * 
	 * @param _bAscSort true if the result must be sorted by asc sort
	 */
	public void setAscSort(Boolean _bAscSort) {
		this._bAscSort = _bAscSort;
	}
	/**
	 * 
	 * @return return true if the results must contain only the comments pinned
	 * 				  false if the results must contain only the comments not pinned
	 * 
	 */
	public Boolean getPinned() {
		return _bPinned;
	}
	/**
	 * 
	 * @param _bPinned true if the results must contain only the comments pinned
	 * 				  false if the results must contain only the comments not pinned
	 */
	public void setPinned(Boolean _bPinned) {
		this._bPinned = _bPinned;
	}

}
