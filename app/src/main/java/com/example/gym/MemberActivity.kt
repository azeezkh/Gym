package com.example.gym

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*

class MemberActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)

        val submitButton : Button = findViewById(R.id.submit)
        val spinnerVal : Spinner = findViewById(R.id.membershipSV)
        var memberships = arrayOf("1 Month","3 Months", "6 Months", "12 Months")
        spinnerVal.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,memberships )

        var flag1 = "1 Month"

        spinnerVal.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                flag1 = memberships.get(p2)
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }




        submitButton.setOnClickListener {
            onClickAddName(flag1)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var dialog_var = InfoDialogClass()
        val mapIntent: Intent = Uri.parse(
            "geo:0,0?q=1600+Amphitheatre+Parkway,+Mountain+View,+Amman"
        ).let { location ->
            val location: Uri = Uri.parse("geo:37.422219,-122.08364?z=14")
            Intent(Intent.ACTION_VIEW, location)
        }
        when (item.itemId) {
            R.id.item1 -> dialog_var.show(supportFragmentManager, "Prices Information Dialog")
            R.id.item2 -> startActivity(mapIntent)
        }
        return true;
    }

    fun onClickAddName(flag : String) {
        val URL = "content://com.example.Gym.MemberProvider"
        val member = Uri.parse(URL)
        val phoneNumber = findViewById<View>(R.id.numberET) as EditText
        var count: Int = 0
        var c = contentResolver.query(member, null, null, null, null)

        if (c != null) {
            if (c?.moveToFirst()) {
                do {

                    if (c.getString(c.getColumnIndex(MemberProvider.NUMBER)) == phoneNumber.text.toString()) {
                        Toast.makeText(baseContext, "Number already exists", Toast.LENGTH_LONG)
                            .show()
                        count++
                    }


                } while (c.moveToNext())

            }
        }
        if (count == 0) {
            val values = ContentValues()
            values.put(
                MemberProvider.F_NAME,
                (findViewById<View>(R.id.firstET) as EditText).text.toString()
            )
            values.put(
                MemberProvider.L_NAME,
                (findViewById<View>(R.id.lastET) as EditText).text.toString()
            )
            values.put(
                MemberProvider.AGE,
                (findViewById<View>(R.id.ageET) as EditText).text.toString()
            )
            values.put(
                MemberProvider.NUMBER,
                (findViewById<View>(R.id.numberET) as EditText).text.toString()
            )
            values.put(
                MemberProvider.MEMBERSHIP,
                flag
            )


            val uri = contentResolver.insert(
                MemberProvider.CONTENT_URI, values
            )
            Toast.makeText(baseContext, uri.toString(), Toast.LENGTH_LONG).show()
        }
    }




}