package org.saudigitus.e_prescription.network

interface CredentialProvider {
    fun getUrl(): String
    fun getUsername(): String
    fun getPassword(): String
}