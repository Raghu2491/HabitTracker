package com.jrg.habittracker.presentation.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jrg.habittracker.domain.Restaurant

@Composable
fun RestaurantScreen(
    state: RestaurantsScreenState,
    onItemClick: (id: Int) -> Unit,
    onFavoriteClick: (id: Int) -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        LazyColumn(
            contentPadding = PaddingValues(
                vertical = 8.dp,
                horizontal = 8.dp
            )
        ) {
            items(state.restaurants) { restaurant ->
                RestaurantItem(item = restaurant, onFavoriteClick = { id ->
                    onFavoriteClick(id)
                }, onItemClick = { id ->
                    onItemClick(id)
                })
            }
        }
        if (state.isLoading)
            CircularProgressIndicator()
    }
}

@Composable
private fun RestaurantItem(
    item: Restaurant,
    onFavoriteClick: (id: Int) -> Unit,
    onItemClick: (id: Int) -> Unit
) {
    val icon = if (item.isLiked)
        Icons.Filled.Favorite
    else
        Icons.Filled.FavoriteBorder

    Card(
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .padding(8.dp)
            .clickable { onItemClick(item.id) }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(icon = Icons.Filled.Place, modifier = Modifier.weight(0.15f))
            RestaurantDetails(modifier = Modifier.weight(0.70f), item.title, item.description)
            RestaurantIcon(icon, modifier = Modifier.weight(0.15f)) {
                onFavoriteClick(item.id)
            }
        }
    }
}

@Composable
fun RestaurantIcon(icon: ImageVector, modifier: Modifier, onClick: () -> Unit = { }) {
    Image(
        imageVector = icon,
        contentDescription = "Restaurant icon",
        modifier = modifier
            .padding(8.dp)
            .clickable {
                onClick()
            }
    )
}

@Composable
fun RestaurantDetails(
    modifier: Modifier,
    title: String,
    description: String,
    horizontalAlignment: Alignment.Horizontal = Alignment.Start
) {
    Column(
        modifier = modifier,
        horizontalAlignment = horizontalAlignment
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall
        )
        CompositionLocalProvider(
            LocalContentColor provides LocalContentColor.current.copy(alpha = 0.4f)
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }
}