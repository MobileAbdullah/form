package com.example.form


import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class FormActivity() : AppCompatActivity(), Parcelable {

    private lateinit var nameEditText: EditText
    private lateinit var surnameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var phoneEditText: EditText
    private lateinit var departmentEditText: EditText
    private lateinit var btnSubmit: Button
    private lateinit var dbHelper: ItemDatabaseHelper
    private var itemId: Long? = null

    constructor(parcel: Parcel) : this() {
        itemId = parcel.readValue(Long::class.java.classLoader) as? Long
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(itemId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<FormActivity> {
        override fun createFromParcel(parcel: Parcel): FormActivity {
            return FormActivity(parcel)
        }

        override fun newArray(size: Int): Array<FormActivity?> {
            return arrayOfNulls(size)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        nameEditText = findViewById(R.id.etName)
        surnameEditText = findViewById(R.id.etSurname)
        addressEditText = findViewById(R.id.etAddress)
        phoneEditText = findViewById(R.id.etPhone)
        departmentEditText = findViewById(R.id.etDepartment)
        btnSubmit = findViewById(R.id.btnSubmit)
        dbHelper = ItemDatabaseHelper(this)





        itemId = intent.getLongExtra("item_id", -1)
        if (itemId != -1L) {
            nameEditText.setText(intent.getStringExtra("name"))
            surnameEditText.setText(intent.getStringExtra("surname"))
            addressEditText.setText(intent.getStringExtra("address"))
            phoneEditText.setText(intent.getStringExtra("phone"))
            departmentEditText.setText(intent.getStringExtra("department"))
        }

        btnSubmit.setOnClickListener {
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val address = addressEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val department = departmentEditText.text.toString()

            if (name.isNotEmpty() && surname.isNotEmpty()) {
                if (phone.isNotEmpty() && !phone.matches("\\d+".toRegex())) {
                    Toast.makeText(
                        this,
                        "Telefon numarası sadece rakam içermelidir.",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                if (name.isNotEmpty() && surname.isNotEmpty()) {
                    if (itemId != null && itemId != -1L) {
                        // Veriyi güncelle
                        dbHelper.updateItem(itemId!!, name, surname, address, phone, department)
                    } else {
                        // Yeni veri ekle
                        dbHelper.insertItem(name, surname, address, phone, department)
                    }
                    finish()
                } else {
                    Toast.makeText(this, "Lütfen isim ve soyisim girin", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}