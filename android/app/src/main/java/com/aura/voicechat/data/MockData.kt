package com.aura.voicechat.data

import com.aura.voicechat.domain.model.*

/**
 * Mock Data for Testing
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Provides sample data for UI development and testing
 */
object MockData {
    
    /**
     * Sample users
     */
    val sampleUsers = listOf(
        User(
            id = "user1",
            name = "AlexGamer",
            avatar = null,
            level = 25,
            exp = 12500,
            vipTier = 1,
            vipExpiry = System.currentTimeMillis() + 2592000000L,
            coins = 5000,
            diamonds = 100,
            gender = Gender.MALE,
            country = "US",
            bio = "Love gaming and chatting!",
            isOnline = true,
            lastActiveAt = System.currentTimeMillis(),
            kycStatus = KycStatus.VERIFIED,
            cpPartnerId = null,
            familyId = null,
            createdAt = System.currentTimeMillis() - 7776000000L
        ),
        User(
            id = "user2",
            name = "MusicLover",
            avatar = null,
            level = 42,
            exp = 35000,
            vipTier = 2,
            vipExpiry = System.currentTimeMillis() + 2592000000L,
            coins = 15000,
            diamonds = 500,
            gender = Gender.FEMALE,
            country = "UK",
            bio = "Music is life!",
            isOnline = true,
            lastActiveAt = System.currentTimeMillis(),
            kycStatus = KycStatus.VERIFIED,
            cpPartnerId = null,
            familyId = null,
            createdAt = System.currentTimeMillis() - 15552000000L
        ),
        User(
            id = "user3",
            name = "ChillVibes",
            avatar = null,
            level = 18,
            exp = 8000,
            vipTier = 0,
            vipExpiry = null,
            coins = 2000,
            diamonds = 50,
            gender = Gender.UNSPECIFIED,
            country = "CA",
            bio = "Just here to chill",
            isOnline = false,
            lastActiveAt = System.currentTimeMillis() - 3600000L,
            kycStatus = KycStatus.NOT_STARTED,
            cpPartnerId = null,
            familyId = null,
            createdAt = System.currentTimeMillis() - 5184000000L
        )
    )
    
    /**
     * Sample rooms
     */
    val sampleRooms = listOf(
        RoomCard(
            id = "room1",
            name = "Gaming Hangout 🎮",
            coverImage = null,
            ownerName = "AlexGamer",
            ownerAvatar = null,
            type = RoomType.VOICE,
            userCount = 8,
            capacity = 8,
            isLive = true,
            tags = listOf("gaming", "fun", "casual")
        ),
        RoomCard(
            id = "room2",
            name = "Music Lounge 🎵",
            coverImage = null,
            ownerName = "MusicLover",
            ownerAvatar = null,
            type = RoomType.MUSIC,
            userCount = 12,
            capacity = 16,
            isLive = true,
            tags = listOf("music", "chill", "24/7")
        ),
        RoomCard(
            id = "room3",
            name = "Chill & Chat 💬",
            coverImage = null,
            ownerName = "ChillVibes",
            ownerAvatar = null,
            type = RoomType.VOICE,
            userCount = 5,
            capacity = 8,
            isLive = true,
            tags = listOf("chill", "chat", "friendly")
        ),
        RoomCard(
            id = "room4",
            name = "Party Time 🎉",
            coverImage = null,
            ownerName = "PartyKing",
            ownerAvatar = null,
            type = RoomType.VOICE,
            userCount = 16,
            capacity = 16,
            isLive = true,
            tags = listOf("party", "fun", "active")
        ),
        RoomCard(
            id = "room5",
            name = "Dating Corner 💕",
            coverImage = null,
            ownerName = "LoveSeeker",
            ownerAvatar = null,
            type = RoomType.VOICE,
            userCount = 6,
            capacity = 8,
            isLive = true,
            tags = listOf("dating", "romance", "singles")
        ),
        RoomCard(
            id = "room6",
            name = "Video Chat 📹",
            coverImage = null,
            ownerName = "VideoStar",
            ownerAvatar = null,
            type = RoomType.VIDEO,
            userCount = 4,
            capacity = 8,
            isLive = true,
            tags = listOf("video", "chat", "live")
        )
    )
    
