package com.rdbac.rdbac.Service;

import com.rdbac.rdbac.Dto.AuthDto;

public interface AuthService {
    String customAuth(AuthDto authDto);
    String signup(AuthDto authDto);
}

