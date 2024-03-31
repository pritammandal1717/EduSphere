package org.education.service;

import lombok.RequiredArgsConstructor;
import org.education.dto.UserDetailsImpl;
import org.education.repository.StudentRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final StudentRepository studentRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return studentRepository.findByEmail(email)
                .map(UserDetailsImpl::new)
                .orElseThrow(()-> new UsernameNotFoundException("Student not found"));
    }
}
