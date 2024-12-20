package com.localcoin.batm.extension.bitgo;

import com.generalbytes.batm.common.currencies.CryptoCurrency;
import com.generalbytes.batm.server.extensions.IGeneratesNewDepositCryptoAddress;
import com.generalbytes.batm.server.extensions.IQueryableWallet;
import com.generalbytes.batm.server.extensions.payment.ReceivedAmount;
import com.localcoin.batm.extension.bitgo.dto.BitGoAddressResponse;
import com.localcoin.batm.extension.bitgo.dto.BitGoCreateAddressRequest;
import com.localcoin.batm.extension.bitgo.dto.ErrorResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import si.mazi.rescu.HttpStatusIOException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class BitgoWalletWithUniqueAddresses extends BitgoWallet implements IGeneratesNewDepositCryptoAddress, IQueryableWallet {
    private static final Logger log = LoggerFactory.getLogger(BitgoWalletWithUniqueAddresses.class);

    private static final Map<String, String> newAddressCryptoCurrency = new HashMap<String, String>() {
        {
            put(CryptoCurrency.USDT.getCode(), "eth");
            put(CryptoCurrency.USDTTRON.getCode(), "trx");
        }
    };

    public BitgoWalletWithUniqueAddresses(String scheme, String host, int port, String token, String walletId, String walletPassphrase, Integer numBlocks) {
        super(scheme, host, port, token, walletId, walletPassphrase, numBlocks);
    }

    @Override
    public String generateNewDepositCryptoAddress(String cryptoCurrency, String label) {
        if (cryptoCurrency == null) {
            cryptoCurrency = getPreferredCryptoCurrency();
        }
        String bitgoCryptoCurrency = newAddressCryptoCurrency.getOrDefault(cryptoCurrency, cryptoCurrencies.get(cryptoCurrency));
        if (bitgoCryptoCurrency == null) {
            return null;
        }
        try {

            final BitGoCreateAddressRequest request = new BitGoCreateAddressRequest();
            request.setChain(0); // https://github.com/BitGo/unspents/blob/master/src/codes.ts ??? [0, UnspentType.p2sh, Purpose.external],
            request.setLabel(label);
            final BitGoAddressResponse response = api.createAddress(bitgoCryptoCurrency, walletId, request);
            if (response == null) {
                return null;
            }
            String address = response.getAddress();
            if (address == null || address.isEmpty()) {
                log.error("address missing in response: '{}'", address);
                return null;
            }
            return address;
        } catch (HttpStatusIOException hse) {
            log.debug("create address error: {}", hse.getHttpBody());
        } catch (ErrorResponseException e) {
            log.debug("create address error, HTTP status: {}, error: {}", e.getHttpStatusCode(), e.getMessage());
        } catch (Exception e) {
            log.error("create address error", e);
        }
        return null;
    }

    @Override
    public ReceivedAmount getReceivedAmount(String address, String cryptoCurrency) {
        String bitgoCryptoCurrency = cryptoCurrencies.get(cryptoCurrency);
        if (bitgoCryptoCurrency == null) {
            log.error("Wallet supports only {}, not {}", getCryptoCurrencies(), cryptoCurrency);
            return ReceivedAmount.ZERO;
        }

        try {
            BitGoAddressResponse resp = api.getAddress(bitgoCryptoCurrency, walletId, address);
            if (resp.getBalance().getConfirmedBalance().compareTo(BigDecimal.ZERO) > 0) {
                return new ReceivedAmount(fromSatoshis(cryptoCurrency, resp.getBalance().getConfirmedBalance()), 999);
            }
            return new ReceivedAmount(fromSatoshis(cryptoCurrency, resp.getBalance().getBalance()), 0);
        } catch (HttpStatusIOException e) {
            log.debug("get address error: {}", e.getHttpBody());
        } catch (ErrorResponseException e) {
            log.debug("get address error, HTTP status: {}, error: {}", e.getHttpStatusCode(), e.getMessage());
        } catch (Exception e) {
            log.error("get address error", e);
        }
        return ReceivedAmount.ZERO;
    }
}
