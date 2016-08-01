/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package vopro7.vop.mobiliteitgent.certificates;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author App Team
 */
public class CertificateInstaller {

    private static final String KEYSTORE_FILE = "kriksleutelkrakvopro";
    private static final char[] PASSPHRASE = "IkBenKabouterPlop".toCharArray();
    private static final String HOST = "vopro7.ugent.be";
    private static final int PORT = 443;

    /**
     * 
     * @param context The context of the installer
     * @param host The host url of the certificate
     * @param port The port of the certificate
     * @return A SSL socket
     * @throws NoSuchAlgorithmException This is thrown when something's wrong
     * @throws KeyStoreException This is thrown when something's wrong
     * @throws KeyManagementException This is thrown when something's wrong
     * @throws IOException This is thrown when something's wrong
     * @throws CertificateEncodingException This is thrown when something's wrong
     * @throws CertificateException This is thrown when something's wrong
     */
    public static SSLSocketFactory installCertificates(Context context, String host, int port) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, CertificateEncodingException, CertificateException{
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());


        File outputDir = context.getFilesDir();
        File certVopro7 = new File(outputDir, KEYSTORE_FILE);

        //Initialization of keystore
        if(certVopro7.exists()) {
            // Load keystore from file
            InputStream in = null;
            try {
                in = new FileInputStream(certVopro7);
                ks.load(in, PASSPHRASE);
            } catch (IOException ex) {
                Logger.getLogger(CertificateInstaller.class.getName()).log(Level.SEVERE, "Unable to load current key-store! New empty keystore will be created instead! ", ex);
                ks.load(null, null);
            } finally {
                if (in != null) {
                    in.close();
                }
            }
        } else {
            // Initialize empty keystore
            ks.load(null, PASSPHRASE);
        }


        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        sslContext.init(null, new TrustManager[]{tm}, null);
        SSLSocketFactory factory = sslContext.getSocketFactory();

        SSLSocket socket = (SSLSocket) factory.createSocket(HOST, PORT);
        socket.setSoTimeout(10000);
        try {
            socket.startHandshake();
            socket.close();
            // No errors = certificate already trusted
        } catch (SSLException e) {
            
            X509Certificate[] chain = tm.chain;
            if (chain == null) {
                return factory;
            }

            //System.setProperty("javax.net.ssl.keyStore", certVopro7.getAbsolutePath());
            
            OutputStream out = new FileOutputStream(certVopro7);

            for (int i = 0; i < chain.length; i++) {
                X509Certificate cert = chain[i];
                if(cert.getSubjectDN().getName().contains(HOST)){
                    String alias = HOST + "-mobiliteitGent";
                    ks.setCertificateEntry(alias, cert);
                    ks.store(out, PASSPHRASE);
                    break;
                }
            }

            sslContext = SSLContext.getInstance("TLS");
            tmf =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);
            defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
            tm = new SavingTrustManager(defaultTrustManager);
            sslContext.init(null, new TrustManager[]{tm}, null);
            factory = sslContext.getSocketFactory();
        }

        return factory;
    }
}
