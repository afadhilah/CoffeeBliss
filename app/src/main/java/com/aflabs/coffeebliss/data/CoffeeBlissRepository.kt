package com.aflabs.coffeebliss.data

import kotlinx.coroutines.flow.Flow

class CoffeeBlissRepository(private val dao: CoffeeBlissDao) {
    val allMembers: Flow<List<Member>> = dao.getAllMembers()

    fun getMemberById(id: Int): Flow<Member?> = dao.getMemberById(id)

    fun getTransactionsForMember(memberId: Int): Flow<List<Transaction>> =
        dao.getTransactionsForMember(memberId)

    fun getRedeemsForMember(memberId: Int): Flow<List<RedeemHistory>> =
        dao.getRedeemsForMember(memberId)

    suspend fun insertMember(member: Member): Long = dao.insertMember(member)

    suspend fun insertTransaction(transaction: Transaction) {
        dao.insertTransactionAndUpdatePoints(transaction)
    }

    suspend fun insertRedeem(redeem: RedeemHistory): Boolean {
        return dao.insertRedeemAndUpdatePoints(redeem)
    }
}
