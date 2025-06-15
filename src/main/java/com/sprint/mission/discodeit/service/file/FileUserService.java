package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {

    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser(User user) {
        userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(UUID userId, String updatedText) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setUserName(updatedText);
            user.setUpdatedAt(System.currentTimeMillis());
            userRepository.save(user);
        } else {
            throw new RuntimeException("유저를 찾을 수 없습니다: " + userId);
        }
    }

    @Override
    public void deleteUser(UUID userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User getUserById(UUID userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new RuntimeException("유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public void findUsersByKeyword(String keyword) {
        System.out.println("[" + keyword + "] 키워드로 검색한 결과: ");
        for (User user : userRepository.findAll()) {
            if(user.getUserName().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(user);
            }
        }
    }

    @Override
    public void updateUserStatus (UUID userId, UserStatus status) {
        userRepository.findById(userId)
                .ifPresent(user -> {
                    user.setUserStatus(status);
                    user.setUpdatedAt(System.currentTimeMillis());
                    userRepository.save(user);
                });
    }

    @Override
    public List<User> findUsersByStatus(UserStatus status) {
        List<User> result = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            // 상태가 일치하는 유저만 결과에 추가
            if(user.getUserStatus().equals(status)){
                result.add(user);
            }
        }
        return result;
    }
}
