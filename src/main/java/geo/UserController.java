package geo;

import kamon.Kamon;
import kamon.trace.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class UserController {
	@Autowired
	private UserRepository repository;

	@RequestMapping("/users")
	public List<UserDetail> getUsers() {
		TraceContext traceContext = Kamon.tracer().newContext("trace1");
		Set<String> userIds = repository.getUserIds();
		List<UserDetail> userDetails = userIds.stream().map((id) -> {
			UserDetail userDetail = repository.getUserDetail(id);
			return userDetail;
		}).collect(Collectors.toList());
		traceContext.finish();
		return userDetails;
	}
}
