package ch.heigvd.amt.stoneoverflow.domain.user;

import ch.heigvd.amt.stoneoverflow.domain.IRepository;

import java.util.Optional;

public interface IUserRepository extends IRepository<User, UserId> {
    Optional<User> findByUsername(String username);
}
