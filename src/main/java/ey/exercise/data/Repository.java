package ey.exercise.data;

import ey.exercise.data.dtos.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

@org.springframework.stereotype.Repository
@Transactional
public interface Repository extends CrudRepository<User, Long> {

  @Query("SELECT u FROM User u WHERE u.email = :vEmail")
  User findByEmail(@Param("vEmail") String vEmail);

  @Query("SELECT u FROM User u WHERE u.token = :vToken")
  User findUserByToken(@Param("vToken") String vToken);

  List<User> findAll();

  @Modifying
  @Query(
      "UPDATE User u SET u.token = :vToken, u.lastLogin = :vLastLogin,u.modified = :vModified WHERE u.email = :vEmail")
  int updateUser(
      @Param("vEmail") String vEmail,
      @Param("vToken") String vToken,
      @Param("vLastLogin") String vLastLogin,
      @Param("vModified") String vModified);

  @Modifying
  @Query("UPDATE User u SET u.token = :vTokenUpdate WHERE u.token = :vTokenSearch")
  int revocateToken(
      @Param("vTokenSearch") String vTokenSearch, @Param("vTokenUpdate") String vTokenUpdate);
}
