package nl.novi.backend.eindopdracht.hidriklandlust.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import nl.novi.backend.eindopdracht.hidriklandlust.models.entities.Authority;

import java.util.Set;

public class UserDto {

    private String username;
    private String password;
    private Boolean enabled = true;
    private String email;
    private AccountSummaryDto account;

    @JsonSerialize
    public Set<Authority> authorities;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public AccountSummaryDto getAccount() {
        return account;
    }

    public void setAccount(AccountSummaryDto account) {
        this.account = account;
    }
}