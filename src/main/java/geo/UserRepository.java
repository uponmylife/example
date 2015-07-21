package geo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@Repository
@Slf4j
public class UserRepository {
	private Map<String, UserDetail> map = new HashMap<>();

	@PostConstruct
	public void init() {
		map.put("geo", new UserDetail("geo", "wj", "M"));
		map.put("antonio", new UserDetail("antonio", "wy", "M"));
		map.put("pai", new UserDetail("pai", "so", "F"));
	}

	public Set<String> getUserIds() {
		randomSleep(300, 500);
		return map.keySet();
	}

	public UserDetail getUserDetail(String id) {
		randomSleep(20, 100);
		return map.get(id);
	}

	private void randomSleep(long min, long max) {
		try {
			long time = min + Math.abs(new Random().nextLong()) % (max - min);
			Thread.sleep(time);
			log.info(time + "ms sleep");
		} catch (InterruptedException e) {}
	}
}