    /**
     * Sample gifts
     */
    val sampleGifts = listOf(
        Gift(
            id = "gift1",
            name = "Rose",
            description = "A beautiful rose",
            category = GiftCategory.LOVE,
            price = 10,
            diamondValue = 5,
            rarity = GiftRarity.COMMON,
            iconUrl = null,
            animationFile = null,
            soundFile = null,
            isAnimated = false,
            isFullScreen = false,
            isCustom = false,
            isLegendary = false,
            duration = 2000,
            enabled = true,
            regions = listOf("all")
        ),
        Gift(
            id = "gift2",
            name = "Diamond Ring",
            description = "A precious diamond ring",
            category = GiftCategory.LUXURY,
            price = 500,
            diamondValue = 250,
            rarity = GiftRarity.EPIC,
            iconUrl = null,
            animationFile = null,
            soundFile = null,
            isAnimated = true,
            isFullScreen = false,
            isCustom = false,
            isLegendary = false,
            duration = 3000,
            enabled = true,
            regions = listOf("all")
        ),
        Gift(
            id = "gift3",
            name = "Fireworks",
            description = "Spectacular fireworks",
            category = GiftCategory.CELEBRATION,
            price = 1000,
            diamondValue = 500,
            rarity = GiftRarity.LEGENDARY,
            iconUrl = null,
            animationFile = null,
            soundFile = null,
            isAnimated = true,
            isFullScreen = true,
            isCustom = false,
            isLegendary = true,
            duration = 5000,
            enabled = true,
            regions = listOf("all")
        ),
        Gift(
            id = "gift4",
            name = "Heart",
            description = "A lovely heart",
            category = GiftCategory.LOVE,
            price = 20,
            diamondValue = 10,
            rarity = GiftRarity.COMMON,
            iconUrl = null,
            animationFile = null,
            soundFile = null,
            isAnimated = false,
            isFullScreen = false,
            isCustom = false,
            isLegendary = false,
            duration = 2000,
            enabled = true,
            regions = listOf("all")
        ),
        Gift(
            id = "gift5",
            name = "Crown",
            description = "A golden crown",
            category = GiftCategory.LUXURY,
            price = 300,
            diamondValue = 150,
            rarity = GiftRarity.RARE,
            iconUrl = null,
            animationFile = null,
            soundFile = null,
            isAnimated = true,
            isFullScreen = false,
            isCustom = false,
            isLegendary = false,
            duration = 3000,
            enabled = true,
            regions = listOf("all")
        )
    )
    
    /**
     * Sample mic seats for a room
     */
    fun getSampleMicSeats(): List<MicSeat> {
        return listOf(
            MicSeat(index = 0, user = sampleUsers[0], isMuted = false, isSpeaking = true, isLocked = false),
            MicSeat(index = 1, user = sampleUsers[1], isMuted = false, isSpeaking = false, isLocked = false),
            MicSeat(index = 2, user = sampleUsers[2], isMuted = true, isSpeaking = false, isLocked = false),
            MicSeat(index = 3, user = null, isMuted = false, isSpeaking = false, isLocked = false),
            MicSeat(index = 4, user = null, isMuted = false, isSpeaking = false, isLocked = false),
            MicSeat(index = 5, user = null, isMuted = false, isSpeaking = false, isLocked = false),
            MicSeat(index = 6, user = null, isMuted = false, isSpeaking = false, isLocked = true),
            MicSeat(index = 7, user = null, isMuted = false, isSpeaking = false, isLocked = false)
        )
    }
    
    /**
     * Sample room messages
     */
    fun getSampleRoomMessages(): List<RoomMessage> {
        val currentTime = System.currentTimeMillis()
        return listOf(
            RoomMessage.SystemMessage(
                id = "msg1",
                timestamp = currentTime - 300000,
                text = "Welcome to the room!"
            ),
            RoomMessage.UserMessage(
                id = "msg2",
                timestamp = currentTime - 240000,
                user = sampleUsers[0],
                text = "Hey everyone! Ready to chat?"
            ),
            RoomMessage.UserMessage(
                id = "msg3",
                timestamp = currentTime - 180000,
                user = sampleUsers[1],
                text = "Hello! Great to be here 😊"
            ),
            RoomMessage.GiftMessage(
                id = "msg4",
                timestamp = currentTime - 120000,
                sender = sampleUsers[0],
                receiver = sampleUsers[1],
                gift = sampleGifts[0],
                quantity = 1
            ),
            RoomMessage.UserMessage(
                id = "msg5",
                timestamp = currentTime - 60000,
                user = sampleUsers[2],
                text = "This room is awesome!"
            ),
            RoomMessage.SystemMessage(
                id = "msg6",
                timestamp = currentTime - 30000,
                text = "AlexGamer joined seat 1"
            ),
            RoomMessage.UserMessage(
                id = "msg7",
                timestamp = currentTime - 10000,
                user = sampleUsers[0],
                text = "Let's have some fun! 🎉"
            )
        )
    }
}
