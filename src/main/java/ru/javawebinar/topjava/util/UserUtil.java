package ru.javawebinar.topjava.util;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserUtil {
    public static final User ADMIN = new User(1, "Admin", "admin@example.com","admin", 2500, true, Collections.singleton(Role.ADMIN));
    public static final User USER1 = new User(2, "Alice", "alice@example.com","user2", 1500, true, Collections.singleton(Role.USER));
    public static final User USER2 = new User(3, "Bob", "bob@example.com","user3", 2000, false, Collections.singleton(Role.USER));

    public static final List<User> users = Arrays.asList(ADMIN, USER1, USER2);
}
