package co.uk.bbk.archiefernandes

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import co.uk.bbk.archiefernandes.databinding.ActivityAddRecipeBinding

class AddRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddRecipeBinding
    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityAddRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup category spinner
        val categories = listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Desserts", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategory.adapter = adapter

        // Save button logic
        binding.buttonSave.setOnClickListener {
            val title = binding.editTextTitle.text.toString()
            val ingredients = binding.editTextIngredients.text.toString()
            val instructions = binding.editTextInstructions.text.toString()
            val category = binding.spinnerCategory.selectedItem.toString()

            if (title.isBlank() || ingredients.isBlank() || instructions.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val recipe = Recipe(
                title = title,
                ingredients = ingredients,
                instructions = instructions,
                category = category
            )

            recipeViewModel.insert(recipe)
            Toast.makeText(this, "Recipe saved!", Toast.LENGTH_SHORT).show()
            finish()
        }

        binding.buttonCancel.setOnClickListener {
            finish()
        }
    }
}
