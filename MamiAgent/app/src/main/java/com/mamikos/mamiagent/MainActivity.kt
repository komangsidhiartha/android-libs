package com.mamikos.mamiagent

import com.mamikos.mamiagent.networks.ProductApi
import com.mamikos.mamiagent.networks.ProductResponse
import com.sidhiartha.libs.activities.BaseActivity
import com.sidhiartha.libs.apps.logIfDebug
import org.jetbrains.anko.toast

class MainActivity : BaseActivity()
{
    override val layoutResource: Int = R.layout.activity_main

    override fun viewDidLoad()
    {
        ProductApi.HatApi().execute(ProductResponse::class.java){ response: ProductResponse?, errorMessage: String? ->
            when (response)
            {
                null ->
                {
                    showErrorMessage(errorMessage)
                }
                else ->
                {
                    logIfDebug("response hat $response")
                }
            }
        }

        ProductApi.ShirtApi().execute(ProductResponse::class.java){ response: ProductResponse?, errorMessage: String? ->
            when (response)
            {
                null ->
                {
                    showErrorMessage(errorMessage)
                }
                else ->
                {
                    logIfDebug("response shirt $response")
                }
            }
        }

        ProductApi.JeansApi().execute(ProductResponse::class.java){ response: ProductResponse?, errorMessage: String? ->
            when (response)
            {
                null ->
                {
                    showErrorMessage(errorMessage)
                }
                else ->
                {
                    logIfDebug("response jeans $response")
                }
            }
        }
    }

    private fun showErrorMessage(errorMessage: String?)
    {
        if (errorMessage != null)
            toast(errorMessage)
    }
}
