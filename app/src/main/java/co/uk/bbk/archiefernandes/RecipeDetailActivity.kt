package co.uk.bbk.archiefernandes

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import co.uk.bbk.archiefernandes.databinding.ActivityRecipeDetailBinding

class RecipeDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecipeDetailBinding
    private val viewModel: RecipeViewModel by viewModels()
    private var currentRecipe: Recipe? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up view binding
        binding = ActivityRecipeDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val titleView = binding.detailTitle
        val ingredientsView = binding.detailIngredients
        val instructionsView = binding.detailInstructions
        val categoryView = binding.detailCategory

        val editButton = binding.buttonEdit
        val deleteButton = binding.buttonDelete

        val recipeId = intent.getIntExtra("RECIPE_ID", -1)
        if (recipeId == -1) {
            Toast.makeText(this, "Recipe not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        viewModel.allRecipes.observe(this) { recipes ->
            val recipe = recipes.find { it.id == recipeId }
            if (recipe != null) {
                currentRecipe = recipe
                titleView.text = recipe.title
                ingredientsView.text = recipe.ingredients
                instructionsView.text = recipe.instructions
                categoryView.text = recipe.category
            }
        }

        editButton.setOnClickListener {
            val intent = Intent(this, EditRecipeActivity::class.java)
            intent.putExtra("RECIPE_ID", currentRecipe?.id ?: -1)
            startActivity(intent)
        }

        deleteButton.setOnClickListener {
            currentRecipe?.let {
                viewModel.delete(it)
                Toast.makeText(this, "Recipe deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
