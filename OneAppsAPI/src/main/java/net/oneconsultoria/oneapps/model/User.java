package net.oneconsultoria.oneapps.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.*;

@Entity(name = "usuario")
@NamedQuery(name = "usuario.findAll", query = "SELECT u FROM usuario u")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "nome")
    private String nome;
    private String apelido;
    @Column(name = "senha")
    private String senha;
    @Column(name = "email")
    private String email;
    private Date ultimoLogin;
    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;
    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;
    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;
    @Column(name = "enabled")
    private Boolean enabled;

    private String status;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_permission", joinColumns = {@JoinColumn(name = "id_user")},
    inverseJoinColumns = {@JoinColumn(name = "id_permission")}
    )
    private List<Permission> permissions;

    public User() {
    }

    public List<String> getRoles(){
        List<String> roles = new ArrayList<>();
        for(Permission p : permissions){
            roles.add(p.getDescription());
        }
        return roles;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.permissions;
    }

    @Override
    public String getPassword() {
        return senha;
    }

    @Override
    public String getUsername() {
        return nome;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Date getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(Date ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
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

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        this.apelido = apelido;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
