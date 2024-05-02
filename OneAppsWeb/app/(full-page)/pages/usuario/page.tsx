/* eslint-disable @next/next/no-img-element */
'use client';
import { Button } from 'primereact/button';
import { Column } from 'primereact/column';
import { DataTable } from 'primereact/datatable';
import { Dialog } from 'primereact/dialog';
import { InputText } from 'primereact/inputtext';
import { Toast } from 'primereact/toast';
import { Toolbar } from 'primereact/toolbar';
import { classNames } from 'primereact/utils';
import React, { useEffect, useRef, useState } from 'react';
import { UsuarioService } from '@/service/UsuarioService';
import { Projeto } from '@/types';

/* @todo Used 'as any' for types here. Will fix in next version due to onSelectionChange event type issue. */
const Crud = () => {
    let usuarioVazio: Projeto.User = {
        id: 0,
        nome: '',
        apelido: '',
        email: '',
        password: '',
        accountNonExpired: true,
        accountNonLocked: true,
        credentialsNonExpired: true,
        enabled: true,
        status: ''
    };

    const [usuarios, setUsuarios] = useState(null);
    const [usuarioDialog, setUsuarioDialog] = useState(false);
    const [usuarioSaveDialog, setUsuarioSaveDialog] = useState(false);
    const [deleteUsuarioDialog, setDeleteUsuarioDialog] = useState(false);
    const [deleteUsuariosDialog, setDeleteUsuariosDialog] = useState(false);
    const [usuario, setUsuario] = useState<Projeto.User>(usuarioVazio);
    const [selectedUsuarios, setSelectedUsuarios] = useState(null);
    const [submitted, setSubmitted] = useState(false);
    const [globalFilter, setGlobalFilter] = useState('');
    const toast = useRef<Toast>(null);
    const dt = useRef<DataTable<any>>(null);
    const usuarioService = new UsuarioService();

    useEffect(() => {
        usuarioService.findAll().then((resp) => {
            setUsuarios(resp.data._embedded.userVOList)
        }).catch((error) => {
            toast.current?.show({
                severity: 'error',
                summary: 'Erro ao carregar',
                detail: 'Erro ao carregar usuarios, tente novamente ou contate o suporte',
                life: 3000
            });
        });
    }, []);

    const openNew = () => {
        setUsuario(usuarioVazio);
        setSubmitted(false);
        setUsuarioSaveDialog(true);
    };

    const hideDialog = () => {
        setSubmitted(false);
        setUsuarioDialog(false);
        setUsuarioSaveDialog(false);
    };

    const hideDeleteUsuarioDialog = () => {
        setDeleteUsuarioDialog(false);
    };

    const hideDeleteUsuariosDialog = () => {
        setDeleteUsuariosDialog(false);
    };

    const saveUsuario = () => {
        setSubmitted(true);

         if (usuario.nome.trim()) {
             let _usuarios = [...(usuarios as any)];
             let _usuario = { ...usuario };
             if (usuario.id) {
                 const index = findIndexById(usuario.id + '');

                 _usuarios[`${index}`] = _usuario;
                 usuarioService.alterar(_usuario).then((resp) => {
                        toast.current?.show({
                            severity: 'success',
                            summary: 'Sucesso',
                            detail: 'Usuario Atualizado com sucesso!',
                            life: 3000
                        });
                 }).catch((error) => {
                    toast.current?.show({
                        severity: 'error',
                        summary: 'Erro ao atualizar usuario',
                        detail: 'Erro ao atualizar usuario, tente novamente ou contate o suporte!',
                        life: 3000
                    });
                });
                
             } else {
                _usuarios.push(_usuario);
                usuarioService.cadastrar(_usuario).then((resp) => {
                    _usuario.id = resp.data.id;
                    toast.current?.show({
                        severity: 'success',
                        summary: 'Sucesso',
                        detail: 'Usuario Criado com sucesso!',
                        life: 3000
                    });
                }
                ).catch((error) => {
                    toast.current?.show({
                        severity: 'error',
                        summary: 'Erro ao criar usuario',
                        detail: 'Erro ao criar usuario, tente novamente ou contate o suporte!',
                        life: 3000
                    });
                });
             }

            setUsuarios(_usuarios as any);
            setUsuarioDialog(false);
            setUsuarioSaveDialog(false);
            setUsuario(usuario);
         }
    };

    const editUsuario = (usuario: Projeto.User) => {
        setUsuario({ ...usuario });
        setUsuarioDialog(true);
    };

    const confirmDeleteUsuario = (usuario: Projeto.User) => {
        setUsuario(usuario);
        setDeleteUsuarioDialog(true);
    };

    const deleteUsuario = () => {
        let _usuarios = (usuarios as any)?.filter((val: any) => val.id !== usuario.id);
        setUsuarios(_usuarios);
        setDeleteUsuarioDialog(false);
        setUsuario(usuario);
        usuarioService.deletar(usuario.id as number)
        .then((resp) => {
            toast.current?.show({
                severity: 'success',
                summary: 'Deletado com sucesso',
                detail: 'Usuario Deletado com sucesso!',
                life: 3000
            });
        }).catch((error) => {  
            toast.current?.show({
                severity: 'error',
                summary: 'Erro ao deletar usuario',
                detail: 'Erro ao deletar usuario, tente novamente ou contate o suporte!',
                life: 3000
            });
        });
    };

    const findIndexById = (id: string) => {
        let index = -1;
        for (let i = 0; i < (usuarios as any)?.length; i++) {
            if ((usuarios as any)[i].id === id) {
                index = i;
                break;
            }
        }

        return index;
    };

    const exportCSV = () => {
        dt.current?.exportCSV();
    };

    const confirmDeleteSelected = () => {
        setDeleteUsuarioDialog(true);
    };

    const deleteSelectedUsuarios = () => {
        let _usuarios = (usuarios as any)?.filter((val: any) => !(selectedUsuarios as any)?.includes(val));
        setUsuarios(_usuarios);
        setDeleteUsuariosDialog(false);
        setSelectedUsuarios(null);
        toast.current?.show({
            severity: 'success',
            summary: 'Successful',
            detail: 'Usuarios Deleted',
            life: 3000
        });
    };

    const onInputChange = (e: React.ChangeEvent<HTMLInputElement | HTMLTextAreaElement>, name: string) => {
        const val = (e.target && e.target.value) || '';
        let _usuario = { ...usuario };
        _usuario[`${name}`] = val;

        setUsuario(_usuario);
    };

    const leftToolbarTemplate = () => {
        return (
            <React.Fragment>
                <div className="my-2">
                    <Button label="Novo" icon="pi pi-plus" severity="success" className=" mr-2" onClick={openNew} />
                    <Button label="Excluir" icon="pi pi-trash" severity="danger" onClick={confirmDeleteSelected} disabled={!selectedUsuarios || !(selectedUsuarios as any).length} />
                </div>
            </React.Fragment>
        );
    };

    const rightToolbarTemplate = () => {
        return (
            <React.Fragment>
                <Button label="Exportar" icon="pi pi-upload" severity="help" onClick={exportCSV} />
            </React.Fragment>
        );
    };

    const nomeBodyTemplate = (rowData: Projeto.User) => {
        return (
            <>
                <span className="p-column-title">Nome</span>
                {rowData.nome}
            </>
        );
    };

    const fullNameBodyTemplate = (rowData: Projeto.User) => {
        return (
            <>
                <span className="p-column-title">Apelido</span>
                {rowData.apelido}
            </>
        );
    };

    const emailBodyTemplate  = (rowData: Projeto.User) => {
        return (
            <>
                <span className="p-column-title">Email</span>
                {rowData.email}
            </>
        );
    };

    const statusBodyTemplate  = (rowData: Projeto.User) => {
        return (
            <>
                <span className="p-column-title">Status</span>
                <Button icon={rowData.enabled ? 'pi pi-check' : 'pi pi-times'} rounded severity={rowData.enabled ? "success" : "danger"}  />
            </>
        );
    };

    const actionBodyTemplate = (rowData: Projeto.User) => {
        return (
            <>
                <Button icon="pi pi-pencil" rounded severity="success" className="mr-2" onClick={() => editUsuario(rowData)} />
                <Button icon="pi pi-trash" rounded severity="danger" onClick={() => confirmDeleteUsuario(rowData)} />
            </>
        );
    };

    const header = (
        <div className="flex flex-column md:flex-row md:justify-content-between md:align-items-center">
            <h5 className="m-0">Usuarios</h5>
            <span className="block mt-2 md:mt-0 p-input-icon-left">
                <i className="pi pi-search" />
                <InputText type="search" onInput={(e) => setGlobalFilter(e.currentTarget.value)} placeholder="Procurar..." />
            </span>
        </div>
    );

    const usuarioDialogFooter = (
        <>
            <Button label="Cancelar" icon="pi pi-times" text onClick={hideDialog} />
            <Button label="Salvar" icon="pi pi-check" text onClick={saveUsuario} />
        </>
    );
    const deleteUsuarioDialogFooter = (
        <>
            <Button label="Não" icon="pi pi-times" text onClick={hideDeleteUsuarioDialog} />
            <Button label="Sim" icon="pi pi-check" text onClick={deleteUsuario} />
        </>
    );
    const deleteUsuariosDialogFooter = (
        <>
            <Button label="Não" icon="pi pi-times" text onClick={hideDeleteUsuariosDialog} />
            <Button label="Sim" icon="pi pi-check" text onClick={deleteSelectedUsuarios} />
        </>
    );

    return (
        <div className="grid crud-demo">
            <div className="col-12">
                <div className="card">
                    <Toast ref={toast} />
                    <Toolbar className="mb-4" left={leftToolbarTemplate} right={rightToolbarTemplate}></Toolbar>

                    <DataTable
                        ref={dt}
                        value={usuarios}
                        selection={selectedUsuarios}
                        onSelectionChange={(e) => setSelectedUsuarios(e.value as any)}
                        dataKey="id"
                        paginator
                        rows={10}
                        rowsPerPageOptions={[5, 10, 25]}
                        className="datatable-responsive"
                        paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
                        currentPageReportTemplate="Mostrando {first} até {last} de {totalRecords} usuarios"
                        globalFilter={globalFilter}
                        emptyMessage="Nenhum usuario encontrado."
                        header={header}
                        responsiveLayout="scroll"
                    >
                        <Column selectionMode="multiple" headerStyle={{ width: '4rem' }}></Column>
                        <Column field="nome" header="Nome" filterField='nome' filter
                         sortable body={nomeBodyTemplate} headerStyle={{ minWidth: '10rem' }}></Column>
                        <Column field="apelido" header="Apelido" sortable filterField='apelido' filter body={fullNameBodyTemplate} headerStyle={{ minWidth: '10rem' }}></Column>
                        <Column field="email" header="Email" sortable body={emailBodyTemplate} filterField='email' filter
                         headerStyle={{ minWidth: '15rem' }}></Column>
                        <Column field="enabled" header="Status" sortable body={statusBodyTemplate} headerStyle={{ minWidth: '9rem'}}></Column>
                        <Column body={actionBodyTemplate} headerStyle={{ minWidth: '10rem' }}></Column>
                    </DataTable>

                    <Dialog visible={usuarioDialog} style={{ width: '450px' }} header="Usuario Editar" modal className="p-fluid" footer={usuarioDialogFooter} onHide={hideDialog}>
                        <div className="field">
                            <label htmlFor="name">Nome</label>
                            <InputText
                                id="userName"
                                value={usuario.nome}
                                onChange={(e) => onInputChange(e, 'userName')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !usuario.nome
                                })}
                            />
                            {submitted && !usuario.nome && <small className="p-invalid">Nome obrigatorio.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="fullName">Nome completo</label>
                            <InputText
                                id="fullName"
                                value={usuario.apelido}
                                onChange={(e) => onInputChange(e, 'fullName')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !usuario.apelido
                                })}
                            />
                            {submitted && !usuario.apelido && <small className="p-invalid">Nome completo obrigatorio.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="password">Senha</label>
                            <InputText
                                id="password"
                                value={usuario.password}
                                onChange={(e) => onInputChange(e, 'password')}
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !usuario.password
                                })}
                            />
                        </div>
                        <div className="field">
                            <label htmlFor="email">Email</label>
                            <InputText
                                id="email"
                                value={usuario.email}
                                onChange={(e) => onInputChange(e, 'email')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !usuario.email
                                })}
                            />
                            {submitted && !usuario.email && <small className="p-invalid">Email é obrigatorio.</small>}
                        </div>
                    </Dialog>

                    <Dialog visible={usuarioSaveDialog} style={{ width: '450px' }} header="Usuario Salvar" modal className="p-fluid" footer={usuarioDialogFooter} onHide={hideDialog}>
                        <div className="field">
                            <label htmlFor="userName">Nome</label>
                            <InputText
                                id="userName"
                                value={usuario.nome}
                                onChange={(e) => onInputChange(e, 'userName')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !usuario.nome
                                })}
                            />
                            {submitted && !usuario.nome && <small className="p-invalid">Nome obrigatorio.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="fullName">Nome completo</label>
                            <InputText
                                id="fullName"
                                value={usuario.apelido}
                                onChange={(e) => onInputChange(e, 'fullName')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !usuario.apelido
                                })}
                            />
                            {submitted && !usuario.apelido && <small className="p-invalid">Nome completo obrigatorio.</small>}
                        </div>

                        <div className="field">
                            <label htmlFor="password">Senha</label>
                            <InputText
                                id="password"
                                type='password'
                                value={usuario.password}
                                onChange={(e) => onInputChange(e, 'password')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !usuario.password
                                })}
                            />
                             {submitted && !usuario.password && <small className="p-invalid">Senha é obrigatorio.</small>}
                        </div>
                        <div className="field">
                            <label htmlFor="email">Email</label>
                            <InputText
                                id="email"
                                value={usuario.email}
                                onChange={(e) => onInputChange(e, 'email')}
                                required
                                autoFocus
                                className={classNames({
                                    'p-invalid': submitted && !usuario.email
                                })}
                            />
                            {submitted && !usuario.email && <small className="p-invalid">Email é obrigatorio.</small>}
                        </div>
                    </Dialog>

                    <Dialog visible={deleteUsuarioDialog} style={{ width: '450px' }} header="Confirma" modal footer={deleteUsuarioDialogFooter} onHide={hideDeleteUsuarioDialog}>
                        <div className="flex align-items-center justify-content-center">
                            <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
                            {usuario && (
                                <span>
                                    Tem certeza que deseja apagar <b>{usuario.nome}</b>?
                                </span>
                            )}
                        </div>
                    </Dialog>

                    <Dialog visible={deleteUsuariosDialog} style={{ width: '450px' }} header="Confirmar" modal footer={deleteUsuariosDialogFooter} onHide={hideDeleteUsuariosDialog}>
                        <div className="flex align-items-center justify-content-center">
                            <i className="pi pi-exclamation-triangle mr-3" style={{ fontSize: '2rem' }} />
                            {usuario && <span>Tem certeza que deseja apagar os usuarios selecionados?</span>}
                        </div>
                    </Dialog>
                </div>
            </div>
        </div>
    );
};

export default Crud;
