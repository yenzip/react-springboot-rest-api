package org.prgms.starbooks.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.prgms.starbooks.model.member.Member;
import org.prgms.starbooks.repository.member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Transactional
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void tearDown() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원을 저장(생성)할 수 있다.")
    void createMember() {
        // when
        Member member = memberService.createMember("test", "test@gmail.com", "서울시 노원구", "000111");

        // then
        Optional<Member> findMember = memberRepository.findById(member.getMemberId());
        assertThat(findMember).isPresent();
    }

    @Test
    @DisplayName("이미 등록된 이메일로 회원을 저장할 때, 에러 메세지를 던진다.")
    void createMemberWithDuplicateEmail() {
        // given
        String duplicateEmail = "test@gmail.com";
        memberService.createMember("test", duplicateEmail, "서울시 노원구", "000111");

        // then
        assertThatThrownBy(() -> memberService.createMember("test2", duplicateEmail, "서울시 노원구", "000111"))
                .isInstanceOf(DuplicateKeyException.class)
                .hasMessage("이미 등록된 이메일입니다.");
    }

    @Test
    @DisplayName("회원 정보(주소)를 수정할 수 있다.")
    void updateMember() {
        // given
        Member member = memberService.createMember("test", "test@gmail.com", "서울시 노원구", "000111");

        // when
        member.setAddress("서울 광진구");
        member.setPostcode("111222");
        Member updateMember = memberService.updateMember(member);

        // then
        assertThat(updateMember.getAddress()).isEqualTo("서울 광진구");
        assertThat(updateMember.getPostcode()).isEqualTo("111222");
    }

    @Test
    @DisplayName("모든 회원을 조회할 수 있다.")
    void getAllMembers() {
        // given
        memberService.createMember("test1", "test1@gmail.com", "서울시 노원구", "000111");
        memberService.createMember("test2", "test2@gmail.com", "서울시 노원구", "000111");

        // when
        List<Member> members = memberService.getAllMembers();

        // then
        assertThat(members).hasSize(2);
    }

    @Test
    @DisplayName("ID로 회원을 조회할 수 있다.")
    void getMemberById() {
        // given
        Member member = memberService.createMember("test", "test@gmail.com", "서울시 노원구", "000111");
        UUID memberId = member.getMemberId();

        // when
        Member findMember = memberService.getMemberById(memberId);

        // then
        assertThat(findMember.getMemberId()).isEqualTo(memberId);
    }

    @Test
    @DisplayName("존재하지 않은 ID로 회원을 조회할 때, 에러 메세지를 던진다.")
    void getMemberByNonExistentId() {
        // given
        UUID nonExistentId = UUID.randomUUID();

        // then
        assertThatThrownBy(() -> memberService.getMemberById(nonExistentId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("상품을 삭제할 수 있다.")
    void deleteMember() {
        // given
        Member member = memberService.createMember("test", "test@gmail.com", "서울시 노원구", "000111");
        UUID memberId = member.getMemberId();

        // when
        memberService.deleteMember(memberId);

        // then
        Optional<Member> findMember = memberRepository.findById(memberId);
        assertThat(findMember).isNotPresent();
    }
}