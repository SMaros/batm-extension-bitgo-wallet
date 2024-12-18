package com.localcoin.batm.extension.bitgo;

import java.net.InetSocketAddress;
import java.util.StringTokenizer;

public class LCBitGoExtension extends AbstractExtension {
    private IExtensionContext ctx;

    private static final String BITGO_WALLET = "lcbitgo";

    private static final String BITGO_WALLET_NO_FORWARD = "lcbitgonoforward";

    @Override
    public void init(IExtensionContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public String getName() {
        return "Localcoin Fireblocks Extension";
    }

    @Override
    public IWallet createWallet(String walletLogin, String tunnelPassword) {
        try{
            if (walletLogin !=null && !walletLogin.trim().isEmpty()) {
                StringTokenizer st = new StringTokenizer(walletLogin,":");
                String walletType = st.nextToken();
                if (BITGO_WALLET.equalsIgnoreCase(walletType) || BITGO_WALLET_NO_FORWARD.equalsIgnoreCase(walletType)) {
                    // BitGo API Specification: https://developers.bitgo.com/api/express.wallet.sendcoins
                    //
                    // bitgo:host:port:token:wallet_address:wallet_passphrase:num_blocks:fee_rate:max_fee_rate
                    // but host is optionally including the "http://" and port is optional,
                    // num_blocks is an optional integer greater than 2 and it's used to calculate mining fee,
                    // fee_rate is an optional integer defined fee rate,
                    // max_fee_rate is an optional integer defined maximum fee rate.
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

                    int numBlocks = 2;
                    if (st.hasMoreTokens()) {
                        int number = Integer.parseInt(st.nextToken());
                        if (number > 2) {
                            numBlocks = number;
                        }
                    }

                    Integer feeRate = st.hasMoreTokens() ? Integer.parseInt(st.nextToken()) : null;
                    Integer maxFeeRate = st.hasMoreTokens() ? Integer.parseInt(st.nextToken()) : null;
                    if (feeRate != null && (maxFeeRate == null || feeRate > maxFeeRate)) {
                        maxFeeRate = feeRate;
                    }

                    if (BITGO_WALLET_NO_FORWARD.equalsIgnoreCase(walletType)) {
                        return new BitgoWalletWithUniqueAddresses(scheme, host, port, token, walletId, walletPassphrase, numBlocks, feeRate, maxFeeRate);
                    }

                    return new BitgoWallet(scheme, host, port, token, walletId, walletPassphrase, numBlocks, feeRate, maxFeeRate);

                }
            }
        } catch (Exception e) {
            ExtensionsUtil.logExtensionParamsException("createWallet", getClass().getSimpleName(), walletLogin, e);
        }
        return null;
    }
}
