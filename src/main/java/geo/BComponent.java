package geo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("b")
@Component
public class BComponent {
	@PostConstruct
	public void postConstruct() {
		System.out.println("b");
	}
}
