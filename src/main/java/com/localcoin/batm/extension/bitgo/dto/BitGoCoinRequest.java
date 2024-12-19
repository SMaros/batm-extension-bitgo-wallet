package com.localcoin.batm.extension.bitgo.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BitGoCoinRequest {
    private String address;
    private String amount;
    private String walletPassphrase;
    private Integer numBlocks;
    private String comment;
    private Integer feeRate;
    private Integer maxFeeRate;


    public BitGoCoinRequest(String address, String amount, String walletPassphrase, String comment, Integer numBlocks) {
        this.address = address;
        this.amount = amount;
        this.walletPassphrase = walletPassphrase;
        this.numBlocks = numBlocks;
        this.comment = comment;
    }

    public BitGoCoinRequest(String address, String amount, String walletPassphrase, String comment, Integer numBlocks, Integer feeRate, Integer maxFeeRate) {
        this(address, amount, walletPassphrase, comment, numBlocks);

        this.feeRate = feeRate;
        this.maxFeeRate = maxFeeRate;
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

    public Integer getFeeRate() {
        return feeRate;
    }

    public void setFeeRate(Integer feeRate) {
        this.feeRate = feeRate;
    }

    public Integer getMaxFeeRate() {
        return maxFeeRate;
    }

    public void setMaxFeeRate(Integer maxFeeRate) {
        this.maxFeeRate = maxFeeRate;
    }
}
