package ch.heigvd.amt.stoneoverflow.infrastructure.persistance.memory;

import ch.heigvd.amt.stoneoverflow.domain.user.IUserRepository;
import ch.heigvd.amt.stoneoverflow.domain.user.User;
import ch.heigvd.amt.stoneoverflow.domain.user.UserId;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.DataCorruptionException;
import ch.heigvd.amt.stoneoverflow.infrastructure.persistance.exception.IntegrityConstraintViolationException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class InMemoryUserRepository extends InMemoryRepository<User, UserId> implements IUserRepository {
    @Override
    public void save(User entity) {
        synchronized (entity.getUsername()) {
            if (findByUsername(entity.getUsername()).isPresent())
                throw new IntegrityConstraintViolationException("Cannot save user. Integrity constraint violation: username must be unique");

            super.save(entity);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> users = findAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .collect(Collectors.toList());
        if (users.size() < 1)
            return Optional.empty();
        else if (users.size() > 1)
            throw new DataCorruptionException("Data store is corrupted. More than one user with the same username");

        return Optional.of(users.get(0).deepClone());
    }
}
