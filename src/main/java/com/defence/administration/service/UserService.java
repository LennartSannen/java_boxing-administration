package com.defence.administration.service;

import com.defence.administration.dto.UserDto;
import com.defence.administration.model.User;
import com.defence.administration.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public User create(User entity) {
        return repository.saveAndFlush(entity);
    }

    public void delete(User entity) {
        repository.delete(entity);
    }

    public void update(User entity, UserDto dto) {
        entity.setUsername(dto.getUsername());
        entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        repository.save(entity);
    }
    
    public Boolean isUser(UserDto dto){
        Optional <User> optional = repository.findByUsernameIgnoreCase(dto.getUsername());
        if (optional.isPresent()) {
            return bCryptPasswordEncoder.matches(dto.getPassword(), optional.get().getPassword());
        }
        return false;
    }
    
    public User findUserByUsername(String username){
        return repository.findByUsernameIgnoreCase(username).isPresent() ? repository.findByUsernameIgnoreCase(username).get() : null;
    }
   
    public User map(UserDto dto) {
        User entity = dto.getId() == null ? new User() : repository.findById(dto.getId()).orElse(new User());
        if (entity.getPassword() == null) {
            entity.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        }
        entity.setUsername(dto.getUsername());
        return entity;
    }

    public UserDto map(User entity) {
        UserDto dto = new UserDto();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setPassword(entity.getPassword());
        return dto;
    }

}
