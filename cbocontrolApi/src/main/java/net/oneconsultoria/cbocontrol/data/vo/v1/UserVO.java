package net.oneconsultoria.cbocontrol.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.util.Objects;

@JsonPropertyOrder({ "id", "email", "userName", "fullName", "enabled" })
public class UserVO extends RepresentationModel<UserVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;
    private String userName;
    private String fullName;
    private String email;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private Boolean accountNonExpired;
    @JsonIgnore
    private Boolean accountNonLocked;
    @JsonIgnore
    private Boolean credentialsNonExpired;
    private Boolean enabled;

    public UserVO() {
    }

    public UserVO(Long key, String userName, String fullName, String email, String password,
                  Boolean accountNonExpired, Boolean accountNonLocked, Boolean credentialsNonExpired, Boolean enabled) {
        this.key = key;
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAccountNonExpired() {
        return accountNonExpired;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean getAccountNonLocked() {
        return accountNonLocked;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public Boolean getCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        UserVO userVO = (UserVO) o;
        return Objects.equals(key, userVO.key) && Objects.equals(userName, userVO.userName) && Objects.equals(fullName, userVO.fullName) && Objects.equals(password, userVO.password) && Objects.equals(accountNonExpired, userVO.accountNonExpired) && Objects.equals(accountNonLocked, userVO.accountNonLocked) && Objects.equals(credentialsNonExpired, userVO.credentialsNonExpired) && Objects.equals(enabled, userVO.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, userName, fullName, password, accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }
}
