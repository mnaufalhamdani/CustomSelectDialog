## Custom Select Dialog

Custom Select Dialog is a dialog for selecting certain items that have been set, including:

- **SingleSelectDialog**: The single select dialog is used to select only one item, and can be reset.
- **MultipleSelectDialog**: The multiple select dialog is used to select more than one item or even all of them, and can be reset.

# Preview
   Main Sample    |  Single Select Dialog  |  Multiple Select Dialog  |
:-------------------------:|:-------------------------:|:-------------------------:
![](https://github.com/mnaufalhamdani/CustomSelectDialog/blob/master/image/photo_2023-03-23_12-04-51.jpg)  |  ![](https://github.com/mnaufalhamdani/CustomSelectDialog/blob/master/image/Screenshot_2024-10-24-14-20-39-97.jpg)  |  ![](https://github.com/mnaufalhamdani/CustomSelectDialog/blob/master/image/Screenshot_2024-10-24-14-20-57-83.jpg)

# Usage

1. Gradle dependency:

	```groovy
	allprojects {
	   repositories {
           	maven { url "https://jitpack.io" }
	   }
	}
	```

    ```groovy
   implementation 'com.github.mnaufalhamdani:customselectdialog:25.4.3'
    ```


# Single Select Dialog

1. Data set for the single select dialog configuration.

	**Kotlin**

	```kotlin
    val listItem: MutableList<SingleSelectItemDomain> = mutableListOf()
    val items = listOf(
        "Indonesia", "Singapore", "Malaysia", "Philipine", "Myanmar", "Thailand", "Kamboja",
        "Brunei", "Vietnam", "Australia", "Timor Leste", "Hongkong"
    )
    items.mapIndexed { index, item ->
        listItem.add(SingleSelectItemDomain(index.toString(), item))
    }
    ```
    
2. Run

    Run the library if needed:

    ```kotlin
    SingleSelectDialog(binding.root.context)
          .listItem(listItem)                   //set list item
          .setTitle("Pilih Salah Satu Item")    //set title
          .setShowEmptyButton(true)             //set show empty or reset button (default false)
          .setHiddenSearch(false)               //set show hidden search (default false)
          .setCancelButton(true)                //set enable cancel button (default true)
          .start(object : SingleSelectDialog.SubmitCallbackListener{
              override fun onSelected(item: SingleSelectItemDomain) {
                  binding.tvResult.text = item.title
              }
              override fun onReset() {
                  binding.tvResult.text = ""
              }
              override fun onError(message: String) {
                  Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
              }
          })
    ```
    
 # Multiple Select Dialog

1. Data set for the multiple select dialog configuration.

	**Kotlin**

	```kotlin
    val message = "Yuk kita jalan-jalan. Jangan lupa persiapkan barang-barang yang akan dibawa \nDan jangan lupa untuk membawa bekal"
    val listItemMultiple: MutableList<MultipleSelectItemDomain> = mutableListOf()
    val listItemSelected: MutableList<String> = mutableListOf()
    val items = listOf(
        "Indonesia", "Singapore", "Malaysia", "Philipine", "Myanmar", "Thailand", "Kamboja",
        "Brunei", "Vietnam", "Australia", "Timor Leste", "Hongkong"
    )
    items.mapIndexed { index, item ->
        listItemMultiple.add(MultipleSelectItemDomain(index.toString(), item, message))
        if (index % 2 == 0) listItemSelected.add(index.toString())
    }
    ```
    
2. Run

    Run the library if needed:

    ```kotlin
    MultipleSelectDialog(binding.root.context)
          .listItem(listItemMultiple)             //set list item
          .listItemSelected(listItemSelected)     //set list item selected (set code or id from your data)
          .setTitle("Pilih Beberapa Item")        //set title
          .setHiddenSearch(false)                 //set show hidden search (default false)
          .setCancelButton(true)                  //set enable cancel button (default true)
          .start(object : MultipleSelectDialog.SubmitCallbackListener{
              override fun onSelected(
                  items: List<MultipleSelectItemDomain>,
                  codeOrId: String,
                  result: String
              ) {
                  binding.tvResult.text = result
              }

              override fun onReset() {
                  binding.tvResult.text = ""
              }
              override fun onError(message: String) {
                  Toast.makeText(binding.root.context, message, Toast.LENGTH_SHORT).show()
              }
          })
    ```
    

## License

    Copyright 2024, mnaufalhamdani

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
   
   
