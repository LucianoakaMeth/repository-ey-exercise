package ey.exercise.utilities.jwt.exceptions;

import ey.exercise.utilities.formats.HandlerMessage;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

import static ey.exercise.utilities.AppConstant.ACCESS_DENIED_EXCEPTION_MESSAGE;

public class CustomAccessDeniedHandler implements AccessDeniedHandler, Serializable {

  private static final long serialVersionUID = -6859297070553441327L;

  public CustomAccessDeniedHandler() {}

  @Override
  public void handle(
      HttpServletRequest request, HttpServletResponse response, AccessDeniedException arg2)
      throws IOException {
    HandlerMessage.formatMessageException(
        response, HttpServletResponse.SC_UNAUTHORIZED, ACCESS_DENIED_EXCEPTION_MESSAGE);
  }
}
