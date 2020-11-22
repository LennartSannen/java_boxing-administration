package com.defence.administration.controller;

import com.defence.administration.dto.UserDto;
import com.defence.administration.model.User;
import com.defence.administration.security.JwtTokenUtil;
import com.defence.administration.service.UserService;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    
    @Autowired
    UserService service;

    @CrossOrigin(origins = "*")    
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid UserDto dto) {
        if (service.isUser(dto)){
            return ResponseEntity.ok(jwtTokenUtil.generateToken(service.map(dto)));
        } 
        return ResponseEntity.badRequest().build();
    }

    @CrossOrigin(origins = "*")    
    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        return ResponseEntity.ok(service.findAll().stream().map(service::map).collect(Collectors.toList()));
    }

    @CrossOrigin(origins = "*")    
    @GetMapping("/users/{id}")
    public ResponseEntity getUser(@PathVariable Long id) {
        Optional<User> optional = service.findById(id);
        if (optional.isPresent()) {
            return ResponseEntity.ok(service.map(optional.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/users")
    public ResponseEntity postUser(@RequestBody @Valid UserDto dto) throws URISyntaxException {
        dto.setUsername(dto.getUsername().toLowerCase());
        if (dto.getId() != null || dto.getUsername().length() < 5 || dto.getPassword().length() < 5) {
            return ResponseEntity.badRequest().build();
        }
        if (service.findAll().stream().anyMatch(u -> u.getUsername().toLowerCase().equals(dto.getUsername()))){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.created(new URI(String.valueOf(service.create(service.map(dto)).getId()))).build();
    }

    @CrossOrigin(origins = "*")    
    @PutMapping("/users/{id}")
    public ResponseEntity putUser(@RequestBody @Valid UserDto dto, @PathVariable Long id){
       dto.setUsername(dto.getUsername().toLowerCase());
       Optional <User> optional = service.findById(id);
       if (dto.getUsername().length() < 5 || dto.getPassword().length() < 5) {
            return ResponseEntity.badRequest().build();
        }
        if(!optional.isPresent()){
            return ResponseEntity.notFound().build(); 
        }
        service.update(optional.get(), dto);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "*")    
    @DeleteMapping("/users/{id}")
    public ResponseEntity deleteMember(@PathVariable Long id){
        Optional <User> optional = service.findById(id);
        if(!optional.isPresent()){
            return ResponseEntity.notFound().build(); 
        }
        service.delete(optional.get());
        return ResponseEntity.noContent().build();
    }
}
