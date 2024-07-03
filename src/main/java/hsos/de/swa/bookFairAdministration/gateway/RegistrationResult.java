package hsos.de.swa.bookFairAdministration.gateway;

public enum RegistrationResult {
    //Anmeldung war erfolgreich
    REGISTERED,
    //AUthor ist bereits angemeldet
    ALREADY_SIGNEDIN,
    //Author wurde auf Warteliste gesetzt
    WAITLISTED,
    //Registrierung ist fehlgeschlagen
    FAILED
}
