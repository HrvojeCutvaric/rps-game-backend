package co.hrvoje.rpsgame.view.games

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.utils.GameStatus
import co.hrvoje.rpsgame.ui.components.LoadingView
import co.hrvoje.rpsgame.ui.theme.RockPaperScissorsGameTheme
import co.hrvoje.rpsgame.viewmodel.games.GamesAction
import co.hrvoje.rpsgame.viewmodel.games.GamesState
import co.hrvoje.rpsgame.viewmodel.games.GamesViewModel
import kotlin.time.Clock
import org.koin.androidx.compose.koinViewModel

@Composable
fun GamesScreen(
    viewModel: GamesViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    state?.let { currentState ->
        GamesLayout(
            state = currentState,
            onAction = viewModel::execute
        )
    } ?: run {
        LoadingView()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GamesLayout(
    state: GamesState,
    onAction: (GamesAction) -> Unit
) {
    if (state.errorResource != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(state.errorResource),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Gray
            )
        }
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Trenutne igre") }
                )
            }
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.games) { game ->
                    GameCard(
                        game = game,
                        onJoinClick = { onAction(GamesAction.OnGamesJoinClicked(game.id)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GameCard(
    game: Game,
    onJoinClick: (Game) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Igra #${game.id}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            GameStatusChip(status = game.status)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kreirano: ${game.createAt}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )

                if (game.status == GameStatus.WAITING_FOR_PLAYERS) {
                    Button(
                        onClick = { onJoinClick(game) }
                    ) {
                        Text("Join")
                    }
                }
            }
        }
    }
}

@Composable
private fun GameStatusChip(status: GameStatus) {
    val color = when (status) {
        GameStatus.IN_PROGRESS -> Color(0xFF4CAF50)
        GameStatus.WAITING_FOR_PLAYERS -> Color(0xFFFFC107)
        GameStatus.FINISHED -> Color(0xFF9E9E9E)
    }

    Surface(
        color = color.copy(alpha = 0.2f),
        shape = RoundedCornerShape(50)
    ) {
        Text(
            text = status.name,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun GamesScreenPreview() {
    RockPaperScissorsGameTheme {
        GamesLayout(
            state = GamesState(
                games = List(5) { index ->
                    Game(
                        id = index + 1,
                        createAt = Clock.System.now().toEpochMilliseconds(),
                        status = GameStatus.WAITING_FOR_PLAYERS,
                    )
                },
                errorResource = null,
            ),
            onAction = { }
        )
    }
}
