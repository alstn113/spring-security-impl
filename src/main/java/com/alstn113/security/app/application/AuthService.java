package com.alstn113.security.app.application;

import com.alstn113.security.app.application.request.LoginRequest;
import com.alstn113.security.app.application.request.RegisterRequest;
import com.alstn113.security.app.application.response.MemberInfoResponse;
import com.alstn113.security.app.application.response.TokenResponse;
import com.alstn113.security.app.domain.Member;
import com.alstn113.security.app.domain.MemberRepository;
import com.alstn113.security.app.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public void register(RegisterRequest request) {
        if (memberRepository.existsByUsername(request.username())) {
            throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
        }

        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = new Member(request.username(), encodedPassword, Role.valueOf(request.role()));

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public TokenResponse login(LoginRequest request) {
        Member member = memberRepository.findByUsername(request.username())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        String token = tokenProvider.generateToken(member.getId());

        return new TokenResponse(token);
    }

    @Transactional(readOnly = true)
    public MemberInfoResponse getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다. 사용자 식별자: %d".formatted(memberId)));

        return new MemberInfoResponse(member.getId(), member.getUsername(), member.getRole());
    }
}
