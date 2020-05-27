package org.xc.elasticjob;

import org.apache.curator.test.TestingServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class DemoApplication {
	static TestingServer testingServer;
	public static void main(String[] args) {

		try {
			testingServer = new TestingServer(2181, new File(String.format("target/test_zk_data/%s/", System.nanoTime())));
			// CHECKSTYLE:OFF
		} catch (final Exception ex) {
			// CHECKSTYLE:ON
			ex.printStackTrace();
		} finally {
			Runtime.getRuntime().addShutdownHook(new Thread() {

				@Override
				public void run() {
					try {
						Thread.sleep(1000L);
						testingServer.close();
					} catch (final InterruptedException | IOException ex) {
					}
				}
			});
		}
		SpringApplication.run(DemoApplication.class, args);
	}

}
