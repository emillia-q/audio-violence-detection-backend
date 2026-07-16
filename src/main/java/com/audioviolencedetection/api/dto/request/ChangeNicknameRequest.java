package com.audioviolencedetection.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeNicknameRequest(
        @Size(max = 100, message = "Nickname cannot be longer than 100 characters")
        @Pattern(
                regexp = "^(?=.*\\S)[a-zA-Z0-9ąęćłńóśźżĄĘĆŁŃÓŚŹŻ ]*$",
                message = "Nickname cannot be blank and can only contain letters, numbers and spaces"
        )
        String customNickname
) {
}
