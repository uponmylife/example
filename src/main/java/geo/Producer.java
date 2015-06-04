package geo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Controller;

import java.util.Scanner;

@Controller
public class Producer implements CommandLineRunner {
	@Autowired
	@Qualifier("kafkaOutChannel")
	private MessageChannel channel;

	@Override
	public void run(String... args) throws Exception {

		Scanner scanner = new Scanner(System.in);
		while (true) channel.send(new GenericMessage<>(scanner.nextLine()));
	}
}
