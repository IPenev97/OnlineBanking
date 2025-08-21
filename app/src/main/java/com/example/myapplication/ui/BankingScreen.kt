package com.example.myapplication.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.R
import com.example.myapplication.ui.components.BalanceCard
import com.example.myapplication.ui.components.CardsCarousel
import com.example.myapplication.ui.components.CustomDialog
import com.example.myapplication.ui.components.TransactionsRow

@Composable
fun BankingScreen(
    viewModel: BankingViewModel = hiltViewModel()
) {

    val state = viewModel.state.value




    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column() {
            if(state.noConnection) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {viewModel.onBankingEvent(BankingEvent.ToggleDialog(true))},
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = "Star",
                        tint = colorResource(R.color.light_red),
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
            CardsCarousel(cards = state.cards, isLoading = state.cardsLoading)
            BalanceCard(balanceBgn = state.balance, onPayClicked = {})
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.transactions),
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Filled.FilterList,
                    contentDescription = "Filter transactions",
                    tint = colorResource(R.color.blue),
                    modifier = Modifier.size(25.dp)
                )
            }
            if (state.transactionsLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                if(state.transactions?.isEmpty() == true){
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = stringResource(R.string.no_transactions), style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold))
                    }
                } else {
                    TransactionsRow(
                        state.transactions,
                        isRefreshing = state.isTransactionsRefreshing,
                        onRefresh = { viewModel.onBankingEvent(BankingEvent.LoadTransactions) })
                }
            }
        }
    }
    if (state.showConnectionDialog)
        CustomDialog(
            confirmButtonText = stringResource(R.string.refresh),
            titleText = stringResource(R.string.banking),
            messageText = stringResource(R.string.no_connection_error),
            onConfirm = { viewModel.onBankingEvent(BankingEvent.RefreshData) },
            onDismiss = {viewModel.onBankingEvent(BankingEvent.ToggleDialog(false))}
        )
}




