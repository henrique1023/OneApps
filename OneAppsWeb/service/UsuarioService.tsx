import React from "react";
import axios from "axios";
import { useRouter } from 'next/navigation';

export const axiosInstance = axios.create({
    baseURL: 'http://localhost:8080'
})

export class UsuarioService {
    router = useRouter();
    
    recuperaToken() {
        if(localStorage.getItem('accessToken') === null || localStorage.getItem('accessToken') === undefined){
            this.router.push('/');
            return ''
        }

        return 'Bearer ' + localStorage.getItem('accessToken');
    }

    async login(username: string, password: string) {
        return axiosInstance.post('/auth/signin', {
            username,
            password
        });
    }

    async findAll() {
        return axiosInstance.get('/api/usuario/v1',{
            headers: {
                Authorization: `${this.recuperaToken()}`
            }
        })
    }

    async cadastrar(usuario : Projeto.User) {
        return axiosInstance.post('/api/usuario/v1', usuario,{
            headers: {
                Authorization: `${this.recuperaToken()}`
            }
        })
    }
    
    async alterar(usuario : Projeto.User) {
        return axiosInstance.put('/api/usuario/v1', usuario,{
            headers: {
                Authorization: `${this.recuperaToken()}`
            }
        })
    }

    async deletar(id : number) {
        return axiosInstance.delete(`/api/usuario/v1/${id}`,{
            headers: {
                Authorization: `${this.recuperaToken()}`
            }
        })
    }
}