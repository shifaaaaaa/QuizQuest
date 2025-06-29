package com.shifa.quizquest

import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

data class Question(
    val text: String,
    val choices: List<String>,
    val correctAnswer: String
)

object QuizRepository {

    // HANYA ADA SATU FUNGSI saveQuizResult
    suspend fun saveQuizResult(result: QuizResultData) {
        val db = Firebase.firestore
        try {
            db.collection("quizResults").add(result).await()
        } catch (e: Exception) {
            println("Error saving quiz result: ${e.message}")
        }
    }

    suspend fun getRecentResultsForUser(userId: String, limit: Long = 5): List<QuizResultData> {
        val db = Firebase.firestore
        val results = mutableListOf<QuizResultData>()
        try {
            val querySnapshot = db.collection("quizResults")
                .whereEqualTo("userId", userId)
                .orderBy("completedAt", Query.Direction.DESCENDING)
                .limit(limit)
                .get()
                .await()
            for (document in querySnapshot.documents) {
                document.toObject(QuizResultData::class.java)?.let {
                    results.add(it)
                }
            }
        } catch (e: Exception) {
            println("Error fetching recent results: ${e.message}")
        }
        return results
    }

    fun getQuestionsByQuizId(quizId: Int): List<Question> {
        return when (quizId) {
            1 -> getMusicQuestions() // Quiz Musik
            2 -> getMovieQuestions() // Quiz Film
            3 -> getHistoryQuestions() // Sejarah
            4 -> getEnglishQuestions() // Bahasa Inggris
            5 -> getMathQuestions() // Matematika Dasar
            6 -> getGeneralQuestions() // Trivia Umum
            7 -> getComedyQuestions() // Lawak & Humor
            8 -> getImageGuessQuestions() // Tebak Gambar
            9 -> getCultureQuestions() // Kebudayaan Indonesia
            else -> getGeneralQuestions() // Default
        }
    }

    suspend fun saveQuizResult(result: QuizResult) {
        val db = Firebase.firestore
        try {
            db.collection("quizResults").add(result).await()
        } catch (e: Exception) {
            println("Error saving quiz result: ${e.message}")
        }
    }

    private fun getMusicQuestions(): List<Question> {
        return listOf(
            Question(
                "Siapa penyanyi lagu 'Bohemian Rhapsody'?",
                listOf("The Beatles", "Queen", "Led Zeppelin", "Pink Floyd"),
                "Queen"
            ),
            Question(
                "Alat musik tradisional Indonesia yang berasal dari Jawa adalah?",
                listOf("Angklung", "Gamelan", "Sasando", "Tifa"),
                "Gamelan"
            ),
            Question(
                "Genre musik yang berasal dari Jamaica adalah?",
                listOf("Jazz", "Blues", "Reggae", "Rock"),
                "Reggae"
            ),
            Question(
                "Siapa komposer terkenal yang menciptakan 'Für Elise'?",
                listOf("Mozart", "Bach", "Beethoven", "Chopin"),
                "Beethoven"
            ),
            Question(
                "Band Indonesia yang terkenal dengan lagu 'Laskar Pelangi' adalah?",
                listOf("Sheila on 7", "Nidji", "Peterpan", "Ungu"),
                "Nidji"
            )
        )
    }

    private fun getMovieQuestions(): List<Question> {
        return listOf(
            Question(
                "Film Indonesia yang memenangkan Piala Citra terbanyak adalah?",
                listOf("Laskar Pelangi", "Habibie & Ainun", "Ada Apa Dengan Cinta", "Pengabdi Setan"),
                "Pengabdi Setan"
            ),
            Question(
                "Siapa sutradara film 'Titanic'?",
                listOf("Steven Spielberg", "James Cameron", "Christopher Nolan", "Martin Scorsese"),
                "James Cameron"
            ),
            Question(
                "Film superhero Marvel yang pertama kali dirilis adalah?",
                listOf("Iron Man", "Thor", "Captain America", "Hulk"),
                "Iron Man"
            ),
            Question(
                "Aktris Indonesia yang membintangi film 'Kartini' adalah?",
                listOf("Dian Sastrowardoyo", "Tara Basro", "Raihaanun", "Marsha Timothy"),
                "Dian Sastrowardoyo"
            ),
            Question(
                "Film animasi Disney yang menceritakan tentang seorang putri dengan rambut panjang adalah?",
                listOf("Frozen", "Moana", "Tangled", "Brave"),
                "Tangled"
            )
        )
    }

    private fun getHistoryQuestions(): List<Question> {
        return listOf(
            Question(
                "Kapan Indonesia memproklamirkan kemerdekaan?",
                listOf("17 Agustus 1945", "17 Agustus 1944", "17 Agustus 1946", "17 Agustus 1947"),
                "17 Agustus 1945"
            ),
            Question(
                "Siapa yang dijuluki sebagai 'Bapak Proklamator'?",
                listOf("Soekarno", "Mohammad Hatta", "Soekarno dan Hatta", "Soedirman"),
                "Soekarno dan Hatta"
            ),
            Question(
                "Perang Dunia II berakhir pada tahun?",
                listOf("1944", "1945", "1946", "1947"),
                "1945"
            ),
            Question(
                "Kerajaan Majapahit didirikan oleh?",
                listOf("Ken Arok", "Raden Wijaya", "Gajah Mada", "Hayam Wuruk"),
                "Raden Wijaya"
            ),
            Question(
                "Penjajahan Belanda di Indonesia berlangsung selama?",
                listOf("300 tahun", "350 tahun", "400 tahun", "250 tahun"),
                "350 tahun"
            )
        )
    }

