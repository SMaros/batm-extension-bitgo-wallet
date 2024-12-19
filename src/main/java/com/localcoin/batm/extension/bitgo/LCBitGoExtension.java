package com.localcoin.batm.extension.bitgo;

import com.generalbytes.batm.server.extensions.AbstractExtension;
import com.generalbytes.batm.server.extensions.IExtensionContext;
import com.generalbytes.batm.server.extensions.IWallet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.StringTokenizer;

public class LCBitGoExtension extends AbstractExtension {

    private static final Logger log = LoggerFactory.getLogger(LCBitGoExtension.class);
    private IExtensionContext ctx;

    private static final String BITGO_WALLET = "lcbitgo";

    private static final String BITGO_WALLET_NO_FORWARD = "lcbitgonoforward";

    @Override
    public void init(IExtensionContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Returns the context object
     *
     * @return @IExtensionContext
     */
    public IExtensionContext getCtx() {
        return ctx;
    }

    @Override
    public String getName() {
        return "Localcoin BitGo Extension";
    }

    @Override
    public IWallet createWallet(String walletLogin, String tunnelPassword) {
        try{
            if (walletLogin !=null && !walletLogin.trim().isEmpty()) {
                StringTokenizer st = new StringTokenizer(walletLogin,":");
                String walletType = st.nextToken();
                if (BITGO_WALLET.equalsIgnoreCase(walletType) || BITGO_WALLET_NO_FORWARD.equalsIgnoreCase(walletType)) {
                    // bitgo:host:port:token:wallet_address:wallet_passphrase:num_blocks
                    // but host is optionally including the "http://" and port is optional,
                    // num_blocks is an optional integer greater than 2 and it's used to calculate mining fee.
                    // bitgo:http://localhost:80:token:wallet_address:wallet_passphrase
                    // bitgo:http://localhost:token:wallet_address:wallet_passphrase
                    // bitgo:localhost:token:wallet_address:wallet_passphrase
                    // bitgo:localhost:80:token:wallet_address:wallet_passphrase
                    // bitgo:localhost:80:token:wallet_address:wallet_passphrase:num_blocks

                    String first = st.nextToken();
                    String scheme;
                    String host;
                    if (first.startsWith("http")) {
                        scheme = first;
                        host = st.nextToken().replaceAll("/", "");
                    } else {
                        scheme = "http";
                        host = first;
                    }

                    int port;
                    String token;
                    String next = st.nextToken();
                    if (next.length() > 6) {
                        port = scheme.equals("https") ? 443 : 80;
                        token = next;
                    } else {
                        port = Integer.parseInt(next);
                        token = st.nextToken();
                    }
                    String walletId = st.nextToken();
                    String walletPassphrase = st.nextToken();

                    InetSocketAddress tunnelAddress = ctx.getTunnelManager().connectIfNeeded(walletLogin, tunnelPassword, InetSocketAddress.createUnresolved(host, port));
                    host = tunnelAddress.getHostString();
                    port = tunnelAddress.getPort();

                    String blocks;
                    int num;
                    Integer numBlocks = 2;
                    if(st.hasMoreTokens()){
                        blocks = st.nextToken();
                        num = Integer.parseInt(blocks);
                        if(num > 2) {
                            numBlocks = num;
                        }
                    }

                    if (BITGO_WALLET_NO_FORWARD.equalsIgnoreCase(walletType)) {
                        return new BitgoWalletWithUniqueAddresses(scheme, host, port, token, walletId, walletPassphrase, numBlocks);
                    }

                    return new BitgoWallet(scheme, host, port, token, walletId, walletPassphrase, numBlocks);

                }
            }
        } catch (Exception e) {
            log.error("Unable to create wallet", e);
        }
        return null;
    }
}
