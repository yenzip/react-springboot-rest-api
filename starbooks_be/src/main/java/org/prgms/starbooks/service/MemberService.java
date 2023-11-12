package org.prgms.starbooks.service;

import org.prgms.starbooks.model.member.Member;
import org.prgms.starbooks.repository.member.MemberRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public Member createMember(String name, String email, String address, String postcode) {
        // 이메일 중복 검사
        Optional<Member> existingMember = memberRepository.findByEmail(email);
        if (existingMember.isPresent()) {
            throw new DuplicateKeyException("이미 등록된 이메일입니다.");
        }

        Member member = new Member(UUID.randomUUID(), name, email, address, postcode, LocalDateTime.now(), LocalDateTime.now());
        return memberRepository.insert(member);
    }

    @Transactional
    public Member updateMember(Member member) {
        return memberRepository.update(member);
    }

    @Transactional(readOnly = true)
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member getMemberById(UUID memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
    }

    @Transactional
    public void deleteMember(UUID memberId) {
        memberRepository.deleteById(memberId);
    }
}
