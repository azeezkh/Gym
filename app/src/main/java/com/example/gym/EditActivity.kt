package com.example.gym

import android.content.ContentResolver
import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)

        val getButton : Button = findViewById(R.id.getBtn)
        val updateButton : Button = findViewById(R.id.updateBtn)
        val deleteButton : Button = findViewById(R.id.deleteBtn)
        val number = findViewById(R.id.phoneN) as EditText
        val number2 = findViewById(R.id.phoneNum) as EditText


        getButton.setOnClickListener {
            var phone_number = number.text.toString()
            onClickGetMember(phone_number)
        }

        updateButton.setOnClickListener {
            var phone_number = number.text.toString()
            onClickUpdateMember(phone_number)
        }
        deleteButton.setOnClickListener {
            var phone_number = number.text.toString()
            var uri = contentResolver.delete(MemberProvider.CONTENT_URI,
                 "NUMBER= $phone_number",
                null)
            Toast.makeText(baseContext, uri.toString(), Toast.LENGTH_LONG).show()
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    fun onClickGetMember(number: String) {
        val URL = "content://com.example.Gym.MemberProvider"
        val member = Uri.parse(URL)
        val firstName = findViewById<View>(R.id.firstName) as EditText
        val lastName = findViewById<View>(R.id.lastName) as EditText
        val age = findViewById<View>(R.id.memberAge) as EditText
        val membership = findViewById<View>(R.id.membershipL) as EditText
        val phoneNumber = findViewById<View>(R.id.phoneNum) as EditText
        var phoneN : String = number

        var c = contentResolver.query(member, null, null, null, null)

        if (c != null) {
            if (c?.moveToFirst()) {
                do {

                    if(c.getString(c.getColumnIndex(MemberProvider.NUMBER)) == number) {
                        firstName.setText(c.getString(c.getColumnIndex(MemberProvider.F_NAME)))
                        lastName.setText(c.getString(c.getColumnIndex(MemberProvider.L_NAME)))
                        age.setText(c.getString(c.getColumnIndex(MemberProvider.AGE)))
                        membership.setText(c.getString(c.getColumnIndex(MemberProvider.MEMBERSHIP)))
                        phoneNumber.setText(c.getString(c.getColumnIndex(MemberProvider.NUMBER)))
                    }
                    else {
                        Toast.makeText(this, phoneN + "Does not exist",
                            Toast.LENGTH_SHORT).show()
                    }
                } while (c.moveToNext())

            }
        }
    }

    fun onClickUpdateMember(number: String) {
        val URL = "content://com.example.Gym.MemberProvider"
        val member = Uri.parse(URL)
        val firstName = findViewById<View>(R.id.firstName) as EditText
        val lastName = findViewById<View>(R.id.lastName) as EditText
        val age = findViewById<View>(R.id.memberAge) as EditText
        val membership = findViewById<View>(R.id.membershipL) as EditText
        val phoneNumber = findViewById<View>(R.id.phoneNum) as EditText
        var phoneN: String = number


        var c = contentResolver.query(member, null, null, null, null)

        if (c != null) {
            if (c?.moveToFirst()) {
                do {

                    if(c.getString(c.getColumnIndex(MemberProvider.NUMBER)) == number) {
                        val values = ContentValues()
                        values.put(
                            MemberProvider.F_NAME,
                            (findViewById<View>(R.id.firstName) as EditText).text.toString()
                        )
                        values.put(
                            MemberProvider.L_NAME,
                            (findViewById<View>(R.id.lastName) as EditText).text.toString()
                        )
                        values.put(
                            MemberProvider.AGE,
                            (findViewById<View>(R.id.memberAge) as EditText).text.toString()
                        )
                        values.put(
                            MemberProvider.NUMBER,
                            (findViewById<View>(R.id.phoneNum) as EditText).text.toString()
                        )
                        values.put(
                            MemberProvider.MEMBERSHIP,
                            (findViewById<View>(R.id.membershipL) as EditText).text.toString()
                        )
                        contentResolver.update(
                            MemberProvider.CONTENT_URI, values, "NUMBER = ?", arrayOf(phoneNumber.text.toString())
                        )
                    }

                } while (c.moveToNext())

            }
        }
    }
}