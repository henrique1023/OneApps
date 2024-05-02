package net.oneconsultoria.oneapps.data.vo.v1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@JsonPropertyOrder({ "id", "nome", "apelido", "email", "enabled", "status" })
public class UserVO extends RepresentationModel<UserVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Mapping("id")
    @JsonProperty("id")
    private Long key;
    private String nome;
    private String apelido;
    private String email;
    @JsonIgnore
    private String senha;
    @JsonIgnore
    private Boolean accountNonExpired;
    @JsonIgnore
    private Boolean accountNonLocked;
    @JsonIgnore
    private Boolean credentialsNonExpired;
    private Boolean enabled;
    private String status;

    public UserVO() {
    }

    public UserVO(Long key, String nome, String apelido, String email, String senha, Boolean accountNonExpired, Boolean accountNonLocked,
                  Boolean credentialsNonExpired, Boolean enabled, String status) {
        this.key = key;
        this.nome = nome;
        this.apelido = apelido;
        this.email = email;
        this.senha = senha;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
        this.status = status;
    }

    public Long getKey() {
        return key;
    }

    public void setKey(Long key) {
        this.key = key;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
