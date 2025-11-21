package ie.ul.iam.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
//import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
