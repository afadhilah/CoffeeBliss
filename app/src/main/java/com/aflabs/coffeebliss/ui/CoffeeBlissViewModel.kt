package com.aflabs.coffeebliss.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.aflabs.coffeebliss.data.CoffeeBlissRepository
import com.aflabs.coffeebliss.data.Member
import com.aflabs.coffeebliss.data.RedeemHistory
import com.aflabs.coffeebliss.data.Transaction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalCoroutinesApi::class)
class CoffeeBlissViewModel(private val repository: CoffeeBlissRepository) : ViewModel() {

    val allMembers: StateFlow<List<Member>> = repository.allMembers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentMemberId = MutableStateFlow<Int?>(null)
    val currentMemberId: StateFlow<Int?> = _currentMemberId.asStateFlow()

    val currentMember: StateFlow<Member?> = _currentMemberId
        .flatMapLatest { id ->
            if (id == null) flowOf(null)
            else repository.getMemberById(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val transactionHistory: StateFlow<List<Transaction>> = _currentMemberId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getTransactionsForMember(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val redeemHistory: StateFlow<List<RedeemHistory>> = _currentMemberId
        .flatMapLatest { id ->
            if (id == null) flowOf(emptyList())
            else repository.getRedeemsForMember(id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Form inputs for registration
    val regName = MutableStateFlow("")
    val regEmail = MutableStateFlow("")
    val regPhone = MutableStateFlow("")

    // Form inputs for new transaction
    val txAmount = MutableStateFlow("")

    fun selectMember(id: Int?) {
        _currentMemberId.value = id
    }

    fun registerMember(onSuccess: () -> Unit, onError: (String) -> Unit) {
        val name = regName.value.trim()
        val email = regEmail.value.trim()
        val phone = regPhone.value.trim()

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            onError("Semua field harus diisi!")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            onError("Format Email tidak valid!")
            return
        }

        viewModelScope.launch {
            val member = Member(name = name, email = email, phone = phone, points = 0)
            val newId = repository.insertMember(member)
            selectMember(newId.toInt())
            // Clear fields
            regName.value = ""
            regEmail.value = ""
            regPhone.value = ""
            onSuccess()
        }
    }

    fun addTransaction(onSuccess: (Int) -> Unit, onError: (String) -> Unit) {
        val memberId = _currentMemberId.value
        if (memberId == null) {
            onError("Tidak ada member yang aktif!")
            return
        }
        val amountStr = txAmount.value.trim()
        val amount = amountStr.toDoubleOrNull()

        if (amount == null || amount <= 0) {
            onError("Nominal transaksi harus berupa angka positif!")
            return
        }

        val pointEarned = (amount / 10000).toInt()
        val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            val transaction = Transaction(
                memberId = memberId,
                amount = amount,
                pointEarned = pointEarned,
                date = dateStr
            )
            repository.insertTransaction(transaction)
            txAmount.value = ""
            onSuccess(pointEarned)
        }
    }

    fun redeemReward(rewardName: String, pointsCost: Int, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val memberId = _currentMemberId.value
        if (memberId == null) {
            onError("Tidak ada member yang aktif!")
            return
        }

        val dateStr = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())

        viewModelScope.launch {
            val redeem = RedeemHistory(
                memberId = memberId,
                rewardName = rewardName,
                pointsDeducted = pointsCost,
                date = dateStr
            )
            val success = repository.insertRedeem(redeem)
            if (success) {
                onSuccess()
            } else {
                onError("Poin tidak cukup untuk menukarkan $rewardName!")
            }
        }
    }
}

class CoffeeBlissViewModelFactory(private val repository: CoffeeBlissRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeBlissViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoffeeBlissViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
