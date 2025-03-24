package org.saudigitus.e_prescription.presentation.screens.prescriptions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.data.model.PrescriptionError
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.InputFieldModel

@Composable
fun PrescriptionCard(
    modifier: Modifier = Modifier,
    prescription: Prescription,
    inputFieldModels: List<InputFieldModel>,
    onValueChange: (Prescription, String) -> Unit
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = prescription.name,
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true,
                        maxLines = 1,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize
                    )
                    Text(
                        text = "${prescription.requestedQtd}",
                        overflow = TextOverflow.Ellipsis,
                        softWrap = true,
                        maxLines = 1,
                        fontSize = MaterialTheme.typography.titleSmall.fontSize
                    )
                }
            }

            TextField(
                modifier = Modifier.width(150.dp)
                    .padding(16.dp),
                value = inputFieldModels.find { it.key == prescription.uid }?.value ?: "",
                onValueChange = {
                    onValueChange(prescription, it)
                },
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                textStyle = TextStyle(textAlign = TextAlign.Center),
                isError = inputFieldModels.find { it.key == prescription.uid }?.hasError() ?: false
            )
        }
    }
}

@Composable
fun PrescriptionCard(
    modifier: Modifier = Modifier,
    prescription: PrescriptionError,
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = prescription.name,
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                maxLines = 1,
                fontSize = MaterialTheme.typography.titleMedium.fontSize
            )
            Text(
                text = stringResource(R.string.given_qtd_gt_requested_qtd, prescription.givenQtd, prescription.requestedQtd),
                overflow = TextOverflow.Ellipsis,
                softWrap = true,
                maxLines = 1,
                fontSize = MaterialTheme.typography.titleSmall.fontSize,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun PrescriptionSummaryCard(
    modifier: Modifier,
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    colors: CardColors = CardDefaults.cardColors()
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(5.dp),
        colors = colors
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title
                )

                Text(title)
            }
            subtitle?.let {
                Text(text = it)
            }
        }
    }
}