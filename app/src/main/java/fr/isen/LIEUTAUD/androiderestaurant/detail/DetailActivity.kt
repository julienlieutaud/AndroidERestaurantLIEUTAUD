package com.example.isen_2021.detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.isen_2021.BaseActivity
import com.example.isen_2021.CategoryActivity
import com.example.isen_2021.HomeActivity
import com.example.isen_2021.R
import com.example.isen_2021.basket.Basket
import com.example.isen_2021.basket.BasketItem
import com.example.isen_2021.databinding.ActivityDetailBinding
import com.example.isen_2021.network.Dish
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlin.math.max

class DetailActivity : BaseActivity() {
    companion object {
        const val DISH_EXTRA = "DISH_EXTRA"
    }

    lateinit var binding: ActivityDetailBinding
    private var itemCount = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val dish = intent.getSerializableExtra(DISH_EXTRA) as? Dish
        dish?.let {
            setupView(it)
        }
        val fragment = DetailViewFragment(dish)
        supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment).commit()
    }

    private fun setupView(dish: Dish) {
        refreshShop(dish)

        binding.less.setOnClickListener {
            itemCount = max(1, itemCount - 1)
            refreshShop(dish)
        }

        binding.more.setOnClickListener {
            itemCount += 1
            refreshShop(dish)
        }

        binding.shopButton.setOnClickListener {
            addToBasket(dish, itemCount)
        }
    }

    private fun refreshShop(dish: Dish) {
        val price = itemCount * dish.prices.first().price.toFloat()
        binding.itemCount.text = itemCount.toString()
        binding.shopButton.text = "${getString(R.string.add_to_cart)} $price€"
    }

    private fun addToBasket(dish: Dish, count: Int) {
        val basket = Basket.getBasket(this)
        basket.addItem(BasketItem(dish, count))
        basket.save(this)
        refreshMenu(basket)
        Snackbar.make(binding.root, getString(R.string.basket_validation), Snackbar.LENGTH_LONG).show()
    }

    private fun refreshMenu(basket: Basket) {
        invalidateOptionsMenu() // refresh l'affichage du menu
    }
}