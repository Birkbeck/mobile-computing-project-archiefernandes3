package co.uk.bbk.archiefernandes

import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import co.uk.bbk.archiefernandes.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var recipeAdapter: RecipeAdapter
    private lateinit var binding: ActivityMainBinding
    private val viewModel: RecipeViewModel by viewModels()
    private val categories = listOf("All", "Breakfast", "Brunch", "Lunch", "Dinner", "Desserts", "Other")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflate layout using View Binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up the Toolbar

        setSupportActionBar(binding.toolbar)

        // Setup RecyclerView
        recipeAdapter = RecipeAdapter { selectedRecipe ->
            val intent = Intent(this, RecipeDetailActivity::class.java)
            intent.putExtra("RECIPE_ID", selectedRecipe.id)
            startActivity(intent)
        }

        binding.recipeRecyclerView.apply {
            adapter = recipeAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        // Setup Spinner
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)
        binding.spinnerCategoryFilter.adapter = spinnerAdapter

        // Observe recipes
        viewModel.allRecipes.observe(this) { recipes ->
            filterAndDisplayRecipes(recipes)
        }

        // Handle Spinner selection
        binding.spinnerCategoryFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                viewModel.allRecipes.value?.let { filterAndDisplayRecipes(it) }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // Add Recipe Button
        binding.buttonAddRecipe.setOnClickListener {
            val intent = Intent(this, AddRecipeActivity::class.java)
            startActivity(intent)
        }

    }

    private fun filterAndDisplayRecipes(allRecipes: List<Recipe>) {
        val selectedCategory = binding.spinnerCategoryFilter.selectedItem.toString()
        val filtered = if (selectedCategory == "All") {
            allRecipes
        } else {
            allRecipes.filter { it.category == selectedCategory }
        }
        recipeAdapter.submitList(filtered)
    }
}