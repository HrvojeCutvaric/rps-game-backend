package co.hrvoje.rpsgame.view.game_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.Round
import co.hrvoje.rpsgame.domain.models.User
import co.hrvoje.rpsgame.domain.utils.Move
import co.hrvoje.rpsgame.ui.theme.RockPaperScissorsGameTheme
import co.hrvoje.rpsgame.viewmodel.game_details.GameDetailsAction
import co.hrvoje.rpsgame.viewmodel.game_details.GameDetailsState
import co.hrvoje.rpsgame.viewmodel.game_details.GameDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun GameDetailsScreen(
    viewModel: GameDetailsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { currentState ->
        GameDetailsLayout(
            state = currentState,
            onAction = viewModel::execute
        )
    }
}

@Composable
private fun GameDetailsLayout(
    state: GameDetailsState,
    onAction: (GameDetailsAction) -> Unit
) {
    if (state.errorResource != null || state.game == null || state.rounds == null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(state.errorResource ?: R.string.generic_error_message),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    } else {
        Scaffold(
            topBar = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { onAction(GameDetailsAction.OnBackClicked) }) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = null,
                        )
                    }

                    if (state.isJoinVisible) {
                        IconButton(onClick = { onAction(GameDetailsAction.OnJoinClicked) }) {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                painter = painterResource(R.drawable.ic_join),
                                contentDescription = null,
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {

                ScoreRow(
                    firstUsername = state.game.firstUser.username,
                    secondUsername = state.game.secondUser?.username,
                    firstScore = state.score.first,
                    secondScore = state.score.second,
                )

                HorizontalDivider()

                RoundsList(
                    modifier = Modifier.weight(1f),
                    rounds = state.rounds,
                )

                HorizontalDivider()

                MoveSelection(
                    onMoveSelected = { onAction(GameDetailsAction.OnMoveClicked(it)) }
                )
            }
        }
    }
}

@Composable
private fun ScoreRow(
    firstUsername: String,
    secondUsername: String?,
    firstScore: Int,
    secondScore: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = firstUsername, fontWeight = FontWeight.Bold)

        Text(
            text = "$firstScore : $secondScore",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        Text(
            text = secondUsername ?: "Waiting...",
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun RoundsList(
    rounds: List<Round>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        items(rounds) { round ->
            RoundItem(round)
        }
    }
}

@Composable
fun RoundItem(round: Round) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = round.firstUserMove?.name ?: "-")
            Text(text = "vs")
            Text(text = round.secondUserMove?.name ?: "-")
        }
    }
}

@Composable
fun MoveSelection(
    onMoveSelected: (Move) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Choose your move",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            MoveButton("🪨", "Rock") { onMoveSelected(Move.ROCK) }
            MoveButton("📄", "Paper") { onMoveSelected(Move.PAPER) }
            MoveButton("✂️", "Scissors") { onMoveSelected(Move.SCISSORS) }
        }
    }
}

@Composable
fun MoveButton(
    emoji: String,
    label: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(56.dp)
    ) {
        Text("$emoji $label")
    }
}


@Preview(showBackground = true)
@Composable
private fun GameDetailsScreenPreview() {
    val game = Game(
        id = 1,
        createAt = System.currentTimeMillis(),
        firstUser = User(1, "hrvoje-test"),
        secondUser = null
    )
    RockPaperScissorsGameTheme {
        GameDetailsLayout(
            state = GameDetailsState(
                game = game,
                rounds = List(5) {
                    Round(
                        id = it,
                        game = game,
                        createdAt = System.currentTimeMillis(),
                        firstUserMove = listOf(null, Move.ROCK, Move.PAPER, Move.SCISSORS).random(),
                        secondUserMove = listOf(
                            null,
                            Move.ROCK,
                            Move.PAPER,
                            Move.SCISSORS
                        ).random(),
                    )
                },
                errorResource = null,
                isJoinVisible = true,
            ),
            onAction = {}
        )
    }
}
