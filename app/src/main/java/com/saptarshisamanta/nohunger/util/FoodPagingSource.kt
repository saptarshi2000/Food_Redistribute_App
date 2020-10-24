package com.saptarshisamanta.nohunger.util

import androidx.paging.PagingSource
import com.saptarshisamanta.nohunger.api.FoodApiService
import com.saptarshisamanta.nohunger.data.Food
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class FoodPagingSource(
    private val foodApiService: FoodApiService,
    private val query: String?
) : PagingSource<Int, Food>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Food> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = foodApiService.availableFoods(query, position, params.loadSize)
            val foods = response.body()
            LoadResult.Page(
                data = foods!!,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (foods.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }
}