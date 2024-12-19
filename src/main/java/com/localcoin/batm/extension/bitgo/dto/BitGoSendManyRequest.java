package com.localcoin.batm.extension.bitgo.dto;

import java.util.List;

public class BitGoSendManyRequest {
    public List<BitGoRecipient> recipients;
    public String walletPassphrase;
    public Integer numBlocks;
    public String comment;

    public BitGoSendManyRequest(List<BitGoRecipient> recipients, String walletPassphrase, String comment){
      this(recipients, walletPassphrase, comment, 2);
    }

    public BitGoSendManyRequest(List<BitGoRecipient> recipients, String walletPassphrase, String comment, Integer numBlocks) {
        this.recipients = recipients;
        this.walletPassphrase = walletPassphrase;
        this.numBlocks = numBlocks;
        this.comment = comment;
    }

    public static class BitGoRecipient {
        public String address;
        public String amount; // in satoshis

        public BitGoRecipient(String address, String amount) {
            this.address = address;
            this.amount = amount;
        }
    }
}
