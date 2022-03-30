package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.ClientUser;
import com.erikfakin.springchatsupport.repositories.ClientUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientUserService {

    @Autowired
    private ClientUserRepository clientUserRepository;

    public ClientUser findByEmail(String email) {
        return clientUserRepository.findByEmail(email);
    }

    public ClientUser save(ClientUser user) {
        return clientUserRepository.save(user);
    }
}