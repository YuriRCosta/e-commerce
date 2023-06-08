package br.com.ecommerce.ecommerce.service;

import javax.net.ssl.*;
import java.io.Serializable;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Set;

public class IgnoreHostNameSSL implements HostnameVerifier, Serializable {

    private static final HostnameVerifier DEFAULT_HOSTNAME_VERIFIER = HttpsURLConnection.getDefaultHostnameVerifier();
    private Set<String> trustedHosts;

    public IgnoreHostNameSSL(Set<String> trustedHosts) {
        this.trustedHosts = trustedHosts;
    }

    public static HostnameVerifier getDefaultHostnameVerifier() throws KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        } };

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {

            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        };

        return hostnameVerifier;
    }

    @Override
    public boolean verify(String hostName, SSLSession session) {
        if (trustedHosts.contains(hostName)) {
            return true;
        } else {
            return DEFAULT_HOSTNAME_VERIFIER.verify(hostName, session);
        }
    }
}
