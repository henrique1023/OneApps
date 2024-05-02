/* eslint-disable @next/next/no-img-element */
'use client';
import { useRouter } from 'next/navigation';
import React, { useRef, useContext, useState } from 'react';
import { Checkbox } from 'primereact/checkbox';
import { Toast } from 'primereact/toast';
import { Button } from 'primereact/button';
import { Password } from 'primereact/password';
import { LayoutContext } from '@/layout/context/layoutcontext';
import { InputText } from 'primereact/inputtext';
import { classNames } from 'primereact/utils';
import { UsuarioService } from '@/service/UsuarioService';
import '@/styles/layout/_login.scss';

const LoginPage = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [checked, setChecked] = useState(false);
    const [loading, setLoading] = useState(false);
    const { layoutConfig } = useContext(LayoutContext);
    const usuarioService = new UsuarioService();
    const toast = useRef<Toast>(null);

    const router = useRouter();
    const containerClassName = classNames('surface-ground flex align-items-center justify-content-center min-h-screen min-w-screen overflow-hidden',
     { 'p-input-filled': layoutConfig.inputStyle === 'filled' });

    function loginIn(email: string, password: string) {
        setLoading(true);
        usuarioService.login(email, password).then((response) => {
            localStorage.setItem('username', response.data.userName)
            localStorage.setItem('accessToken', response.data.accessToken)
            localStorage.setItem('refleshToken', response.data.refleshToken)
            router.push('/pages/usuario')
            setLoading(false);
        }).catch((error) => {
            setLoading(false);
            toast.current?.show({
                severity: 'error',
                summary: 'Senha ou email incorretos',
                detail: 'Senha ou email incorretos, tente novamente!',
                life: 3500
            });
        })
    }

    return (
        <div className={containerClassName}>
         <Toast ref={toast} />
            <div className="flex flex-column align-items-center justify-content-center background-div-image" >
                <div style={{
                        borderRadius: '23px',
                        padding: '0.3rem',  
                        background: 'linear-gradient(#0D2C4B, rgb(33 150 243 / 34%) 150%)'
                    }}>

                    <div className="w-full surface-card py-8 px-5 sm:px-8 form-login">
                        <div className="mb-5" style={{display: 'flex', alignItems: 'center'}}>
                            <img src="/layout/images/logo-black.png" alt="Image" height="80" className="mb-3" />
                        </div>

                        <div style={{width: '60%' }}>
                            <label htmlFor="email1" className="block text-900 text-xl font-medium mb-2">
                                Email / Usuario
                            </label>
                            <InputText id="email1" value={email} type="text" placeholder="Endereço de Email" 
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full md:w-30rem mb-5" style={{ padding: '1rem' }} />

                            <label htmlFor="password1" className="block text-900 font-medium text-xl mb-2">
                                Senha
                            </label>
                            <InputText id="password1" value={password} type='password'
                            onChange={(e) => setPassword(e.target.value)} placeholder="Senha" 
                            className="w-full md:w-30rem mb-5" style={{ padding: '1rem' }} ></InputText>

                            <div className="flex align-items-center justify-content-between mb-5 gap-5">
                                <div className="flex align-items-center">
                                    <Checkbox inputId="rememberme1" checked={checked} onChange={(e) => setChecked(e.checked ?? false)} className="mr-2"></Checkbox>
                                    <label htmlFor="rememberme1">Lembrar-me</label>
                                </div>
                                <a className="font-medium no-underline ml-2 text-right cursor-pointer" style={{ color: 'var(--primary-color)' }}>
                                    Esqueceu a senha?
                                </a>
                            </div>
                            <Button label="Acessar" loading={loading}  className="w-full p-3 text-xl" onClick={() => loginIn(email, password)}></Button>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default LoginPage;
