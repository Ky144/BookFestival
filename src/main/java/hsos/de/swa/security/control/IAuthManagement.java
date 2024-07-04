package hsos.de.swa.security.control;

public interface IAuthManagement {
    public boolean authenticate(String username, String password);
    public String generateToken(String username);
}
