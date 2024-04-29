'use client';
import React, { useContext, useEffect } from 'react';
import { AppConfigProps } from '@/types';
import { LayoutContext } from './context/layoutcontext';

const AppConfig = (props: AppConfigProps) => {
    const { layoutConfig } = useContext(LayoutContext);
    const applyScale = () => {
        document.documentElement.style.fontSize = layoutConfig.scale + 'px';
    };

    useEffect(() => {
        applyScale();
    }, [layoutConfig.scale]);

    return (
        <>
        </>
    );
};

export default AppConfig;
