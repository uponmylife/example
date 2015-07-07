package geo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Profile({"a", "c"})
@Component
public class ACComponet {
	@PostConstruct
	public void postConstruct() {
		System.out.println("ac");
	}
}
