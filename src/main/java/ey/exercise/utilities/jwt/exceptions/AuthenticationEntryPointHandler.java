package ey.exercise.utilities.jwt.exceptions;

import ey.exercise.utilities.formats.HandlerMessage;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

import static ey.exercise.utilities.AppConstant.SESSION_EXCEPTION_MESSAGE;

public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint, Serializable {

  private static final long serialVersionUID = -9206078375353792395L;

  public AuthenticationEntryPointHandler() {}

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    HandlerMessage.formatMessageException(response, HttpServletResponse.SC_BAD_REQUEST, SESSION_EXCEPTION_MESSAGE);
  }
}
