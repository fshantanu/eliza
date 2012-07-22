package eloquent.eliza.processor;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import eloquent.eliza.facebook.Post;
import eloquent.eliza.facebook.User;
import eloquent.eliza.rest.FacebookHandler;

public class BirthdayProcessor implements Runnable {

	/**
	 * Facebook to process birthday events
	 */
	private FacebookHandler facebookHandler;
	
	/**
	 * Logger for logging
	 */
	private Logger logger = Logger.getLogger(getClass());


	@Override
	public void run() {
		// Get Today's Date
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd");
		String today = formatter.format(new Date());
		logger.info("Processing Birthday events for - " + today);

		// Get the user object for the logged in user
		User me = facebookHandler.getObject(facebookHandler.getUserId(), User.class);
		// get a list of friends for the user. 
		Collection<User> friends = facebookHandler.getFriendList();

		// for each user check if his birthday today.
		// if so wish the user Happy Birthday!
		for(User friend: friends){
			User user = facebookHandler.getObject(friend.getId(), User.class);
			if(user.getBirthday()!=null && 
					formatter.format(user.getBirthday()).equals(today)){

				try{
					logger.info(String.format("Wishing %s happy birthday", user.getName()));
					Post post = new Post();
					post.setFrom(me);
					post.setMessage(String.format("Happy Birthday %s !", user.getFirstName()));
					facebookHandler.postOnWall(post, user);
				}
				catch(Throwable ex){
					logger.error(String.format("Error processing birthday event " +
							"for user [%s]", user.getName()), ex);
				}
			}
		}

		logger.info("Finished Processing birthday events");
	}


	public void setFacebookHandler(FacebookHandler facebookHandler) {
		this.facebookHandler = facebookHandler;
	}

}
