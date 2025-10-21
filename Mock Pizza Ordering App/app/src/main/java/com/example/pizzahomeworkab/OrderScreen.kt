package com.example.pizzahomeworkab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class OrderScreen : AppCompatActivity() {

    // Selections
    private lateinit var style2: TextView
    private lateinit var size2: TextView
    private lateinit var slice2: TextView
    private lateinit var toppings2: TextView
    private lateinit var pizzaPic2: ImageView

    // Quantity
    private lateinit var quantityCounter: TextView
    private lateinit var minusbutton: Button
    private lateinit var addButton: Button
    private var quantity = 1

    //Pricing & Counters
    private lateinit var subCount: TextView
    private lateinit var deliveryCount: TextView
    private lateinit var deliverySwitch: Switch
    private lateinit var taxCount: TextView
    private lateinit var tipCount: TextView
    private lateinit var tipBarSlide: SeekBar
    private lateinit var tipPrctgCount: TextView
    private lateinit var totalCount: TextView

    // Edit & Order
    private lateinit var editButton: Button
    private lateinit var orderButton: Button

    // Prices
    private var subtotalCount = 0.0
    private var dlvFee = 0.0
    private var tax = 0.0
    private var tip = 0.0
    private var grandtotal = 0.0
    private var tipPrctg = 20

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_secondactivity)

        // Pizza Selecitions
        pizzaPic2 = findViewById(R.id.imageView2)
        style2 = findViewById(R.id.finalStyle)
        size2 = findViewById(R.id.finalSize)
        slice2 = findViewById(R.id.finalCut)
        toppings2 = findViewById(R.id.finalTopings)

        // quantity
        quantityCounter = findViewById(R.id.quantity)
        minusbutton = findViewById(R.id.minus)
        addButton = findViewById(R.id.add)

        // Pricing
        subCount = findViewById(R.id.sub2)
        deliveryCount = findViewById(R.id.delivfee)
        deliverySwitch = findViewById(R.id.switch1)
        taxCount = findViewById(R.id.tax)
        tipCount = findViewById(R.id.tip)
        tipPrctgCount = findViewById(R.id.percent)
        totalCount = findViewById(R.id.total)
        tipBarSlide = findViewById(R.id.tipBar)

        // Edit & order buttons
        editButton = findViewById(R.id.edit)
        orderButton = findViewById(R.id.Order)

        // Get data from the previous activity
        subtotalCount = intent.getDoubleExtra("Subtotal", 0.0)
        val zaStyle = intent.getStringExtra("Style") ?: "Margherita"
        val zaSize = intent.getStringExtra("Size") ?: "Medium"
        val zaSlice = intent.getStringExtra("Slice") ?: "Classic Triangle Slices"
        val zaTopCount = intent.getIntExtra("Toppings_Count", 0)
        val zaImageId = intent.getIntExtra("PizzaIMG", R.drawable.margherita)

        // Display Pizza Selections from previous
        style2.text = zaStyle
        size2.text = zaSize
        slice2.text = zaSlice
        toppings2.text = "Extra Toppings: $zaTopCount"
        pizzaPic2.setImageResource(zaImageId)

        // Tip Slide Bar
        tipBarSlide.max = 100
        tipBarSlide.progress = tipPrctg
        tipPrctgCount.text = "$tipPrctg%"
        updateTotal()

        // Set Listeners
        deliverySwitch.setOnCheckedChangeListener { _, isChecked ->         // delivery switch
            dlvFee = if (isChecked) 2.0 else 0.0
            deliverySwitch.text = if (isChecked) "Yes: $2.00" else "No: $0.00"
            updateTotal()
        }
        minusbutton.setOnClickListener {                                    // minus button
            if (quantity > 1) {
                quantity--
                quantityCounter.text = quantity.toString()
                updateTotal()
            }
        }
        addButton.setOnClickListener {                                      // add button
            quantity++
            quantityCounter.text = quantity.toString()
            updateTotal()
        }
        tipBarSlide.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {       //tip slide  bar
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tipPrctg = progress
                tipPrctgCount.text = "$tipPrctg%"
                updateTotal()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        editButton.setOnClickListener {                                 // edit button
            finish()                                                    // Go back to the selection screen
        }
        orderButton.setOnClickListener {                                // Order Button

            // Oder confirmation
            Toast.makeText(
                this,
                "Congratulations Order Confirmed! Total: $${String.format("%.2f", grandtotal)}",
                Toast.LENGTH_LONG
            ).show()

            // Go back to selection screen
            val goBack = Intent(this, SelectionScreen::class.java)
            goBack.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(goBack)
        }
    }
    private fun updateTotal() {
        // Calculate total price
        val baseSubtotal = subtotalCount * quantity
        subCount.text = "$${String.format("%.2f", baseSubtotal)}"
        deliveryCount.text = "$${String.format("%.2f", dlvFee)}"
        tax = (baseSubtotal + dlvFee) * 0.0635 // Calculate tax
        taxCount.text = "$${String.format("%.2f", tax)}"
        tip = (baseSubtotal + dlvFee) * (tipPrctg / 100.0) // Calculate tip
        tipCount.text = "$${String.format("%.2f", tip)}"
        grandtotal = baseSubtotal + dlvFee + tax + tip
        totalCount.text = "$${String.format("%.2f", grandtotal)}"
    }
}