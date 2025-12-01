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
    @POST("api/v1/auth/otp/send")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<SendOtpResponse>
    
    @POST("api/v1/auth/otp/verify")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>
    
    @POST("api/v1/auth/refresh")
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>
    
    @POST("api/v1/auth/logout")
    suspend fun logout(): Response<LogoutResponse>
    
    @POST("api/v1/auth/google")
    suspend fun signInWithGoogle(@Body request: GoogleSignInRequest): Response<SocialSignInResponse>
    
    @POST("api/v1/auth/facebook")
    suspend fun signInWithFacebook(@Body request: FacebookSignInRequest): Response<SocialSignInResponse>
    
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
    
    // ============================================
    // Games - New Game Types
    // ============================================
    
    // Get available games
    @GET("games")
    suspend fun getAvailableGames(): Response<GamesListResponse>
    
    // Get game stats
    @GET("games/stats")
    suspend fun getGameStats(): Response<GameStatsResponse>
    
    // Get jackpots
    @GET("games/jackpots")
    suspend fun getJackpots(): Response<JackpotsResponse>
    
    @GET("games/jackpots/{gameType}")
    suspend fun getJackpot(@Path("gameType") gameType: String): Response<JackpotDto>
    
    // Start game session
    @POST("games/{gameType}/start")
    suspend fun startGame(
        @Path("gameType") gameType: String,
        @Body request: StartGameRequest
    ): Response<GameSessionResponse>
    
    // Perform game action
    @POST("games/{gameType}/action")
    suspend fun gameAction(
        @Path("gameType") gameType: String,
        @Body request: GameActionRequest
    ): Response<GameResultResponse>
    
    // Get game history
    @GET("games/{gameType}/history")
    suspend fun getGameHistory(
        @Path("gameType") gameType: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<GiftWheelRecordsResponse>
    
    // Get Gift Wheel draw records
    @GET("games/gift-wheel/draw-records")
    suspend fun getGiftWheelRecords(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<GiftWheelRecordsResponse>
    
    // ============================================
    // Gifts - Gift Catalog and Sending
    // ============================================
    
    // Get gift catalog
    @GET("gifts/catalog")
    suspend fun getGiftCatalog(
        @Query("region") region: String? = null,
        @Query("category") category: String? = null
    ): Response<GiftCatalogResponse>
    
    // Send gift
    @POST("gifts/send")
    suspend fun sendGift(@Body request: GiftSendRequestDto): Response<GiftSendResponseDto>
    
    // Send baggage gift (free)
    @POST("gifts/send/baggage")
    suspend fun sendBaggageGift(@Body request: BaggageSendRequestDto): Response<GiftSendResponseDto>
    
    // Get gift transaction history
    @GET("gifts/history")
    suspend fun getGiftHistory(
        @Query("type") type: String, // "sent" or "received"
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<GiftHistoryResponse>
    
    // ============================================
    // Inventory - User's owned items
    // ============================================
    
    // Get user inventory
    @GET("profile/inventory")
    suspend fun getInventory(
        @Query("category") category: String? = null
    ): Response<InventoryResponse>
    
    // Get equipped items
    @GET("profile/inventory/equipped")
    suspend fun getEquippedItems(): Response<EquippedItemsResponse>
    
    // Equip item
    @POST("profile/inventory/equip")
    suspend fun equipItem(@Body request: EquipItemRequest): Response<Unit>
    
    // Unequip item
    @POST("profile/inventory/unequip")
    suspend fun unequipItem(@Body request: UnequipItemRequest): Response<Unit>
    
    // Get baggage (free gifts to send)
    @GET("profile/baggage")
    suspend fun getBaggage(): Response<BaggageResponse>
    
    // ============================================
    // Store - Items for purchase
    // ============================================
    
    // Get store catalog
    @GET("store/catalog")
    suspend fun getStoreCatalog(
        @Query("category") category: String? = null
    ): Response<StoreCatalogResponse>
    
    // Get featured items
    @GET("store/featured")
    suspend fun getFeaturedItems(): Response<StoreCatalogResponse>
    
    // Purchase item
    @POST("store/purchase")
    suspend fun purchaseItem(@Body request: PurchaseItemRequest): Response<PurchaseItemResponse>
    
    // Get item details
    @GET("store/items/{itemId}")
    suspend fun getStoreItem(@Path("itemId") itemId: String): Response<StoreItemDto>
}
