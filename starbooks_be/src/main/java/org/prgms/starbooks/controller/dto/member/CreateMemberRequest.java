package org.prgms.starbooks.controller.dto.member;

// TODO: 각 입력값에 대한 유효성 검사 추가
public record CreateMemberRequest(String name, String email, String address, String postcode) {
}
