package geo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
	@Autowired
	private UserRepository repository;

	@RequestMapping("/users")
	public List<UserDetail> getUsers() {
		return repository.getUserIds().stream().map((id) -> repository.getUserDetail(id)).collect(Collectors.toList());
	}
}
