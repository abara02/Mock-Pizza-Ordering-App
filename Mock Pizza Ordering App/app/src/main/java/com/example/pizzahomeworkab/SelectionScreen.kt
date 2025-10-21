package com.example.pizzahomeworkab

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SelectionScreen : AppCompatActivity() {

    // Pizza Customization
    private lateinit var style: RadioGroup
    private lateinit var size: RadioGroup
    private lateinit var slice: RadioGroup
    private lateinit var toppings: List<CheckBox>
    private lateinit var pizzaPic: ImageView

    // Subtotal UI
    private lateinit var subtotalCount: TextView
    private var subtotal: Double = 0.0

    // Restart & Checkout Buttons
    private lateinit var restartButton: Button
    private lateinit var checkoutButton: Button

    // Added media player
    private var mp: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Pizza Customization
        pizzaPic = findViewById(R.id.zaza)
        style = findViewById(R.id.styleRGroup)
        size = findViewById(R.id.sizeRGroup)
        slice = findViewById(R.id.sliceRGroup)
        toppings = listOf(
            findViewById(R.id.tom),
            findViewById(R.id.mozz),
            findViewById(R.id.olive),
            findViewById(R.id.onion),
            findViewById(R.id.sausg),
            findViewById(R.id.pepper)
        )

        // Subtotal, Restart & Checkout
        subtotalCount = findViewById(R.id.price)
        restartButton = findViewById(R.id.restart)
        checkoutButton = findViewById(R.id.checkout)

        // Set Listeners for Buttons
        style.setOnCheckedChangeListener { _, checkedId ->
            updatePizza(checkedId, pizzaPic)
        }
        size.setOnCheckedChangeListener { _, _ ->
            calcPrice(size, toppings, subtotalCount)
        }
        slice.setOnCheckedChangeListener { _, _ -> }

        toppings.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, _ ->
                calcPrice(size, toppings, subtotalCount)
            }
        }

        // Restart button resets selections
        restartButton.setOnClickListener {
            resetPizza(style, size, slice, toppings, pizzaPic, subtotalCount)
        }

        checkoutButton.setOnClickListener {
            if (style.checkedRadioButtonId == -1 || size.checkedRadioButtonId == -1 || slice.checkedRadioButtonId == -1) {
                Toast.makeText(this, "Please make required selections: Style, Size, and Slice", Toast.LENGTH_SHORT).show()
            } else {
                val toNext = Intent(this, OrderScreen::class.java)
                toNext.putExtra("Subtotal", subtotal)

                val pizzaStyle = when (style.checkedRadioButtonId) {
                    R.id.marg -> "Margherita"
                    R.id.pep -> "Pepperoni"
                    R.id.bbqchx -> "BBQ Chicken"
                    R.id.hawaii -> "Hawaiian"
                    else -> null
                }
                toNext.putExtra("Style", pizzaStyle)

                val pizzaSize = when (size.checkedRadioButtonId) {
                    R.id.small -> "Small"
                    R.id.med -> "Medium"
                    R.id.large -> "Large"
                    else -> null
                }
                toNext.putExtra("Size", pizzaSize)

                val pizzaSlice = when (slice.checkedRadioButtonId) {
                    R.id.triangle -> "Classic Triangle Slices"
                    R.id.square -> "Square Slices"
                    else -> null
                }
                toNext.putExtra("Slice", pizzaSlice)

                val toppingsCount = toppings.count { it.isChecked }
                toNext.putExtra("Topppings_Count", toppingsCount)

                val pizzaImageId = when (style.checkedRadioButtonId) {
                    R.id.marg -> R.drawable.margherita
                    R.id.pep -> R.drawable.pepperoni
                    R.id.bbqchx -> R.drawable.bbq_chicken
                    R.id.hawaii -> R.drawable.hawaiian
                    else -> R.drawable.pizza_crust
                }
                toNext.putExtra("PizzaIMG", pizzaImageId)

                startActivity(toNext)
            }
        }
    }

    // Update picture and play song
    private fun updatePizza(checkedId: Int, pizzaPic: ImageView) {
        val id = when (checkedId) {
            R.id.marg -> {
                mp = MediaPlayer.create(this, R.raw.song)
                R.drawable.margherita
            }
            R.id.pep -> {
                mp = MediaPlayer.create(this, R.raw.song)
                R.drawable.pepperoni
            }
            R.id.bbqchx -> {
                mp = MediaPlayer.create(this, R.raw.song)
                R.drawable.bbq_chicken
            }
            R.id.hawaii -> {
                mp = MediaPlayer.create(this, R.raw.song)
                R.drawable.hawaiian
            }
            else -> R.drawable.pizza_crust
        }

        pizzaPic.setImageResource(id)

        // Start italian song
        mp?.start()
    }

    // Calculate the total cost
    private fun calcPrice(size: RadioGroup, toppings: List<CheckBox>, subtotalCount: TextView) {
        val sizeCost = when (size.checkedRadioButtonId) {
            R.id.small -> 10.29
            R.id.med -> 12.59
            R.id.large -> 14.89
            else -> 0.0
        }

        val toppingCost = when (size.checkedRadioButtonId) {
            R.id.small -> 1.39
            R.id.med -> 2.29
            R.id.large -> 2.99
            else -> 0.0
        }

        val selectedToppings = toppings.count { it.isChecked }
        subtotal = sizeCost + (selectedToppings * toppingCost)
        subtotalCount.text = "$${"%.2f".format(subtotal)}"
    }
    // Pause the music when you close out of app
    override fun onPause() {
        super.onPause()
        mp?.pause()
    }
    override fun onDestroy() {
        super.onDestroy()
        mp?.release()
        mp = null
    }

}

// Restart everything back to blank
private fun resetPizza(
    style: RadioGroup, size: RadioGroup, slice: RadioGroup,
    toppings: List<CheckBox>, pizzaPic: ImageView, subtotalTextView: TextView
) {
    style.clearCheck()
    size.clearCheck()
    slice.clearCheck()
    toppings.forEach { it.isChecked = false }
    pizzaPic.setImageResource(R.drawable.pizza_crust)
    subtotalTextView.text = "$0.00"
}
