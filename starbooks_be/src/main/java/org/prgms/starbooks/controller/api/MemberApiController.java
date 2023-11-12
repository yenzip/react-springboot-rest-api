package org.prgms.starbooks.controller.api;

import org.prgms.starbooks.controller.dto.member.CreateMemberRequest;
import org.prgms.starbooks.model.member.Member;
import org.prgms.starbooks.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/members")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody @Validated CreateMemberRequest request) {
        Member member = memberService.createMember(request.name(), request.email(), request.address(), request.postcode());
        return ResponseEntity.created(URI.create("/api/vi/members/" + member.getMemberId()))
                .body(member);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMember(@PathVariable("id") UUID memberId) {
        Member member = memberService.getMemberById(memberId);
        return ResponseEntity.ok(member);
    }

    @GetMapping
    public ResponseEntity<Member> getMemberByEmail(@RequestParam String email) {
        Member member = memberService.getMemberByEmail(email);
        return ResponseEntity.ok(member);
    }
}
