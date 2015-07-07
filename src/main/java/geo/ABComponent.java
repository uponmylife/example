package geo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile({"a", "b"})
@Component
public class ABComponent {
	@PostConstruct
	public void postConstruct() {
		System.out.println("ab");
	}
}
