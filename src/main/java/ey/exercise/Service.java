package ey.exercise;

import ey.exercise.usecases.dosignin.models.DoSignInResponse;
import ey.exercise.usecases.getusers.models.GetUsersResponse;
import ey.exercise.usecases.dosignin.models.DoSignInRequest;
import ey.exercise.usecases.finduserbyemail.models.FindUserByEmailRequest;
import ey.exercise.usecases.finduserbyemail.models.FindUserByEmailResponse;

public interface Service {

  DoSignInResponse doSignIn(DoSignInRequest doSignInRequest);

  GetUsersResponse getUsers();

  FindUserByEmailResponse findUserByEmail(FindUserByEmailRequest findUserByEmailRequest);
}
