package org.choongang.global.tests;

import org.choongang.member.MemberInfo;
import org.choongang.member.entities.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MockSecurityContextFactory implements WithSecurityContextFactory<MockMember> {
    @Autowired
    private PasswordEncoder encoder;

    @Override
    public SecurityContext createSecurityContext(MockMember mockMember) {

        Member member = new Member();
        member.setSeq(mockMember.seq());
        member.setEmail(mockMember.email());
        member.setPassword(encoder.encode(mockMember.password()));
        member.setUserName(mockMember.userName());
        member.setGid(mockMember.gid());
        member.setJob(mockMember.job());
        member.setMobile(mockMember.mobile());

        List<SimpleGrantedAuthority> _authorities = List.of(new SimpleGrantedAuthority(mockMember.authority().name()));

        MemberInfo memberInfo = MemberInfo.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .authorities(_authorities)
                .member(member)
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(memberInfo, null, _authorities);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);

        return context;
    }
}
