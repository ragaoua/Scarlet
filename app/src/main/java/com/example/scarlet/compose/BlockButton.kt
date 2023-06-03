package com.example.scarlet.compose

import android.content.Context
import android.content.Intent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.scarlet.activities.BlockActivity
import com.example.scarlet.model.Block

@Composable
fun BlockButton(
    context: Context,
    block: Block
) {
    Button(onClick = {
        val intent = Intent(context, BlockActivity::class.java)
        intent.putExtra("block", block)
        context.startActivity(intent)
    }) {
        Text(block.name!!)
    }
}