package ru.gorbunov.vocabulary.backend.repo.cassandra.model

import com.datastax.oss.driver.api.mapper.annotations.CqlName
import com.datastax.oss.driver.api.mapper.annotations.Entity
import com.datastax.oss.driver.api.mapper.annotations.PartitionKey
import ru.gorbunov.vocabulary.common.models.*

@Entity
data class WordCassandraDTO(
    @field:CqlName(COLUMN_ID)
    @field:PartitionKey // можно задать порядок
    var id: String? = null,
    @field:CqlName(COLUMN_ENGLISH)
    var english: String? = null,
    @field:CqlName(COLUMN_RUSSIAN)
    var russian: String? = null,
    @field:CqlName(COLUMN_PART_OF_SPEECH)
    var partOfSpeech: WordPartOfSpeech? = null,
    @field:CqlName(COLUMN_OWNER_ID)
    var ownerId: String? = null,
    @field:CqlName(COLUMN_LOCK)
    var lock: String?,
) {
    constructor(wordModel: VcblWord) : this(
        id = wordModel.id.takeIf { it != VcblWordId.NONE }?.asString(),
        english = wordModel.english.takeIf { it.isNotBlank() },
        russian = wordModel.russian.takeIf { it.isNotBlank() },
        partOfSpeech = wordModel.partOfSpeech.toTransport(),
        ownerId = wordModel.ownerId.takeIf { it != VcblUserId.NONE }?.asString(),
        lock = wordModel.lock.takeIf { it != VcblWordLock.NONE }?.asString()
    )

    fun toWordModel(): VcblWord =
        VcblWord(
            id = id?.let { VcblWordId(it) } ?: VcblWordId.NONE,
            english = english ?: "",
            russian = russian ?: "",
            partOfSpeech = partOfSpeech.fromTransport(),
            ownerId = ownerId?.let { VcblUserId(it) } ?: VcblUserId.NONE,
            lock = lock?.let { VcblWordLock(it) } ?: VcblWordLock.NONE
        )

    companion object {
        const val TABLE_NAME = "vocabulary_words"

        const val COLUMN_ID = "id"
        const val COLUMN_ENGLISH = "english"
        const val COLUMN_RUSSIAN = "russian"
        const val COLUMN_OWNER_ID = "owner_id"
        const val COLUMN_PART_OF_SPEECH = "part_of_speech"
        const val COLUMN_LOCK = "lock"
    }
}
