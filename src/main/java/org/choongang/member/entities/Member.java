package org.choongang.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.choongang.member.constants.Authority;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Member {

    private Long seq;

    private String gid;

    private String email;

    private String job;

    private String password;

    private String userName;

    private String mobile;

    private Authority authorities;

    private String memMajor;
    private String memMinor;
}
