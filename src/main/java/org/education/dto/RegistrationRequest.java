package org.education.dto;

import lombok.Data;
import org.education.entity.Role;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class RegistrationRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<Role> roles;
    private String phoneno;
    private String currentdegree;
    private MultipartFile profileimage;
}
