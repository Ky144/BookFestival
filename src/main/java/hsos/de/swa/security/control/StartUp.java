package hsos.de.swa.security.control;

import hsos.de.swa.security.entity.User;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;

@Singleton
public class StartUp {
    @Transactional
    public void loadUsers(@Observes StartupEvent ev) {
        User.deleteAll();
        User.add("admin1", "admin1", "admin");
        User.add("admin2", "admin2", "admin");
        User.add("author1", "author1", "author");
        User.add("author2", "author2", "author");

    }
}
