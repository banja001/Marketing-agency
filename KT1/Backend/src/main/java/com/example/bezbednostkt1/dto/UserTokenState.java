package com.example.bezbednostkt1.dto;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class UserTokenState {
    private String accessToken;
    private Long expiresIn;

    public UserTokenState() {
        this.accessToken = null;
        this.expiresIn = null;
    }
}
