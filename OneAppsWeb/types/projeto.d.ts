declare namespace Projeto {
    type User = {
        id?: number;
        nome: string;
        apelido: string;
        email: string;
        password: string;
        accountNonExpired: boolean;
        accountNonLocked: boolean;
        credentialsNonExpired: boolean;
        enabled: boolean;
        status: string;
    };
}