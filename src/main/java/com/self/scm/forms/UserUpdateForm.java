package com.self.scm.forms;

import org.springframework.web.multipart.MultipartFile;

import com.self.scm.validators.ValidFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class UserUpdateForm {

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

    @ValidFile()
    private MultipartFile userImage;
    private String picture;
}
