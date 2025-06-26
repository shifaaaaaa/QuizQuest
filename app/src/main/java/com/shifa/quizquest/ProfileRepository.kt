package com.shifa.quizquest.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.shifa.quizquest.R
import com.shifa.quizquest.datastore.ProfileData
import com.shifa.quizquest.datastore.ProfileDataStore
import kotlinx.coroutines.tasks.await

class ProfileRepository(private val context: Context, private val uid: String) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // ✅ Simpan data ke Firestore (dengan imageIndex, bukan imageRes langsung!)
    suspend fun saveProfileToFirestore(nickname: String, description: String, imageRes: Int) {
        val currentUid = auth.currentUser?.uid ?: return
        val imageIndex = imageResToIndex(imageRes) // Simpan sebagai index (1–5)

        val userMap = mapOf(
            "nickname" to nickname,
            "description" to description,
            "imageRes" to imageIndex
        )

        firestore.collection("userData")
            .document(currentUid)
            .collection("userProfile")
            .document("main")
            .set(userMap)
            .await()
    }

    // ✅ Ambil data dari Firestore → Konversi index ke imageRes
    suspend fun getProfileFromFirestore(): ProfileData? {
        val currentUid = auth.currentUser?.uid ?: return null
        val snapshot = firestore.collection("userData")
            .document(currentUid)
            .collection("userProfile")
            .document("main")
            .get()
            .await()

        return if (snapshot.exists()) {
            val nickname = snapshot.getString("nickname") ?: ""
            val description = snapshot.getString("description") ?: ""
            val imageIndex = snapshot.getLong("imageRes")?.toInt() ?: 1
            val imageRes = indexToImageRes(imageIndex)

            ProfileData(
                nickname = nickname,
                description = description,
                imageRes = imageRes
            )
        } else null
    }

    // ✅ Sinkronisasi dari cloud → ke local datastore
    suspend fun syncProfileToLocal() {
        val profile = getProfileFromFirestore()
        val currentUid = auth.currentUser?.uid ?: return
        profile?.let {
            val store = ProfileDataStore(context, currentUid)
            store.saveProfile(
                nickname = it.nickname,
                description = it.description,
                imageId = it.imageRes
            )
        }
    }

    // ✅ Sinkronisasi dari local datastore → ke cloud
    suspend fun syncProfileToCloud() {
        val currentUid = auth.currentUser?.uid ?: return
        val store = ProfileDataStore(context, currentUid)
        val local = store.getProfile()
        saveProfileToFirestore(
            nickname = local.nickname,
            description = local.description,
            imageRes = local.imageRes
        )
    }

    // ✅ Konversi resource ID → index (1–5)
    fun imageResToIndex(resId: Int): Int {
        return when (resId) {
            R.drawable.profile1 -> 1
            R.drawable.profile2 -> 2
            R.drawable.profile3 -> 3
            R.drawable.profile4 -> 4
            R.drawable.profile5 -> 5
            else -> 1
        }
    }

    // ✅ Konversi index (1–5) → resource ID
    fun indexToImageRes(index: Int): Int {
        return when (index) {
            1 -> R.drawable.profile1
            2 -> R.drawable.profile2
            3 -> R.drawable.profile3
            4 -> R.drawable.profile4
            5 -> R.drawable.profile5
            else -> R.drawable.profile1
        }
    }
}
