package org.prgms.starbooks.controller.dto.member;

import org.prgms.starbooks.model.member.Member;

import java.time.LocalDateTime;
import java.util.UUID;

public record UpdateMemberRequest(UUID memberId, String name, String email, String address, String postcode) {
    public Member toMember() {
        return new Member(memberId, name, email, address, postcode, LocalDateTime.now(), LocalDateTime.now());
    }
}
