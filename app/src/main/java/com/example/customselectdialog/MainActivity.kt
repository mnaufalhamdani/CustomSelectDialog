package com.example.customselectdialog

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.customselectdialog.databinding.ActivityMainBinding
import com.mnaufalhamdani.customselectdialog.MultipleSelectDialog
import com.mnaufalhamdani.customselectdialog.SingleSelectDialog
import com.mnaufalhamdani.customselectdialog.domain.MultipleSelectItemDomain
import com.mnaufalhamdani.customselectdialog.domain.SingleSelectItemDomain

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val listItem: MutableList<SingleSelectItemDomain> = mutableListOf()
    val listItemMultiple: MutableList<MultipleSelectItemDomain> = mutableListOf()
    val listItemSelected: MutableList<String> = mutableListOf()
    val items = listOf(
        "Indonesia", "Singapore", "Malaysia", "Philipine", "Myanmar", "Thailand", "Kamboja",
        "Brunei", "Vietnam", "Australia", "Timor Leste", "Hongkong"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        items.mapIndexed { index, item ->
            listItem.add(SingleSelectItemDomain(index.toString(), item))
        }

        items.mapIndexed { index, item ->
            listItemMultiple.add(MultipleSelectItemDomain(index.toString(), item))
            if (index % 2 == 0) listItemSelected.add(index.toString())
        }

        binding.btnSingle.setOnClickListener {
            SingleSelectDialog(binding.root.context)
                .listItem(listItem)
                .setTitle("Pilih Salah Satu Item")
                .setShowEmptyButton(true)
                .setHiddenSearch(false)
                .setCancelButton(true)
                .start(object : SingleSelectDialog.SubmitCallbackListener{
                    override fun onSelected(item: SingleSelectItemDomain) {
                        binding.tvResult.text = item.message
                    }
                    override fun onReset() {
                        binding.tvResult.text = ""
                    }
                    override fun onError(message: String) {
                        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
                    }
                })
        }

        binding.btnMultiple.setOnClickListener {
            MultipleSelectDialog(binding.root.context)
                .listItem(listItemMultiple)
                .listItemSelected(listItemSelected)
                .setTitle("Pilih Beberapa Item")
                .setHiddenSearch(false)
                .setCancelButton(true)
                .start(object : MultipleSelectDialog.SubmitCallbackListener{
                    override fun onSelected(
                        items: List<MultipleSelectItemDomain>,
                        codeOrId: String,
                        message: String
                    ) {
                        binding.tvResult.text = message
                    }

                    override fun onReset() {
                        binding.tvResult.text = ""
                    }
                    override fun onError(message: String) {
                        Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
                    }
                })
        }

    }
}