package eloquent.eliza.core;

import org.junit.Test;

import junit.framework.Assert;

/**
 * Test case for {@link Eliza}
 * @author Shantanu
 *
 */
public class ElizaTestCase {
	
	private Eliza eliza = new Eliza();
	
	@Test
	public void testGetReply(){
		String reply = eliza.getReply("How are you?");
		Assert.assertNotNull(reply);
	}

}
