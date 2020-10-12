package ch.heigvd.amt.StoneOverflow.domain.user;

import ch.heigvd.amt.StoneOverflow.domain.IRepository;

import java.util.Optional;

public interface IUserRepository extends IRepository<User, UserId> {
    Optional<User> findByUsername(String username);
}
