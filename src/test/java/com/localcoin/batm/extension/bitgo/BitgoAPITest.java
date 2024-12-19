package com.localcoin.batm.extension.bitgo;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.localcoin.batm.extension.bitgo.net.CompatSSLSocketFactory;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.ClientConfig;
import si.mazi.rescu.RestProxyFactory;

import javax.net.ssl.SSLContext;
import javax.ws.rs.HeaderParam;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.List;


public class BitgoAPITest {

    private static final Logger log = LoggerFactory.getLogger(BitgoAPITest.class);

    private static IBitgoAPI api;

    private static final Integer readTimeout = 90 * 1000; //90 seconds

    private static void setLoggerLevel(String name, String level) {
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
            List<ch.qos.logback.classic.Logger> loggers = loggerContext.getLoggerList();

            for (ch.qos.logback.classic.Logger logger : loggers) {
                if (logger.getName().startsWith(name)) {
                    logger.setLevel(Level.toLevel(level));
                }
            }
        } catch (Throwable e) {
            log.error("batm.master.ServerUtil - setLoggerLevel");
            log.error("setLoggerLevel", e);
        }
    }

    @BeforeClass
    public static void setup() {
        setLoggerLevel("batm", "trace");
        setLoggerLevel("si.mazi.rescu","trace");

        ClientConfig config = new ClientConfig();
        config.setHttpReadTimeout(readTimeout);
        config.addDefaultParam(HeaderParam.class, "Authorization", "Bearer v2x8d5e9e46379dc328b2039a400a12b04ea986689b38107fd84cd339bc89e3fb21");

        try {
            SSLContext sslcontext = SSLContext.getInstance("TLS");
            sslcontext.init(null, null, null);
            final CompatSSLSocketFactory socketFactory = new CompatSSLSocketFactory(sslcontext.getSocketFactory());
            config.setSslSocketFactory(socketFactory);
            config.setIgnoreHttpErrorCodes(true);
        }catch(KeyManagementException kme) {
            log.error("", kme);
        } catch (NoSuchAlgorithmException nae) {
            log.error("", nae);
        }
        api = RestProxyFactory.createProxy(IBitgoAPI.class, "https://test.bitgo.com/", config);
    }
}
