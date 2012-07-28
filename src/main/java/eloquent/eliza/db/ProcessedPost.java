package eloquent.eliza.db;

import eloquent.eliza.facebook.Comment;
import eloquent.eliza.facebook.Post;
import eloquent.eliza.facebook.User;

/**
 * A processed post. This can be a {@link Comment} or a {@link Post}
 * @author Shantanu
 *
 */
public class ProcessedPost {
	
	/**
	 * id of the post
	 */
	private String id;
	
	/**
	 * User who posted this posted
	 */
	private User from;
	
	/**
	 * message in the post
	 */
	private String message;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getFrom() {
		return from;
	}

	public void setFrom(User from) {
		this.from = from;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
