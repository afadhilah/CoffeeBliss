package com.aflabs.coffeebliss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CoffeeBlissDao {
    @Query("SELECT * FROM members ORDER BY name ASC")
    fun getAllMembers(): Flow<List<Member>>

    @Query("SELECT * FROM members WHERE id = :id")
    fun getMemberById(id: Int): Flow<Member?>

    @Query("SELECT * FROM members WHERE id = :id")
    suspend fun getMemberByIdSync(id: Int): Member?

    @Query("SELECT * FROM transactions WHERE memberId = :memberId ORDER BY id DESC")
    fun getTransactionsForMember(memberId: Int): Flow<List<Transaction>>

    @Query("SELECT * FROM redeem_history WHERE memberId = :memberId ORDER BY id DESC")
    fun getRedeemsForMember(memberId: Int): Flow<List<RedeemHistory>>

    @Insert
    suspend fun insertMember(member: Member): Long

    @Query("UPDATE members SET points = :newPoints WHERE id = :memberId")
    suspend fun updateMemberPoints(memberId: Int, newPoints: Int)

    @Insert
    suspend fun insertTransaction(transaction: Transaction): Long

    @Insert
    suspend fun insertRedeem(redeem: RedeemHistory): Long

    // Atomic transaction: Insert a transaction and update member points
    @androidx.room.Transaction
    suspend fun insertTransactionAndUpdatePoints(transaction: Transaction) {
        val member = getMemberByIdSync(transaction.memberId) ?: return
        val updatedPoints = member.points + transaction.pointEarned
        insertTransaction(transaction)
        updateMemberPoints(transaction.memberId, updatedPoints)
    }

    // Atomic transaction: Insert a redemption and deduct member points
    @androidx.room.Transaction
    suspend fun insertRedeemAndUpdatePoints(redeem: RedeemHistory): Boolean {
        val member = getMemberByIdSync(redeem.memberId) ?: return false
        if (member.points < redeem.pointsDeducted) {
            return false // Insufficient points
        }
        val updatedPoints = member.points - redeem.pointsDeducted
        insertRedeem(redeem)
        updateMemberPoints(redeem.memberId, updatedPoints)
        return true
    }
}