    private fun getEnglishQuestions(): List<Question> {
        return listOf(
            Question(
                "What is the past tense of 'go'?",
                listOf("goed", "went", "gone", "going"),
                "went"
            ),
            Question(
                "Which word is a synonym of 'happy'?",
                listOf("sad", "angry", "joyful", "tired"),
                "joyful"
            ),
            Question(
                "What is the plural form of 'child'?",
                listOf("childs", "children", "childes", "child"),
                "children"
            ),
            Question(
                "Choose the correct sentence:",
                listOf("She don't like coffee", "She doesn't likes coffee", "She doesn't like coffee", "She not like coffee"),
                "She doesn't like coffee"
            ),
            Question(
                "What does 'beautiful' mean in Indonesian?",
                listOf("jelek", "cantik", "besar", "kecil"),
                "cantik"
            )
        )
    }

    private fun getMathQuestions(): List<Question> {
        return listOf(
            Question(
                "Berapa hasil dari 15 + 27?",
                listOf("42", "41", "43", "40"),
                "42"
            ),
            Question(
                "Berapa hasil dari 8 × 7?",
                listOf("54", "56", "58", "52"),
                "56"
            ),
            Question(
                "Berapa hasil dari 144 ÷ 12?",
                listOf("11", "12", "13", "14"),
                "12"
            ),
            Question(
                "Berapa hasil dari 25 - 13?",
                listOf("11", "12", "13", "14"),
                "12"
            ),
            Question(
                "Berapa akar kuadrat dari 64?",
                listOf("6", "7", "8", "9"),
                "8"
            )
        )
    }

    private fun getGeneralQuestions(): List<Question> {
        return listOf(
            Question(
                "Planet terbesar di tata surya adalah?",
                listOf("Mars", "Jupiter", "Saturnus", "Neptunus"),
                "Jupiter"
            ),
            Question(
                "Apa ibu kota Australia?",
                listOf("Sydney", "Melbourne", "Canberra", "Perth"),
                "Canberra"
            ),
            Question(
                "Berapa jumlah benua di dunia?",
                listOf("5", "6", "7", "8"),
                "7"
            ),
            Question(
                "Hewan tercepat di darat adalah?",
                listOf("Singa", "Cheetah", "Kuda", "Harimau"),
                "Cheetah"
            ),
            Question(
                "Gas yang paling banyak di atmosfer bumi adalah?",
                listOf("Oksigen", "Karbon dioksida", "Nitrogen", "Hidrogen"),
                "Nitrogen"
            )
        )
    }

    private fun getComedyQuestions(): List<Question> {
        return listOf(
            Question(
                "Siapa komedian Indonesia yang terkenal dengan gaya 'Benyamin'?",
                listOf("Benyamin Sueb", "Dono", "Kasino", "Indro"),
                "Benyamin Sueb"
            ),
            Question(
                "Grup komedi 'Warkop' terdiri dari berapa orang?",
                listOf("2", "3", "4", "5"),
                "3"
            ),
            Question(
                "Siapa yang terkenal dengan sebutan 'Raja Dangdut'?",
                listOf("Rhoma Irama", "Mansyur S", "A. Rafiq", "Elvy Sukaesih"),
                "Rhoma Irama"
            ),
            Question(
                "Acara komedi TV yang dibawakan oleh Sule dan Andre adalah?",
                listOf("OVJ", "Ini Talk Show", "Tonight Show", "Pesbukers"),
                "OVJ"
            ),
            Question(
                "Komedian yang terkenal dengan karakter 'Pak Tarno' adalah?",
                listOf("Tarzan", "Tessy", "Tarno", "Tukul"),
                "Tukul"
            )
        )
    }

    private fun getImageGuessQuestions(): List<Question> {
        return listOf(
            Question(
                "Landmark terkenal di Paris yang berbentuk menara adalah?",
                listOf("Big Ben", "Menara Eiffel", "Statue of Liberty", "Colosseum"),
                "Menara Eiffel"
            ),
            Question(
                "Bangunan terkenal di Indonesia yang merupakan candi Buddha adalah?",
                listOf("Candi Prambanan", "Candi Borobudur", "Candi Mendut", "Candi Sewu"),
                "Candi Borobudur"
            ),
            Question(
                "Hewan yang memiliki leher panjang adalah?",
                listOf("Gajah", "Jerapah", "Kuda", "Zebra"),
                "Jerapah"
            ),
            Question(
                "Buah yang berwarna kuning dan berbentuk melengkung adalah?",
                listOf("Apel", "Jeruk", "Pisang", "Mangga"),
                "Pisang"
            ),
            Question(
                "Alat transportasi yang bisa terbang adalah?",
                listOf("Mobil", "Kapal", "Pesawat", "Kereta"),
                "Pesawat"
            )
        )
    }

    private fun getCultureQuestions(): List<Question> {
        return listOf(
            Question(
                "Tarian tradisional dari Bali adalah?",
                listOf("Saman", "Kecak", "Jaipong", "Tor-tor"),
                "Kecak"
            ),
            Question(
                "Rumah adat dari Sumatera Utara adalah?",
                listOf("Rumah Gadang", "Rumah Bolon", "Rumah Limas", "Rumah Panggung"),
                "Rumah Bolon"
            ),
            Question(
                "Makanan khas Yogyakarta yang terkenal adalah?",
                listOf("Rendang", "Gudeg", "Pempek", "Kerak Telor"),
                "Gudeg"
            ),
            Question(
                "Batik berasal dari daerah?",
                listOf("Sumatera", "Jawa", "Kalimantan", "Sulawesi"),
                "Jawa"
            ),
            Question(
                "Alat musik tradisional dari Sumatera Barat adalah?",
                listOf("Angklung", "Talempong", "Sasando", "Kolintang"),
                "Talempong"
            )
        )
    }
}
