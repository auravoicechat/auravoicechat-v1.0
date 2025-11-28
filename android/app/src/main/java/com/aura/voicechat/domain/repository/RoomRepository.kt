package com.aura.voicechat.domain.repository

import com.aura.voicechat.domain.model.Room
import com.aura.voicechat.domain.model.RoomCard

/**
 * Room Repository interface
 * Developer: Hawkaye Visions LTD — Pakistan
 */
interface RoomRepository {
    suspend fun getPopularRooms(): Result<List<RoomCard>>
    suspend fun getMyRooms(): Result<List<RoomCard>>
    suspend fun getRoom(roomId: String): Result<Room>
    suspend fun createRoom(name: String, capacity: Int): Result<Room>
    suspend fun joinRoom(roomId: String): Result<Unit>
    suspend fun leaveRoom(roomId: String): Result<Unit>
    suspend fun joinSeat(roomId: String, position: Int): Result<Unit>
    suspend fun leaveSeat(roomId: String): Result<Unit>
}
