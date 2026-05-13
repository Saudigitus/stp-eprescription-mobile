package org.saudigitus.e_prescription.presentation.screens.prescriptions.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.saudigitus.e_prescription.R
import org.saudigitus.e_prescription.data.model.Patient

private data class UiField(
    val label: String,
    val value: String?
)

@Composable
fun TeiCard(
    modifier: Modifier = Modifier,
    patient: Patient?,
) {
    val fields = listOf(
        UiField(
            label = stringResource(R.string.process_number),
            value = patient?.processNumber?.second
        ),
        UiField(
            label = stringResource(R.string.birthdate),
            value = patient?.birthdate?.second
        ),
        UiField(
            label = stringResource(R.string.gender),
            value = patient?.gender?.second
        ),
        UiField(
            label = stringResource(R.string.address),
            value = patient?.residence?.second
        )
    ).filter { !it.value.isNullOrBlank() }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .then(modifier),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = patient?.name?.first
                        ?: stringResource(R.string.patient_name),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                Text(
                    text = listOfNotNull(
                        patient?.name?.second,
                        patient?.surname?.second
                    ).joinToString(" ").ifBlank { "---" },
                    style = MaterialTheme.typography.titleSmall
                )
            }

            fields.forEach { field ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = field.label,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = field.value ?: "---",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}