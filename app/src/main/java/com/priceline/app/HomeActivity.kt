package com.priceline.app

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.priceline.app.view.BooksNameListFragment

class HomeActivity : AppCompatActivity() {

    //region fragment override methods
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val booksNameListFragment = BooksNameListFragment()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            transaction.add(R.id.root_layout, booksNameListFragment, "BookNameList")
            transaction.commit()
            supportFragmentManager.executePendingTransactions()
        }

    }

    override fun onBackPressed() {
        val fm = supportFragmentManager
        if (fm.backStackEntryCount > 0) {
            fm.popBackStack()
        } else {
            val builder: AlertDialog.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                AlertDialog.Builder(this)
            } else {
                AlertDialog.Builder(this)
            }
            builder
                    .setMessage("Are you sure yow want to exit ? ")
                    .setPositiveButton("Yes") { dialog, which -> finish() }
                    .setNegativeButton("No") { dialog, which -> dialog.dismiss() }
                    .show()
        }
    }
    //endregion
}