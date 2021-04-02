package com.recipeapp.presentation.ui.recipe_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.recipeapp.presentation.components.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class RecipeListFragment : Fragment() {

    private val viewModel by viewModels<RecipeListViewModel>()

    @ExperimentalCoroutinesApi
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        return ComposeView(requireContext()).apply {
            setContent {

                ViewRecipeList()

                val errorState = viewModel.errorState.value

                if (errorState.hasError) {
                    AppAlertDialog(
                        activity = requireActivity(),
                        title = "Network Error",
                        message = "Something went wrong : ${errorState.errorMessage}",
                        buttonText = "Ok",
                        state = mutableStateOf(true)
                    )
                }
            }
        }
    }

    @Composable
    private fun ViewRecipeList() {
        val recipes = viewModel.recipes.value
        val query = viewModel.query.value
        val selectedCategory = viewModel.selectedFoodCategory.value

        Column {
            SearchAppBar(
                query = query,
                onQueryChange = viewModel::onQueryChange,
                executeSearch = viewModel::search,
                categoryScrollPosition = viewModel.categoryScrollPosition,
                onSelectedCategoryChange = viewModel::onSelectedCategoryChange,
                onChangeCategoryScrollPosition = viewModel::onChangeCategoryScrollPosition,
                selectedCategory = selectedCategory
            )
            val loading = viewModel.loading.value

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .fillMaxWidth()
            ) {
                if (loading) {

                    LoadingRecipeListShimmer(cardHeight = 250.dp)

                }else {
                    LazyColumn {
                        itemsIndexed(items = recipes) { _, recipe ->
                            RecipeCard(recipe = recipe, onClick = {

                            })
                        }
                    }
                }

                CircularIndeterminateProgressBar(isDisplayed = loading)
            }
        }
    }

}