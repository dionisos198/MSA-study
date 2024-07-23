package org.example.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.ResponseCookie;

@Getter
public class TokenResponseWithUserIdDto {
    private String grantType;
    private String accessToken;
    private String refreshToken;
    private Long accessTokenExpirationTime;
    private String loginId;
    private String memberId;

    @Builder
    public TokenResponseWithUserIdDto(String grantType,String accessToken,String refreshToken, Long accessTokenExpirationTime,
            String loginId,String memberId){
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.accessTokenExpirationTime = accessTokenExpirationTime;
        this.loginId = loginId;
        this.memberId = memberId;
    }
}
