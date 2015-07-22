package geo;

import kamon.annotation.EnableKamon;
import kamon.annotation.Trace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@EnableKamon
@RestController
public class UserController {
	@Autowired
	private UserRepository repository;

	@Trace("trace3")
	@RequestMapping("/users")
	public List<UserDetail> getUsers() {
		return repository.getUserIds().stream().map((id) -> repository.getUserDetail(id)).collect(Collectors.toList());
	}
}
