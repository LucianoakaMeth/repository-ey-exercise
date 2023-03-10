package ey.exercise.usecase.finduserbyemail;

import ey.exercise.ProtectedController;
import ey.exercise.ServiceFacade;
import ey.exercise.data.Repository;
import ey.exercise.data.dtos.User;
import ey.exercise.usecases.dosignin.DoSignInUseCase;
import ey.exercise.usecases.finduserbyemail.FindUserByEmailUseCase;
import ey.exercise.usecases.finduserbyemail.models.FindUserByEmailResponse;
import ey.exercise.usecases.getusers.GetUsersUseCase;
import ey.exercise.utilities.exceptions.ExceptionHandlerResponse;
import ey.exercise.usecase.UserStubs;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static ey.exercise.usecase.finduserbyemail.FindUserByEmailStubs.getFindUserByEmailRequest;
import static ey.exercise.usecase.finduserbyemail.FindUserByEmailStubs.getFindUserByEmailResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class FindUserByEmailTest {

  @Mock private DoSignInUseCase doSignInUseCase;
  @Mock private GetUsersUseCase getUsersUseCase;
  @Mock private Repository globalLogicRepository;

  private ProtectedController globalLogicProtectedController;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(16);
  }

  @Before
  public void setup() throws IOException {

    final FindUserByEmailUseCase findUserByEmailUseCase =
        new FindUserByEmailUseCase(globalLogicRepository);

    final ServiceFacade globalLogicServiceFacade =
        new ServiceFacade(doSignInUseCase, getUsersUseCase, findUserByEmailUseCase);

    globalLogicProtectedController = new ProtectedController(globalLogicServiceFacade);

    when(globalLogicRepository.findByEmail(any(String.class)))
        .thenReturn(
            new User(
                UserStubs.getUser().getId(),
                UserStubs.getUser().getCreated(),
                UserStubs.getUser().getModified(),
                UserStubs.getUser().getLastLogin(),
                UserStubs.getUser().getToken(),
                UserStubs.getUser().isActive()));
  }

  @Test
  public void itShouldFindUserByEmailWhenApiSuccess() throws IOException {
    assertNotNull(getFindUserByEmailRequest());

    final FindUserByEmailResponse findUserByEmailResponse =
        globalLogicProtectedController.findUserByEmail(getFindUserByEmailRequest());

    assertNotNull(getFindUserByEmailResponse());
    assertNotNull(findUserByEmailResponse);
    assertEquals(findUserByEmailResponse, getFindUserByEmailResponse());
  }

  @Test
  public void itShouldFindUserByEmailWhenApiFailure() throws IOException {
    assertNotNull(getFindUserByEmailRequest());
    try {
      globalLogicProtectedController.findUserByEmail(null);
    } catch (NullPointerException ex) {
      Assert.assertEquals(new ExceptionHandlerResponse<>(null).getMessage(), ex.getMessage());
    }
  }
}
