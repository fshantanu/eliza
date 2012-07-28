package eloquent.eliza.processor;


import java.util.Collection;
import org.apache.log4j.Logger;


import eloquent.eliza.core.Eliza;
import eloquent.eliza.db.ProcessedPost;
import eloquent.eliza.db.ProcessedPostDao;
import eloquent.eliza.facebook.Comment;
import eloquent.eliza.facebook.Post;
import eloquent.eliza.facebook.User;
import eloquent.eliza.rest.FacebookHandler;

public class PostProcessor implements Runnable {

	/**
	 * logger for logging
	 */
	private Logger logger = Logger.getLogger(getClass());

	/**
	 * facebook object
	 */
	private FacebookHandler facebookHandler;
	
	/**
	 * Eliza to generate replies for the post
	 */
	private Eliza eliza;
	
	/**
	 * Dao for processed posts
	 */
	private ProcessedPostDao processedPostDao;

	@Override
	public void run() {

		logger.info("Starting processing posts");

		// Read feed from the user's wall
		Collection<Post> feed = facebookHandler.getFeed();
		for (Post post : feed) {

			try {
				// process the message in the post
				String postReply = processEntity(post.getId(), post.getMessage(),
						post.getFrom());
				if (postReply != null) {
					facebookHandler.commentOnPost(createComment(post, postReply));
				}

				// Get a list of comments for the post
				Collection<Comment> comments = facebookHandler.getComments(post);
				for (Comment comment : comments) {
					// process the message in the comment
					String commentReply = processEntity(comment.getId(),
							comment.getMessage(), comment.getFrom());
					if (commentReply != null) {
						String personalisedReply = String.format("@%s: %s", comment
								.getFrom().getName(), commentReply);
						facebookHandler
								.commentOnPost(createComment(post, personalisedReply));
					}
				}
			} catch (Exception ex) {
				logger.error(String.format("Error processing post [%s]. The post will"
						+ "be processed in the next iteration.", post.getMessage()), ex);
			}
		}
	
		logger.info("finished processing all posts");
	}

	private Comment createComment(Post post, String message) {
		Comment comment = new Comment();
		comment.setPost(post);
		comment.setMessage(message);
		return comment;
	}

	private String processEntity(String id, String message, User from) {

		ProcessedPost post = new ProcessedPost();
		post.setId(id);
		post.setFrom(from);
		post.setMessage(message);
		
		// This is the id for eloquent.eliza
		if (message == null || processedPostDao.contains(post.getId())
				|| from.getId().equals("100002508051477"))
			return null;

		processedPostDao.insert(post);
		return eliza.getReply(message);
	}

	/**
	 * Set Eliza
	 * 
	 * @param eliza
	 */
	public void setEliza(Eliza eliza) {
		this.eliza = eliza;
	}

	/**
	 * Facebook handler
	 * 
	 * @param facebookHandler
	 */
	public void setFacebookHandler(FacebookHandler facebookHandler) {
		this.facebookHandler = facebookHandler;
	}

	public void setProcessedPostDao(ProcessedPostDao processedPostDao) {
		this.processedPostDao = processedPostDao;
	}

}
