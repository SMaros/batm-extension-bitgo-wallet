package com.localcoin.batm.extension.bitgo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitGoCoinRequest {
    private String address;
    private String amount;
    private String walletPassphrase;
    private Integer numBlocks;
    private String comment;
    private String type;


    public BitGoCoinRequest(String address, String amount, String walletPassphrase, String comment, Integer numBlocks) {
        this.address = address;
        this.amount = amount;
        this.walletPassphrase = walletPassphrase;
        this.numBlocks = numBlocks;
        this.comment = comment;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWalletPassphrase() {
        return walletPassphrase;
    }

    public void setWalletPassphrase(String walletPassphrase) {
        this.walletPassphrase = walletPassphrase;
    }

    public void setNumBlocks(Integer numBlocks){
        this.numBlocks = numBlocks;
    }

    public Integer getNumBlocks() {
        return numBlocks;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
