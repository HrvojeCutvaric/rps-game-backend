package co.hrvoje.rpsgame.view.games

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import co.hrvoje.rpsgame.R
import co.hrvoje.rpsgame.domain.models.Game
import co.hrvoje.rpsgame.domain.models.User
import co.hrvoje.rpsgame.ui.components.LoadingView
import co.hrvoje.rpsgame.ui.theme.RockPaperScissorsGameTheme
import co.hrvoje.rpsgame.viewmodel.games.GamesAction
import co.hrvoje.rpsgame.viewmodel.games.GamesState
import co.hrvoje.rpsgame.viewmodel.games.GamesViewModel
import formatGameTime
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
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onAction(GamesAction.OnGameCreateClicked) },
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_add),
                        contentDescription = null,
                    )
                }
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
                        onGameClicked = { onAction(GamesAction.OnGameClicked(game.id)) }
                    )
                }
            }
        }
    }
}

@Composable
private fun GameCard(
    game: Game,
    onGameClicked: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onGameClicked),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Game #${game.id}",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                color = Color(0xFFFFC107),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = stringResource(R.string.join_in_a_game, game.firstUser.username),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color.White,
                    style = MaterialTheme.typography.labelMedium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = game.createAt.formatGameTime(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
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
                        firstUser = User(id = 1, username = "hrvoje-test"),
                        secondUser = User(id = 1, username = "hrvoje-test"),
                    )
                },
                errorResource = null,
            ),
            onAction = { }
        )
    }
}
