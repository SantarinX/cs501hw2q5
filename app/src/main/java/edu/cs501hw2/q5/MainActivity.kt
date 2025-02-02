package edu.cs501hw2.q5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.cs501hw2.q5.ui.theme.Q5Theme
import kotlinx.coroutines.CoroutineScope
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.launch

data class Item(val name: String, val price: Double)

val shoppingCart = listOf(
    Pair(Item("Apple", 1.0), 2),
    Pair(Item("Banana", 0.5), 3),
    Pair(Item("Orange", 0.75), 4),
    Pair(Item("Pineapple", 2.0), 1),
    Pair(Item("Grapes", 1.5), 2)
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Q5Theme {
                val snackbarHostState = remember { SnackbarHostState() }
                val coroutineScope = rememberCoroutineScope()

                Scaffold(Modifier.fillMaxSize(), snackbarHost = { SnackbarHost(snackbarHostState) }) { innerPadding ->
                    ShoppingCartScreen(Modifier.padding(innerPadding), snackbarHostState, coroutineScope)
                }
            }
        }
    }
}

@Composable
fun ShoppingCartScreen(modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState, coroutineScope: CoroutineScope) {
    val totalPrice = shoppingCart.sumOf { it.first.price * it.second }

    Column(modifier = modifier.fillMaxSize().padding(16.dp).verticalScroll(rememberScrollState())) {
        Text(text = "Shopping Cart", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(bottom = 16.dp))

        shoppingCart.forEach {(item, quantity) -> ShoppingCartItem(item, quantity) }

        Spacer(modifier = Modifier.height(24.dp))

        Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
            Text(text = "Total:", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)

            Text(text = "$${"%.2f".format(totalPrice)}", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { coroutineScope.launch { snackbarHostState.showSnackbar("Ordered") } }, modifier = Modifier.fillMaxWidth()) {
            Text("Checkout")
        }

    }
}

@Composable
fun ShoppingCartItem(item: Item, quantity: Int) {
    Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween, Alignment.CenterVertically) {
        Column {
            Text(text = item.name, fontWeight = FontWeight.Medium)

            Text(text = "$${"%.2f".format(item.price)} each", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(text = "Quantity: $quantity", style = MaterialTheme.typography.bodyMedium)

            Text(text = "$${"%.2f".format(item.price * quantity)}", style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
        }
    }
}