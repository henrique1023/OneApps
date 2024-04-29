import { Metadata } from 'next';
import Layout from '../../layout/layout';
import React from 'react';

interface SimpleLayoutProps {
    children: React.ReactNode;
}

export const metadata: Metadata = {
    title: 'One Admin',
    description: 'Sistema tributario.',
    robots: { index: false, follow: false },
    viewport: { initialScale: 1, width: 'device-width' },
    openGraph: {
        type: 'website',
        title: 'One Admin',
        url: 'https://onecontrol.oneconsultoria.net/',
        description: 'Sistema tributario.',
        images: [],
        ttl: 604800
    },
    icons: {
        icon: '/favicon.ico'
    }
};

export default function SimpleLayout({ children }: SimpleLayoutProps) {
    return <Layout>{children}</Layout>;
}
