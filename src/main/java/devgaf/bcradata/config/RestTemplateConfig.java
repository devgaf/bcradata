package devgaf.bcradata.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class RestTemplateConfig {

    /**
     * Devuelve un objeto RestTemplate configurado para que no verifique el
     * certificado SSL de los sitios web que se consultan. Esto se hace para que 
     * pueda consumir la API de DolarSi y la API del BCRA, que tienen un certificado 
     * SSL autofirmado.
     * 
     * @return un objeto RestTemplate configurado para no verificar el certificado
     *         SSL
     * @throws Exception si hay un error al configurar el objeto RestTemplate
     */
    @Bean
    public RestTemplate restTemplate() throws Exception {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    /**
                     * Devuelve un arreglo vacio de X509Certificate que indica los
                     * certificados que se consideran validos. Esto se hace para que el
                     * objeto X509TrustManager no lance excepciones al verificar el
                     * certificado SSL.
                     *
                     * @return un arreglo vacio de X509Certificate
                     */
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    /**
                     * No hace nada. Este mtodo debe ser implementado para que el objeto
                     * X509TrustManager sea valido, pero no se utiliza en este caso.
                     *
                     * @param certs    arreglo de certificados que se estan verificando
                     * @param authType tipo de autenticacion que se esta realizando
                     */
                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    /**
                     * No hace nada. Este metodo debe ser implementado para que el objeto
                     * X509TrustManager sea valido, pero no se utiliza en este caso.
                     *
                     * @param authType tipo de autenticacion que se esta realizando
                     */
                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(60000);
        factory.setReadTimeout(60000);

        return new RestTemplate(factory);
    }
}