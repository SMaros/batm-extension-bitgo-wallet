package com.localcoin.batm.extension.bitgo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class BitGoAddressResponse {
    private String id;
    private String address;
    private Integer index;
    private String coin;
    private String wallet;
    private String label;
    private Balance balance;

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public Integer getIndex() {
        return index;
    }

    public String getCoin() {
        return coin;
    }

    public String getWallet() {
        return wallet;
    }

    public String getLabel() {
        return label;
    }

    public Balance getBalance() {
        return balance;
    }

    public static class Balance {
        @JsonProperty("confirmedBalanceString")
        private BigDecimal confirmedBalance;
        @JsonProperty("balanceString")
        private BigDecimal balance;

        /**
         * @return confirmed balance in the smallest unit (e.g. satoshi)
         */
        public BigDecimal getConfirmedBalance() {
            return confirmedBalance;
        }

        /**
         * @return balance in the smallest unit (e.g. satoshi)
         */
        public BigDecimal getBalance() {
            return balance;
        }
    }
}
