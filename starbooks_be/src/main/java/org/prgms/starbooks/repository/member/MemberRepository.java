package org.prgms.starbooks.repository.member;

import org.prgms.starbooks.model.member.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberRepository {
    Member insert(Member member);

    Member update(Member member);

    List<Member> findAll();

    Optional<Member> findById(UUID memberId);

    Optional<Member> findByEmail(String email);

    void deleteAll();

    void deleteById(UUID memberId);
}
