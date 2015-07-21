package geo;

import kamon.trace.Tracer;
import kamon.util.Supplier;
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
		return Tracer.withNewContext("trace2", true, (Supplier<List<UserDetail>>) () -> {
			Supplier<Set<String>> getUserIds = () -> repository.getUserIds();
			Set<String> userIds = Tracer.currentContext().withNewSegment("getUserIds", "database", "jdbc", getUserIds);

			List<UserDetail> userDetails = userIds.stream().map((id) -> {
				Supplier<UserDetail> getDetail = () -> repository.getUserDetail(id);
				return Tracer.currentContext().withNewSegment("getDetail", "database", "jdbc", getDetail);
			}).collect(Collectors.toList());

			return userDetails;
		});
	}
}
