package com.aura.voicechat.ui.support

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.text.SimpleDateFormat
import java.util.*

/**
 * Support Ticket System
 * Developer: Hawkaye Visions LTD — Pakistan
 * 
 * Create and manage support tickets
 * - Browse tickets
 * - Create new tickets
 * - View ticket status
 * - Reply to tickets
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupportTicketsScreen(
    onNavigateBack: () -> Unit,
    onNavigateToTicket: (String) -> Unit,
    onNavigateToNewTicket: () -> Unit,
    onNavigateToLiveChat: () -> Unit,
    viewModel: SupportTicketsViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Support Center") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Live Chat FAB
                SmallFloatingActionButton(
                    onClick = onNavigateToLiveChat,
                    containerColor = MaterialTheme.colorScheme.secondary
                ) {
                    Icon(Icons.Default.Chat, "Live Chat")
                }
                
                // New Ticket FAB
                ExtendedFloatingActionButton(
                    onClick = onNavigateToNewTicket,
                    icon = { Icon(Icons.Default.Add, "New Ticket") },
                    text = { Text("New Ticket") }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Quick Actions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    QuickActionButton(
                        icon = Icons.Default.Chat,
                        label = "Live Chat",
                        count = if (state.hasActiveChat) 1 else null,
                        onClick = onNavigateToLiveChat
                    )
                    
                    QuickActionButton(
                        icon = Icons.Default.ConfirmationNumber,
                        label = "My Tickets",
                        count = state.myTickets.size,
                        onClick = { selectedTab = 0 }
                    )
                    
                    QuickActionButton(
                        icon = Icons.Default.QuestionAnswer,
                        label = "FAQ",
                        onClick = { /* Navigate to FAQ */ }
                    )
                }
            }
            
            // Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("My Tickets (${state.myTickets.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Resolved") }
                )
            }
            
            // Content
            when (selectedTab) {
                0 -> ActiveTicketsList(state.myTickets, onNavigateToTicket)
                1 -> ResolvedTicketsList(state.resolvedTickets, onNavigateToTicket)
            }
        }
    }
}

@Composable
private fun QuickActionButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    count: Int? = null,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Box {
            IconButton(onClick = onClick) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            
            if (count != null && count > 0) {
                Badge(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(count.toString())
                }
            }
        }
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun ActiveTicketsList(
    tickets: List<SupportTicket>,
    onNavigateToTicket: (String) -> Unit
) {
    if (tickets.isEmpty()) {
        EmptyTicketsState()
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tickets) { ticket ->
                TicketCard(ticket, onNavigateToTicket)
            }
        }
    }
}

@Composable
private fun ResolvedTicketsList(
    tickets: List<SupportTicket>,
    onNavigateToTicket: (String) -> Unit
) {
    if (tickets.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "No resolved tickets",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    } else {
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(tickets) { ticket ->
                TicketCard(ticket, onNavigateToTicket)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TicketCard(
    ticket: SupportTicket,
    onNavigateToTicket: (String) -> Unit
) {
    Card(
        onClick = { onNavigateToTicket(ticket.id) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "#${ticket.id}",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        Surface(
                            color = getStatusColor(ticket.status),
                            shape = MaterialTheme.shapes.small
                        ) {
                            Text(
                                text = ticket.status,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                        
                        if (ticket.hasUnread) {
                            Badge {
                                Text("New")
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = ticket.subject,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = ticket.lastMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        maxLines = 2
                    )
                }
                
                Icon(
                    Icons.Default.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        getCategoryIcon(ticket.category),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = ticket.category,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Text(
                    text = formatDate(ticket.lastUpdated),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun EmptyTicketsState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Icon(
                Icons.Default.ConfirmationNumber,
                contentDescription = null,
                modifier = Modifier.size(80.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "No active tickets",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Create a ticket or start a live chat if you need help",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

private fun getStatusColor(status: String): androidx.compose.ui.graphics.Color {
    return when (status.uppercase()) {
        "OPEN" -> androidx.compose.ui.graphics.Color(0xFF2196F3)
        "IN_PROGRESS" -> androidx.compose.ui.graphics.Color(0xFFFF9800)
        "WAITING" -> androidx.compose.ui.graphics.Color(0xFFFFC107)
        "RESOLVED" -> androidx.compose.ui.graphics.Color(0xFF4CAF50)
        "CLOSED" -> androidx.compose.ui.graphics.Color(0xFF757575)
        else -> androidx.compose.ui.graphics.Color(0xFF9E9E9E)
    }
}

private fun getCategoryIcon(category: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (category.uppercase()) {
        "ACCOUNT" -> Icons.Default.AccountCircle
        "PAYMENT" -> Icons.Default.Payment
        "TECHNICAL" -> Icons.Default.BugReport
        "REPORT" -> Icons.Default.Report
        "GENERAL" -> Icons.Default.Help
        else -> Icons.Default.QuestionAnswer
    }
}

private fun formatDate(timestamp: Long): String {
    val now = System.currentTimeMillis()
    val diff = now - timestamp
    
    return when {
        diff < 60_000 -> "Just now"
        diff < 3600_000 -> "${diff / 60_000}m ago"
        diff < 86400_000 -> "${diff / 3600_000}h ago"
        diff < 604800_000 -> "${diff / 86400_000}d ago"
        else -> {
            val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
            sdf.format(Date(timestamp))
        }
    }
}

// Data Models
data class SupportTicket(
    val id: String,
    val subject: String,
    val category: String,
    val status: String,
    val lastMessage: String,
    val lastUpdated: Long,
    val hasUnread: Boolean = false
)
