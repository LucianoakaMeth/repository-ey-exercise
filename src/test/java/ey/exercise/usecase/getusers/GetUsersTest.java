package ey.exercise.usecase.getusers;

import ey.exercise.ProtectedController;
import ey.exercise.ServiceFacade;
import ey.exercise.data.Repository;
import ey.exercise.data.dtos.User;
import ey.exercise.usecases.dosignin.DoSignInUseCase;
import ey.exercise.usecases.finduserbyemail.FindUserByEmailUseCase;
import ey.exercise.usecases.getusers.GetUsersUseCase;
import ey.exercise.usecases.getusers.models.GetUsersResponse;
import ey.exercise.usecase.UserStubs;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static ey.exercise.usecase.getusers.GetUsersStubs.getGetUsersResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class GetUsersTest {

  @Mock private DoSignInUseCase doSignInUseCase;
  @Mock private FindUserByEmailUseCase findUserByEmailUseCase;
  @Mock private Repository globalLogicRepository;

  private ProtectedController globalLogicProtectedController;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(16);
  }

  @Before
  public void setup() throws IOException {

    final GetUsersUseCase getUsersUseCase = new GetUsersUseCase(globalLogicRepository);

    final ServiceFacade globalLogicServiceFacade =
        new ServiceFacade(doSignInUseCase, getUsersUseCase, findUserByEmailUseCase);

    globalLogicProtectedController = new ProtectedController(globalLogicServiceFacade);

    final List<User> listUser = new ArrayList<>();
    listUser.add(
        new User(
            UserStubs.getUser().getId(),
            UserStubs.getUser().getName(),
            UserStubs.getUser().getEmail(),
            UserStubs.getUser().getPassword(),
            UserStubs.getUser().getPhones(),
            UserStubs.getUser().getCreated(),
            UserStubs.getUser().getModified(),
            UserStubs.getUser().getLastLogin(),
            UserStubs.getUser().getToken(),
            UserStubs.getUser().getRoles(),
            UserStubs.getUser().isActive()));

    when(globalLogicRepository.findAll()).thenReturn(listUser);
  }

  @Test
  public void itShouldDoGetUsersWhenApiSuccess() throws IOException {
    final GetUsersResponse getUsersResponse = globalLogicProtectedController.getUsers();

    assertNotNull(getGetUsersResponse());
    assertNotNull(getUsersResponse);
    assertEquals(getUsersResponse, getGetUsersResponse());
  }
}
