package baitap10.repository;

import baitap10.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
 
    User findByUsernameAndPassword(String username, String password);
}
