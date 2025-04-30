package com.alstn113.security.app.application.response;

import com.alstn113.security.app.domain.Role;

public record MemberInfoResponse(
        Long memberId,
        String username,
        Role role
) {
}
