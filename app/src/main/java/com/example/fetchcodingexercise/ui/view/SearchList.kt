package com.example.fetchcodingexercise.ui.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fetchcodingexercise.R
import com.example.fetchcodingexercise.data.models.Item
import com.example.fetchcodingexercise.viewModel.ItemViewModel
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

/**
 * A searchable list of Items
 */
@Composable
fun SearchList(
    itemViewModel: ItemViewModel,
    modifier: Modifier = Modifier,
) {
    // Get items from itemViewModel and remove any with no name
    val items = itemViewModel.items.value.filter { it.name != "" }

    // Searchable Id's from items, adding "All" selector
    val searchableIds = listOf("All") + items
        .distinctBy { it.listId }
        .map { it.listId.toString() }
        .sorted()

    // Search query that is returned by the text field
    var searchQuery by remember { mutableStateOf("") }

    // Currently selected listId number from the dropdown menu (-1 if no filter)
    var listIdSelected by remember { mutableIntStateOf(-1) }

    // Observe loading state from the ViewModel
    val isLoading by itemViewModel.isLoading

    Column (modifier = modifier) {
        Row (
            // Be sure to align search bar area vertically
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Search bar
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search") },
                // Do not wrap to new line
                singleLine = true,
                // Take up maximum space
                modifier = Modifier.weight(1f)
            )

            // Small space between search bar and id dropdown menu
            Spacer(modifier = Modifier.width(8.dp))

            // Selection of different listId's
            IdDropdownMenu(
                itemIds = searchableIds,
                selectedId = listIdSelected,
                onIdSelected = { listIdSelected = it },
                // Static width so not overtaken by search bar
                modifier = Modifier.width(100.dp)
            )
        }

        // Spacer between search area and list items
        Spacer(modifier = Modifier.height(2.dp))

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing = isLoading),
            onRefresh = { itemViewModel.fetchItems() }
        ) {
            if (itemViewModel.items.value.isEmpty()) {
                // Show a loading indicator when the list is empty and loading
                Box(
                    modifier = modifier.fillMaxWidth()
                        .padding(top = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Text("Nothing's here...",
                            fontWeight = FontWeight.Light,
                            fontStyle = FontStyle.Italic)
                    }
                }
            } else {
                // Filtered list based on item filter function below
                val filteredItems = items.filter {
                    itemFilter(it, listIdSelected, searchQuery)
                }
                    .sortedBy { it.name }
                // Then sort by name for consistency

                ListArea(filteredItems, listIdSelected)
            }
        }
    }
}

/**
 * Builds the column list of items from the filteredItems list
 */
@Composable
private fun ListArea(filteredItems: List<Item>, listIdSelected: Int) {
    // Group items by listId
    val groupedItems = filteredItems.groupBy { it.listId }
        .toSortedMap()

    // Display a notification for empty list if necessary
    if (filteredItems.isEmpty()) {
        NoItemsAlert()
    }

    LazyColumn {
        groupedItems.forEach { (listId, items) ->
            // List Id spacer to denote new lists (only if not filtering by list id)
            item {
                if (listIdSelected == -1) {
                    ListIdSpacer(listId)
                }
            }

            // List items
            items (items) { item ->
                ItemCard(
                    item,
                    modifier = Modifier.padding(vertical = 5.dp, horizontal = 8.dp)
                )
            }
        }
    }
}

/**
 * Returns whether an item passes the item filter for the item list
 */
private fun itemFilter(item: Item, idSelected: Int, searchQuery: String): Boolean {
    var valid = true

    // Make sure the item contains the search query
    valid = valid && item.name.contains(searchQuery, ignoreCase = true)

    // If we are filtering by list Id, filter out unwanted Id's
    if (idSelected != -1) {
        valid = valid && item.listId == idSelected
    }

    return valid
}

/**
 * Item card for each item
 */
@Composable
private fun ItemCard(
    item: Item,
    modifier: Modifier = Modifier
) {
    ElevatedCard (modifier = modifier) {
        Row (
            // Make sure item content is centered vertically
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Display generated item image
            Image(
                painter = painterResource(item.itemImageResId),
                contentDescription = item.name,
                modifier =  Modifier
                    .padding(8.dp)
                    .padding(end = 15.dp)
            )

            // Item name
            Text(
                item.name,
                fontWeight = FontWeight.Bold
            )

            // Item Id (displayed on right end of card)
            Column (
                modifier = Modifier.weight(1f)
                    .padding(end = 8.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "Item ID: ${item.id}",
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

/**
 * List Id Spacer to denote new item lists
 */
@Composable
private fun ListIdSpacer(
    listId: Int,
    modifier: Modifier = Modifier
) {
    ElevatedCard (
        colors = CardDefaults.cardColors(
            containerColor = Color(47,11,55),
            contentColor = Color.White
        ),
        modifier = modifier.fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 2.dp)
    ) {
        Text(
            listId.toString(),
            fontSize = 14.sp,
            modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
        )
    }
}

/**
 * Dropdown menu for selecting which list ID to filter by
 */
@Composable
private fun IdDropdownMenu(
    itemIds: List<String>,
    selectedId: Int,
    onIdSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Expanded state variable
    var expanded by remember { mutableStateOf(false) }

    Box(
        // Center the dropdown menu in box area
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = { expanded = true },
            colors = ButtonColors(
                containerColor = Color(47,11,55),
                contentColor = Color.White,
                disabledContentColor = Color.White,
                disabledContainerColor = Color(47,11,55)
            )
        ) {
            // Inner text (if selected id is -1, no filter)
            if (selectedId == -1) {
                Text("All")
            } else {
                Text(selectedId.toString())
            }

            // Dropdown icon
            Icon(painterResource(R.drawable.baseline_arrow_drop_down), contentDescription = "Dropdown Arrow")
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            itemIds.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        // If our selected option is All, we must make our selected id -1 (denotes no filter)
                        if (option == "All") {
                            onIdSelected(-1)
                        } else {
                            onIdSelected(option.toInt())
                        }
                        expanded = false
                    }
                )
            }
        }
    }
}

/**
 * Simple text box notification for no items for query
 */
@Composable
private fun NoItemsAlert(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            "Nothing Matches Your Search Query",
            fontWeight = FontWeight.Light,
            fontStyle = FontStyle.Italic
        )
    }
}