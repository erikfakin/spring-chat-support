package com.erikfakin.springchatsupport.services;

import com.erikfakin.springchatsupport.entities.ClientUser;

public interface ClientUserService {
    ClientUser findByEmail(String email);

    ClientUser save(ClientUser user);
}
