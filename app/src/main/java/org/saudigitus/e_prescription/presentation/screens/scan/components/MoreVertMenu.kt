package org.saudigitus.e_prescription.presentation.screens.scan.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.presentation.screens.scan.ScanUiEvent
import org.saudigitus.e_prescription.presentation.screens.scan.utils.Menu

@Composable
fun MoreVertMenu(
    onItemClick: (ScanUiEvent) -> Unit,
) {
    val context = LocalContext.current
    var expand by remember { mutableStateOf(false) }
    var selectedItemIndex by remember { mutableIntStateOf(-1) }

    val paddingValue = if (selectedItemIndex >= 0) {
        4.dp
    } else {
        0.dp
    }

    Column {
        IconButton(onClick = { expand = !expand }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.menu)
            )
        }

        DropdownMenu(
            offset = DpOffset(0.dp, 2.dp),
            expanded = expand,
            onDismissRequest = {
                expand = !expand
            },
        ) {
            Menu.items.forEachIndexed { index, item ->
                Row(Modifier.padding(horizontal = 10.dp)) {
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = if (selectedItemIndex == index) {
                                    Color.LightGray.copy(.5f)
                                } else {
                                    Color.Transparent
                                },
                                shape = RoundedCornerShape(16.dp),
                            )
                            .padding(paddingValue),
                        text = {
                            Text(
                                text = stringResource(item.name),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                softWrap = true,
                            )
                        },
                        onClick = {
                            onItemClick(item.event)
                            expand = !expand
                            selectedItemIndex = index
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = stringResource(item.name),
                                tint = MaterialTheme.colorScheme.primary,
                            )
                        },
                    )
                }
            }
        }
    }
}