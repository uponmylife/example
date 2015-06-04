package geo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.integration.kafka.support.KafkaHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
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
		while (true) {
			String payload = scanner.next();
			Message<String> message = MessageBuilder.withPayload(payload)
					.setHeader(KafkaHeaders.MESSAGE_KEY, payload)
					.build();
			channel.send(message);
		}
	}
}
