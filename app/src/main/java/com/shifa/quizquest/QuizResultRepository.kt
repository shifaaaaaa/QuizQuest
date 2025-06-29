import com.shifa.quizquest.ApiService
import com.shifa.quizquest.QuizResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuizResultRepository @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun getRecentQuizResults(userId: Long, limit: Int = 5): Result<List<QuizResult>> {
        return try {
            val response = apiService.getRecentQuizResults(userId, limit)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch quiz results"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllQuizResults(userId: Long): Result<List<QuizResult>> {
        return try {
            val response = apiService.getQuizResults(userId)
            if (response.isSuccessful && response.body()?.success == true) {
                Result.success(response.body()?.data ?: emptyList())
            } else {
                Result.failure(Exception("Failed to fetch quiz results"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}