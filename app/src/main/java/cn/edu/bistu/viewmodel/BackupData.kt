package cn.edu.bistu.viewmodel

import java.time.LocalDateTime

data class BackupData(
    val id: Int,
    val e_mail: String,
    val cTime: LocalDateTime,
    val data: String
)