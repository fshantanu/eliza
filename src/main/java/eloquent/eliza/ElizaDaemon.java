package eloquent.eliza;

import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Daemon to load up the spring configurations and 
 * initialize the processes
 * 
 * @author Shantanu
 *
 */
public class ElizaDaemon {
	
	/**
	 * Logger for logging
	 */
	private static Logger log = Logger.getLogger(ElizaDaemon.class);
	
	public static void main(String[] args) throws Exception{
		
		final CountDownLatch exitLatch = new CountDownLatch(1);
    // Load the Spring configuration.
    final ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
        "classpath:/spring/root-context.xml");
    // The daemon and required timers are created in the spring context already.
    log.info("Eliza Daemon started");

    // Register the shutdown hook to exit cleanly.
    Runtime.getRuntime().addShutdownHook(new Thread() {
      @Override
      public void run() {
        try {
          log.info("Eliza Daemon shutdown started.");
          appContext.close();
          log.info("Eliza Daemon shutdown complete.");
        }
        catch (Throwable ex) {
          log.warn("Could not shutdown cleanly. Continuing exit process.", ex);
        }
        finally {
          exitLatch.countDown();
        }
      }
    });

    // Wait for a shutdown request.
    exitLatch.await();	
	}
}
