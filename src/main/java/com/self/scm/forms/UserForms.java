package com.self.scm.forms;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserForms {

    @NotBlank(message = "Please enter your username")
    @Size(min=3, message="Minimun 3 characters required")
    private String name;
    @Email(message="Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Please enter password")
    @Size(min=6, message = "Minimum 6 characters required")
    private String password;
    @NotBlank(message = "Please provide some details")
    private String about;
    @Size(min=10,max=10, message = "Invalid phone number")
    private String phoneNumber;
}
