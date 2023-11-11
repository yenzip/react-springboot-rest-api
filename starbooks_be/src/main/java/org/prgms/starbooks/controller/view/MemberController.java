package org.prgms.starbooks.controller.view;

import org.prgms.starbooks.controller.dto.member.CreateMemberRequest;
import org.prgms.starbooks.controller.dto.member.UpdateMemberRequest;
import org.prgms.starbooks.model.member.Member;
import org.prgms.starbooks.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@RequestMapping("/members")
@Controller
public class MemberController {

    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping
    public String getAllMembers(Model model) {
        List<Member> members = memberService.getAllMembers();
        model.addAttribute("members", members);
        return "member/member-list";
    }

    @GetMapping("/save")
    public String createMember() {
        return "member/member-new";
    }

    @PostMapping("/save")
    public String createMember(CreateMemberRequest request) {
        memberService.createMember(request.name(), request.email(), request.address(), request.postcode());
        return "redirect:/members";
    }

    @GetMapping("/{id}")
    public String getMember(@PathVariable("id") UUID memberId, Model model) {
        Member member = memberService.getMemberById(memberId);
        model.addAttribute("member", member);
        return "member/member-detail";
    }

    @GetMapping("{id}/edit")
    public String updateMember(@PathVariable("id") UUID memberId, Model model) {
        model.addAttribute("member", memberService.getMemberById(memberId));
        return "member/member-edit";
    }

    @PostMapping("{id}/edit")
    public String updateMember(@PathVariable("id") UUID memberId, UpdateMemberRequest request) {
        memberService.updateMember(request.toMember());
        return "redirect:/members/" + memberId;
    }

    @GetMapping("{id}/delete")
    public String deleteMember(@PathVariable("id") UUID memberId) {
        memberService.deleteMember(memberId);
        return "redirect:/members";
    }
}
