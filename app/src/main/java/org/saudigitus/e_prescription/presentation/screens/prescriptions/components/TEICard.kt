package org.saudigitus.e_prescription.presentation.screens.prescriptions.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.hisp.dhis.android.core.trackedentity.TrackedEntityInstance
import org.saudigitus.e_prescription.data.model.Prescription
import org.saudigitus.e_prescription.presentation.screens.prescriptions.model.InputFieldModel

@Composable
fun TeiCard(
    modifier: Modifier = Modifier,
    tei:  TrackedEntityInstance?,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                //.background(color = Color(0xFFF0F4FF)) // Custom light background color
                .background(MaterialTheme.colorScheme.surface)
                .border(color = Color(0xFFF0F4FF), shape = RoundedCornerShape(16.dp), width = 1.dp)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Referência da consulta",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${tei?.uid()}", // Use actual tei id if available
                    style = MaterialTheme.typography.titleSmall
                )
            }

//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween
//            ) {
//                Text(
//                    text = "Nome",
//                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
//                )
//                Text(
//                    text = "${tei?.organisationUnit()}", // Replace with actual data
//                    style = MaterialTheme.typography.titleSmall
//                )
//            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Data",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${tei?.created()}", // Replace with actual data
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}


//@Preview(showBackground = false)
//@Composable
//fun TeiCardPreview() {
//    TeiCard(modifier = Modifier
//        .fillMaxWidth()
//        .height(150.dp)
//        .padding(vertical = 8.dp),tei = "568532665535",
//    )
//}