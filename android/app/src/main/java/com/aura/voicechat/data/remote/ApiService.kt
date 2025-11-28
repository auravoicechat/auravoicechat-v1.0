package com.aura.voicechat.data.remote

import com.aura.voicechat.data.model.*
import retrofit2.Response
import retrofit2.http.*

/**
 * API Service interface for Retrofit
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface ApiService {
    
    // Authentication
    @POST("auth/otp/send")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<SendOtpResponse>
    
    @POST("auth/otp/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>
    
    // Daily Rewards
    @GET("rewards/daily/status")
    suspend fun getDailyRewardStatus(): Response<DailyRewardStatusResponse>
    
    @POST("rewards/daily/claim")
    suspend fun claimDailyReward(): Response<ClaimRewardResponse>
    
    // VIP
    @GET("vip/tier")
    suspend fun getVipTier(): Response<VipTierResponse>
    
    @POST("vip/purchase")
    suspend fun purchaseVip(@Body request: PurchaseVipRequest): Response<Unit>
    
    // Medals
    @GET("profile/medals")
    suspend fun getUserMedals(): Response<MedalsResponse>
    
    @POST("profile/medals/display")
    suspend fun updateMedalDisplay(@Body request: UpdateMedalDisplayRequest): Response<Unit>
    
    @GET("users/{userId}/medals")
    suspend fun getOtherUserMedals(@Path("userId") userId: String): Response<MedalsResponse>
    
    // Wallet
    @GET("wallet/balances")
    suspend fun getWalletBalances(): Response<WalletBalancesResponse>
    
    @POST("wallet/exchange")
    suspend fun exchangeDiamondsToCoins(@Body request: ExchangeRequest): Response<ExchangeResponse>
    
    // Referrals - Get Coins
    @POST("referrals/bind")
    suspend fun bindReferralCode(@Body request: BindReferralRequest): Response<Unit>
    
    @GET("referrals/coins/summary")
    suspend fun getReferralCoinsSummary(): Response<ReferralCoinsSummaryResponse>
    
    @POST("referrals/coins/withdraw")
    suspend fun withdrawReferralCoins(@Body request: WithdrawCoinsRequest): Response<Unit>
    
    @GET("referrals/records")
    suspend fun getReferralRecords(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int
    ): Response<ReferralRecordsResponse>
    
    // Referrals - Get Cash
    @GET("referrals/cash/summary")
    suspend fun getReferralCashSummary(): Response<ReferralCashSummaryResponse>
    
    @POST("referrals/cash/withdraw")
    suspend fun withdrawReferralCash(@Body request: WithdrawCashRequest): Response<Unit>
    
    @GET("referrals/cash/invite-record")
    suspend fun getCashInviteRecords(
        @Query("weekStart") weekStart: String,
        @Query("page") page: Int
    ): Response<InviteRecordsResponse>
    
    @GET("referrals/cash/ranking")
    suspend fun getCashRanking(@Query("page") page: Int): Response<RankingResponse>
    
    // Rooms
    @GET("rooms/popular")
    suspend fun getPopularRooms(): Response<RoomsResponse>
    
    @GET("rooms/mine")
    suspend fun getMyRooms(): Response<RoomsResponse>
    
    @GET("rooms/{roomId}")
    suspend fun getRoom(@Path("roomId") roomId: String): Response<RoomResponse>
    
    @POST("rooms/{roomId}/video/playlist")
    suspend fun addToPlaylist(
        @Path("roomId") roomId: String,
        @Body request: AddToPlaylistRequest
    ): Response<Unit>
    
    @POST("rooms/{roomId}/video/exit")
    suspend fun exitVideo(@Path("roomId") roomId: String): Response<Unit>
    
    // User Profile
    @GET("users/{userId}")
    suspend fun getUser(@Path("userId") userId: String): Response<UserResponse>
    
    @PUT("users/me")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<Unit>
    
    // KYC
    @GET("kyc/status")
    suspend fun getKycStatus(): Response<KycStatusResponse>
    
    @POST("kyc/submit")
    suspend fun submitKyc(@Body request: SubmitKycRequest): Response<Unit>
}
