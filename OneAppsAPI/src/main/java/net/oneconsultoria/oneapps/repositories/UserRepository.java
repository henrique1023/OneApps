package net.oneconsultoria.oneapps.repositories;

import net.oneconsultoria.oneapps.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from usuario u where u.nome ilike :userName")
    User findByUsername(@Param("userName") String userName);

    @Query("select u from usuario u where u.nome ilike :userName or u.email ilike :userName")
    User findByUsernameOrEmail(@Param("userName") String username);
}
