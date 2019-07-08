package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.UsersUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private Map<Integer, User> repositoryUser = new ConcurrentHashMap<>();

    {
        UsersUtil.USERS.stream()
                .sorted(Comparator.comparing(User::getName).thenComparing(User::getRegistered))
                .forEach(this::save);
    }
    private Integer maxId = UsersUtil.USERS.stream().max(Comparator.comparingInt(User::getId)).get().getId();

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            if (maxId == null) {
                maxId = 0;
            }
            int userId = maxId++;
            user.setId(userId);
            repositoryUser.put(userId, user);
            return user;
        }
        return repositoryUser.putIfAbsent(user.getId(), user);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repositoryUser.remove(id) != null;
    }

//    @Override
//    public User save(User user) {
//        log.info("save {}", user);
//        return user;
//    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repositoryUser.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return new ArrayList<>(repositoryUser.values());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        List<User> userWithEmail = repositoryUser.values().stream()
                .filter(user -> user.getEmail() == email)
                .limit(1)
                .collect(Collectors.toList());
        return userWithEmail != null ? userWithEmail.get(0) : null;
    }
}
