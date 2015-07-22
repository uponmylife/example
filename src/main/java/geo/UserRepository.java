package geo;

import kamon.annotation.EnableKamon;
import kamon.annotation.Segment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

@EnableKamon
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

	@Segment(name = "getUserIds", category = "database", library = "jdbc")
	public Set<String> getUserIds() {
		randomSleep(300, 500);
		return map.keySet();
	}

	@Segment(name = "getUserDetail", category = "database", library = "jdbc")
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
