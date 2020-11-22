package com.defence.administration.controller;

import com.defence.administration.dto.MemberDto;
import com.defence.administration.model.Member;
import com.defence.administration.service.MemberService;
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
public class MemberController {
    
    @Autowired
    private MemberService service;
    
    @CrossOrigin(origins = "*")
    @GetMapping("/members")
    public ResponseEntity getAllMembers() {
        return ResponseEntity.ok(service.findAll().stream().map(service::map).collect(Collectors.toList()));
    }

    @CrossOrigin(origins = "*")    
    @GetMapping("/members/{id}")
    public ResponseEntity getMember(@PathVariable Long id){
        Optional <Member> member = service.findById(id);
        if (member.isPresent()){
            return ResponseEntity.ok(service.map(member.get()));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @CrossOrigin(origins = "*")    
    @PostMapping("/members")
    public ResponseEntity postMember(@RequestBody @Valid MemberDto dto) throws URISyntaxException {
        if (dto.getId() != null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.created(new URI(String.valueOf(service.saveAndFlush(service.map(dto)).getId()))).build();
    }

    @CrossOrigin(origins = "*")
    @PutMapping("/members/{id}")
    public ResponseEntity putMember(@RequestBody @Valid MemberDto dto, @PathVariable Long id){
        Optional <Member> member = service.findById(id);
        if(!member.isPresent()){
            return ResponseEntity.notFound().build(); 
        }
        service.update(member.get(), dto);
        return ResponseEntity.noContent().build();
    }

    @CrossOrigin(origins = "*")    
    @DeleteMapping("/members/{id}")
    public ResponseEntity deleteMember(@PathVariable Long id){
        Optional <Member> member = service.findById(id);
        if(!member.isPresent()){
            return ResponseEntity.notFound().build(); 
        }
        service.delete(member.get());
        return ResponseEntity.noContent().build();
    }
}
