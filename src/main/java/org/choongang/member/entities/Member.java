package org.choongang.member.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Transient;
import lombok.Data;
import org.choongang.thesis.entities.Interests;

import java.util.List;

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

    private String memMajor;//전공

    private String memMinor;//부전공

    @Transient
    private List<Interests> interests;

    private List<Authorities> authorities;
}
