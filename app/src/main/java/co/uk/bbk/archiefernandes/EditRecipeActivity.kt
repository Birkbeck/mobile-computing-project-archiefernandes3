package co.uk.bbk.archiefernandes

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import co.uk.bbk.archiefernandes.databinding.ActivityEditRecipeBinding


class EditRecipeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditRecipeBinding
    private val viewModel: RecipeViewModel by viewModels()
    private var currentRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityEditRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup category spinner
        val categories = listOf("Breakfast", "Brunch", "Lunch", "Dinner", "Desserts", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategory.adapter = adapter

        val recipeId = intent.getIntExtra("RECIPE_ID", -1)
        if (recipeId == -1) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load existing recipe and populate fields
        viewModel.allRecipes.observe(this) { recipes ->
            val recipe = recipes.find { it.id == recipeId }
            if (recipe != null) {
                currentRecipe = recipe
                binding.editTextTitle.setText(recipe.title)
                binding.editTextIngredients.setText(recipe.ingredients)
                binding.editTextInstructions.setText(recipe.instructions)
                binding.spinnerCategory.setSelection(categories.indexOf(recipe.category))
            }
        }

        binding.buttonSaveChanges.setOnClickListener {
            val updated = currentRecipe?.copy(
                title = binding.editTextTitle.text.toString(),
                ingredients = binding.editTextIngredients.text.toString(),
                instructions = binding.editTextInstructions.text.toString(),
                category = binding.spinnerCategory.selectedItem.toString()
            )

            if (updated != null) {
                viewModel.update(updated)
                Toast.makeText(this, "Recipe updated!", Toast.LENGTH_SHORT).show()
            }

            finish()
        }

        binding.buttonCancelEdit.setOnClickListener {
            finish()
        }

    }
}