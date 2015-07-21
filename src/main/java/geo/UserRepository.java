package geo;

import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Repository
public class UserRepository {
	private Map<String, UserDetail> map = new HashMap<>();

	@PostConstruct
	public void init() {
		map.put("geo", new UserDetail("geo", "wj", "M"));
		map.put("antonio", new UserDetail("antonio", "wy", "M"));
		map.put("pai", new UserDetail("pai", "so", "F"));
	}

	public Set<String> getUserIds() {
		randomSleep(500);
		return map.keySet();
	}

	public UserDetail getUserDetail(String id) {
		randomSleep(100);
		return map.get(id);
	}

	private void randomSleep(long scope) {
		try { Thread.sleep(Math.abs(new Random().nextLong()) % scope); } catch (InterruptedException e) {}
	}
}
