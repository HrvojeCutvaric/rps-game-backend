package co.hrvoje.rpsgame.viewmodel.game_details

import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.Round
import co.hrvoje.rpsgame.domain.models.User
import co.hrvoje.rpsgame.domain.utils.Move

data class GameDetailsState(
    val game: Game?,
    val rounds: List<Round>?,
    val errorResource: Int?,
    val isJoinVisible: Boolean,
    val currentUser: User,
) {
    companion object {
        enum class RoundResult {
            FIRST_WINS,
            SECOND_WINS,
            DRAW
        }

        fun resolveRound(
            first: Move?,
            second: Move?
        ): RoundResult {
            if (first == null || second == null) return RoundResult.DRAW

            if (first == second) return RoundResult.DRAW

            return when (first) {
                Move.ROCK ->
                    if (second == Move.SCISSORS) RoundResult.FIRST_WINS else RoundResult.SECOND_WINS

                Move.PAPER ->
                    if (second == Move.ROCK) RoundResult.FIRST_WINS else RoundResult.SECOND_WINS

                Move.SCISSORS ->
                    if (second == Move.PAPER) RoundResult.FIRST_WINS else RoundResult.SECOND_WINS
            }
        }

        data class Score(
            val first: Int,
            val second: Int
        )
    }

    fun calculateScore(): Score {
        var firstScore = 0
        var secondScore = 0

        rounds?.forEach { round ->
            when (resolveRound(round.firstUserMove, round.secondUserMove)) {
                RoundResult.FIRST_WINS -> firstScore++
                RoundResult.SECOND_WINS -> secondScore++
                RoundResult.DRAW -> Unit
            }
        }

        return Score(firstScore, secondScore)
    }

    val score = calculateScore()

    val lastRound: Round?
        get() = rounds?.maxByOrNull { it.createdAt }

    val isCurrentUserInGame: Boolean
        get() = game?.let {
            it.firstUser.id == currentUser.id ||
                    it.secondUser?.id == currentUser.id
        } ?: false

    val isCurrentUserFirstPlayer: Boolean
        get() = game?.firstUser?.id == currentUser.id

    val isCurrentUserSecondPlayer: Boolean
        get() = game?.secondUser?.id == currentUser.id

    val hasCurrentUserPlayed: Boolean
        get() = lastRound?.let { round ->
            when {
                isCurrentUserFirstPlayer -> round.firstUserMove != null
                isCurrentUserSecondPlayer -> round.secondUserMove != null
                else -> true
            }
        } ?: true

    val canCurrentUserPlayMove: Boolean
        get() = isCurrentUserInGame && !hasCurrentUserPlayed
}
