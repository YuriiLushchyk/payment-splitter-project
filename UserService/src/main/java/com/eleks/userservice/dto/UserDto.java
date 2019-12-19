package com.eleks.userservice.dto;

import com.eleks.userservice.serializer.SimpleDateJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    @NotNull
    @Size(min = 1, max = 50)
    private String username;

    @NotNull
    @Size(min = 1, max = 50)
    private String firstName;

    @NotNull
    @Size(min = 1, max = 50)
    private String lastName;

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SimpleDateJsonDeserializer.PATTERN)
    @JsonDeserialize(using = SimpleDateJsonDeserializer.class)
    private LocalDate dateOfBirth;

    @NotNull
    @Email
    private String email;

    @NotNull
    private Boolean receiveNotifications;
}
