package org.prgms.starbooks.controller.dto.member;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateMemberRequest(
        @NotBlank(message = "Name must not be blank")
        String name,
        @NotBlank(message = "Email must not be blank")
        @Email(message = "Invalid email address")
        String email,
        @NotBlank(message = "Address must not be blank")
        String address,
        @NotBlank(message = "Postcode must not be blank")
        String postcode) {
}
