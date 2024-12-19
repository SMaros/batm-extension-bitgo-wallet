package com.localcoin.batm.extension.bitgo;

import com.localcoin.batm.extension.bitgo.dto.BitGoAddressResponse;
import com.localcoin.batm.extension.bitgo.dto.BitGoCoinRequest;
import com.localcoin.batm.extension.bitgo.dto.BitGoCreateAddressRequest;
import com.localcoin.batm.extension.bitgo.dto.BitGoSendManyRequest;
import com.localcoin.batm.extension.bitgo.dto.ErrorResponseException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.Map;

@Path("/api/v2")
@Produces(MediaType.APPLICATION_JSON)
public interface IBitgoAPI {

    @POST
    @Path("/{coin}/wallet/{id}/sendmany")
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Object> sendMany(@PathParam("coin") String coin, @PathParam("id") String id, BitGoSendManyRequest request) throws IOException, ErrorResponseException;

    @POST
    @Path("/{coin}/wallet/{id}/sendcoins")
    @Consumes(MediaType.APPLICATION_JSON)
    Map<String, Object> sendCoins(@PathParam("coin") String coin, @PathParam("id") String id, BitGoCoinRequest request) throws IOException, ErrorResponseException;

    @GET
    @Path("/{coin}/wallet/balances")
    Map<String, Object> getTotalBalances(@PathParam("coin") String coin) throws IOException, ErrorResponseException;

    @GET
    @Path("/{coin}/wallet")
    Map<String, Object> getWallets(@PathParam("coin") String coin) throws IOException, ErrorResponseException;

    @GET
    @Path("/{coin}/wallet/{id}")
    Map<String, Object> getWalletById(@PathParam("coin") String coin, @PathParam("id") String id) throws IOException, ErrorResponseException;

    @GET
    @Path("/{coin}/wallet/{walletId}/address/{addressOrId}")
    BitGoAddressResponse getAddress(@PathParam("coin") String coin, @PathParam("walletId") String walletId, @PathParam("addressOrId") String addressOrId) throws IOException, ErrorResponseException;

    @POST
    @Path("/{coin}/wallet/{id}/address")
    @Consumes(MediaType.APPLICATION_JSON)
    BitGoAddressResponse createAddress(@PathParam("coin") String coin, @PathParam("id") String id, BitGoCreateAddressRequest request) throws IOException, ErrorResponseException;
}