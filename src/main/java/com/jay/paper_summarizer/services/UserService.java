package com.jay.paper_summarizer.services;

import com.jay.paper_summarizer.dto.UserDTO;
import com.jay.paper_summarizer.models.User;
import com.jay.paper_summarizer.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepo;
    private final ModelMapper modelMapper;


    public UserDTO registerUser(UserDTO userDTO){

        User user = User.builder()
                .email(userDTO.getEmail())
                .password(userDTO.getPassword())
                .build();

        User savedUser = userRepo.save(user);

        return modelMapper.map(savedUser, UserDTO.class);


    }


    public void loginUser(UserDTO userDTO) {
    }
}
