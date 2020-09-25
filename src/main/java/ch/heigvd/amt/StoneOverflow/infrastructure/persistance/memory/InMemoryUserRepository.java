package ch.heigvd.amt.StoneOverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.StoneOverflow.domain.user.IUserRepository;
import ch.heigvd.amt.StoneOverflow.domain.user.User;
import ch.heigvd.amt.StoneOverflow.domain.user.UserId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryUserRepository extends InMemoryRepository<User, UserId> implements IUserRepository {
    @Override
    public void save(User entity) {
        //todo: - synchronized (concurrency issue)
        //      - verify there is only one user with that username
        super.save(entity);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .collect(Collectors.toList());
        if (users.size() < 1)
            return Optional.empty();

        //todo: throw error if more than one user is found
        return Optional.of(users.get(0).deepClone());
    }
}
