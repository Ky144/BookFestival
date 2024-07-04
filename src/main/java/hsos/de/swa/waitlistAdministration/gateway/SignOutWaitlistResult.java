package hsos.de.swa.waitlistAdministration.gateway;

public enum SignOutWaitlistResult {
    //austragen von der Warteliste
    SIGN_OUT,
    //Author steht nicht auf der Warteliste
    NOT_SIGNED_IN,
    //Abmeldung ist fehlgeschlagen
    FAILED
}
