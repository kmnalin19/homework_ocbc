package com.app.homework.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.homework.R


/**
 * AppCompatActivity extension function for add fragment to View
 * @param fragment
 */
internal fun AppCompatActivity.addFragment(fragment : Fragment){
    val transaction = supportFragmentManager.beginTransaction()
    transaction.replace(R.id.container, fragment, "CollectDetailPageFragment")
    transaction.addToBackStack(null)
    transaction.commit()
}