package ru.gorbunov.vocabulary.app.common

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ru.gorbunov.vocabulary.common.models.VcblUserId
import ru.gorbunov.vocabulary.common.permissions.VcblPrincipalModel
import ru.gorbunov.vocabulary.common.permissions.VcblUserGroups
import java.util.Base64

const val AUTH_HEADER: String = "x-jwt-payload"

fun String?.jwt2principal(): VcblPrincipalModel = this?.let { jwtHeader ->
    val jwtJson = String(Base64.getDecoder().decode(jwtHeader), Charsets.UTF_8)
    println("JWT JSON PAYLOAD: $jwtJson")
    val jwtObj = mapper.readValue<JwtPayload>(jwtJson)
    jwtObj.toPrincipal()
}
    ?: run {
        println("No jwt found in headers")
        VcblPrincipalModel.NONE
    }

fun VcblPrincipalModel.createJwtTestHeader(): String {
    val jwtObj = fromPrincipal()
    val jwtJson = mapper.writeValueAsString(jwtObj)
    return Base64.getEncoder().encodeToString(jwtJson.toByteArray(Charsets.UTF_8))
}

private val mapper = jacksonObjectMapper()
    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

private data class JwtPayload(
    val aud: List<String>? = null,
    val sub: String? = null,
    @JsonProperty("family_name")
    val familyName: String? = null,
    @JsonProperty("given_name")
    val givenName: String? = null,
    @JsonProperty("middle_name")
    val middleName: String? = null,
    val groups: List<String>? = null,
)

private fun JwtPayload.toPrincipal(): VcblPrincipalModel = VcblPrincipalModel(
    id = sub?.let { VcblUserId(it) } ?: VcblUserId.NONE,
    fname = givenName ?: "",
    mname = middleName ?: "",
    lname = familyName ?: "",
    groups = groups?.mapNotNull { it.toPrincipalGroup() }?.toSet() ?: emptySet(),
)

private fun VcblPrincipalModel.fromPrincipal(): JwtPayload = JwtPayload(
    sub = id.takeIf { it != VcblUserId.NONE }?.asString(),
    givenName = fname.takeIf { it.isNotBlank() },
    middleName = mname.takeIf { it.isNotBlank() },
    familyName = lname.takeIf { it.isNotBlank() },
    groups = groups.mapNotNull { it.fromPrincipalGroup() }.toList().takeIf { it.isNotEmpty() } ?: emptyList(),
)

private fun String?.toPrincipalGroup(): VcblUserGroups? = when (this?.uppercase()) {
    "USER" -> VcblUserGroups.USER
    "ADMIN" -> VcblUserGroups.ADMIN
    "TEST" -> VcblUserGroups.TEST
    // TODO сделать обработку ошибок
    else -> null
}

private fun VcblUserGroups?.fromPrincipalGroup(): String? = when (this) {
    VcblUserGroups.USER -> "USER"
    VcblUserGroups.ADMIN -> "ADMIN"
    VcblUserGroups.TEST -> "TEST"
    // TODO сделать обработку ошибок
    else -> null
}