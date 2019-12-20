package com.eleks.userservice.dto.user;

import com.eleks.userservice.serializer.SimpleDateJsonDeserializer;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
    @NotNull(message = "username is required")
    @Size(min = 1, max = 50, message = "username length should be between 1 and 50")
    private String username;

    @NotNull(message = "firstName is required")
    @Size(min = 1, max = 50, message = "firstName length should be between 1 and 50")
    private String firstName;

    @NotNull(message = "lastName is required")
    @Size(min = 1, max = 50, message = "lastName length should be between 1 and 50")
    private String lastName;

    @NotNull(message = "dateOfBirth is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = SimpleDateJsonDeserializer.PATTERN)
    @JsonDeserialize(using = SimpleDateJsonDeserializer.class)
    private LocalDate dateOfBirth;

    @NotNull(message = "email is required")
    @Email(message = "email string has to be a well-formed email address")
    private String email;

    @NotNull(message = "receiveNotifications is required")
    private Boolean receiveNotifications;
}
