package ey.exercise.usecase.dosignin;

import ey.exercise.Controller;
import ey.exercise.ServiceFacade;
import ey.exercise.data.Repository;
import ey.exercise.data.dtos.User;
import ey.exercise.usecases.dosignin.DoSignInUseCase;
import ey.exercise.usecases.dosignin.models.DoSignInResponse;
import ey.exercise.usecases.finduserbyemail.FindUserByEmailUseCase;
import ey.exercise.usecases.getusers.GetUsersUseCase;
import ey.exercise.utilities.exceptions.ExceptionHandlerResponse;
import ey.exercise.utilities.jwt.JwtTokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static ey.exercise.usecase.UserStubs.getUser;
import static ey.exercise.usecase.dosignin.DoSignInStubs.getDoSignInRequest;
import static ey.exercise.usecase.dosignin.DoSignInStubs.getDoSignInResponse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
public class DoSignInTest {

  @Mock private GetUsersUseCase getUsersUseCase;
  @Mock private FindUserByEmailUseCase findUserByEmailUseCase;
  @Mock private Repository globalLogicRepository;
  @MockBean AuthenticationManager authenticationManager;
  @MockBean JwtTokenProvider jwtTokenProvider;

  private Controller globalLogicController;

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(16);
  }

  @Before
  public void setup() throws IOException {

    final DoSignInUseCase doSignInUseCase =
        new DoSignInUseCase(
            authenticationManager, jwtTokenProvider, globalLogicRepository, passwordEncoder());

    final ServiceFacade globalLogicServiceFacade =
        new ServiceFacade(doSignInUseCase, getUsersUseCase, findUserByEmailUseCase);

    globalLogicController = new Controller(globalLogicServiceFacade);

    final User user =
        new User(
            getUser().getId(),
            getUser().getName(),
            getUser().getEmail(),
            getUser().getPassword(),
            getUser().getPhones(),
            getUser().getCreated(),
            getUser().getModified(),
            getUser().getLastLogin(),
            getUser().getToken(),
            getUser().getRoles(),
            getUser().isActive());
    when(globalLogicRepository.save(any(User.class))).thenReturn(user);
    when(globalLogicRepository.findByEmail(any(String.class))).thenReturn(user);
    when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
        .thenReturn(
            new UsernamePasswordAuthenticationToken(
                getDoSignInRequest().getEmail(), getDoSignInRequest().getPassword()));
    when(globalLogicRepository.updateUser(
            any(String.class), any(String.class), any(String.class), any(String.class)))
        .thenReturn(1);
    when(jwtTokenProvider.createToken(any(), any())).thenReturn(getDoSignInResponse().getToken());
  }

  @Test
  public void itShouldDoSignInWhenApiSuccess() throws IOException {
    assertNotNull(getDoSignInRequest());

    final DoSignInResponse doSignInResponse = globalLogicController.doSignIn(getDoSignInRequest());

    assertNotNull(getDoSignInResponse());
    assertNotNull(doSignInResponse);
    assertEquals(
        doSignInResponse,
        new DoSignInResponse(
            getDoSignInResponse().getId(),
            getDoSignInResponse().getCreated(),
            getDoSignInResponse().getModified(),
            getDoSignInResponse().getLastLogin(),
            getDoSignInResponse().getToken(),
            getDoSignInResponse().isActive()));
  }

  @Test
  public void itShouldDoSignInWhenApiFailure() throws IOException {
    assertNotNull(getDoSignInRequest());
    try {
      globalLogicController.doSignIn(null);
    } catch (NullPointerException ex) {
      assertEquals(new ExceptionHandlerResponse<>(null).getMessage(), ex.getMessage());
    }
  }
}
