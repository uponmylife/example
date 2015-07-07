package geo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile("a")
@Component
public class AComponent {
	@PostConstruct
	public void postConstruct() {
		System.out.println("a");
	}
}
