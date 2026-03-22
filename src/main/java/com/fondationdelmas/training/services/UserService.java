package com.fondationdelmas.training.services;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.fondationdelmas.training.configs.UserMapper;
import com.fondationdelmas.training.dtos.CreateUserRequestDTO;
import com.fondationdelmas.training.dtos.UserResponseDTO;
import com.fondationdelmas.training.entities.UserEntity;
import com.fondationdelmas.training.repositories.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import static net.logstash.logback.argument.StructuredArguments.kv;
import io.micrometer.observation.annotation.Observed;

@Service
@Transactional
@Slf4j
public class UserService {
        private final UserRepository repository;
        private final UserMapper userMapper;

        public UserService(UserRepository repository, UserMapper userMapper) {
                this.repository = repository;
                this.userMapper = userMapper;
        }


        /**
         * 
         * @param request
         * @return
         */
        @Observed(name = "users.service.create")
        public UserResponseDTO create(CreateUserRequestDTO request) {
        
                long start = System.currentTimeMillis();

                log.info("User creation requested",
                        kv("action", "USER_CREATE"),
                        kv("email", request.email())
                );

                if (repository.existsByEmail(request.email())) {
                log.warn("User creation failed - email already exists",
                        kv("action", "USER_CREATE_FAILED"),
                        kv("email", request.email())
                );
                throw new IllegalArgumentException("Email already used");
                }

                 UserEntity user = repository.save(userMapper.toEntity(request));

                log.info("User created successfully",
                        kv("action", "USER_CREATED"),
                        kv("userId", user.getId()),
                        kv("duration_ms", System.currentTimeMillis() - start)
                );

                return userMapper.toDtoResponse(user);
       }


       /**
        * 
        * @return
        */
        public List<UserResponseDTO> findAll() {
                log.info("Fetching all users",
                        kv("action", "USER_LIST")
                );
                return userMapper.toDtoList(repository.findAll());
        }


        /**
         * 
         * @param id
         * @return
         */
        public UserResponseDTO findById(Long id) {
                log.info("Fetching user by id",
                        kv("action", "USER_GET"),
                        kv("userId", id)
                );

                UserEntity user = repository.findById(id)
                        .orElseThrow(() -> {
                        log.warn("User not found",
                                kv("action", "USER_NOT_FOUND"),
                                kv("userId", id)
                        );
                        return new NoSuchElementException("User not found");
                        });

                return userMapper.toDtoResponse(user);
        }


        /**
         * 
         * @param id
         * @param request
         * @return
         */
        public UserResponseDTO update(Long id, CreateUserRequestDTO request) {

                log.info("User update requested",
                        kv("action", "USER_UPDATE"),
                        kv("userId", id)
                );

                UserEntity user = repository.findById(id)
                        .orElseThrow(() -> new NoSuchElementException("User not found"));

                user.setFirstName(request.firstName());
                user.setLastName(request.lastName());

                log.info("User updated successfully",
                        kv("action", "USER_UPDATED"),
                        kv("userId", id)
                );

                return userMapper.toDtoResponse(user);
        }


        /**
         * 
         * @param id
         */
        public void delete(Long id) {

                log.info("User deletion requested",
                        kv("action", "USER_DELETE"),
                        kv("userId", id)
                );

                repository.deleteById(id);

                log.info("User deleted successfully",
                        kv("action", "USER_DELETED"),
                        kv("userId", id)
                );
        }
}
