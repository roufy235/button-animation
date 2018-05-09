package com.example.roufy235.signupanimation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Intent
import android.databinding.DataBindingUtil
import android.graphics.Color
import android.graphics.PorterDuff
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewGroup
import com.example.roufy235.signupanimation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var mBinding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
    }

    fun load(view : View) {
        animateBtnWidth()
        fadeOutTextAndSetProgressDialog()
        nextAction()
    }

    private fun animateBtnWidth() {
        val anim : ValueAnimator = ValueAnimator.ofInt(mBinding!!.signInBtn.measuredWidth, getFinalWidth())

        anim.addUpdateListener { animation ->

            val value : Int = animation!!.animatedValue.toString().toInt()
            val layoutParams : ViewGroup.LayoutParams = mBinding!!.signInBtn.layoutParams

            layoutParams.width = value
            mBinding!!.signInBtn.requestLayout()
        }

        anim.duration = 250
        anim.start()

    }

    private fun nextAction() {
        Handler().postDelayed({
            revealBtn()
            fadeOutProgressDialog()
            delayedStartNextActivity()
        }, 2000)
    }

    private fun fadeOutProgressDialog() {
        mBinding!!.progressBar.animate().alpha(0f).setDuration(200).start()

    }

    private fun delayedStartNextActivity() {
        Handler().postDelayed({

            //load second activity
            startActivity(Intent(this, SecondView::class.java))
        }, 2000)
    }

    private fun revealBtn() {
        mBinding!!.signInBtn.elevation = 0f
        mBinding!!.revealView.visibility = View.VISIBLE

        val x = mBinding!!.revealView.width
        val y = mBinding!!.revealView.height

        val startX = (getFinalWidth() / 2 + mBinding!!.signInBtn.x).toInt()
        val startY = (getFinalWidth() / 2 + mBinding!!.signInBtn.y).toInt()

        val radius = Math.max(x, y) * 1.2f

        val reveal : Animator = ViewAnimationUtils.createCircularReveal(mBinding!!.revealView, startX, startY, getFinalWidth().toFloat(), radius)
        reveal.duration = 350
        reveal.addListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation : Animator?) {
                super.onAnimationEnd(animation)

                finish()
            }
        })

        reveal.start()
    }

    private fun showProgressDialog() {
        mBinding!!.progressBar.indeterminateDrawable.setColorFilter(Color.parseColor("#ffffff"), PorterDuff.Mode.SRC_IN)

        mBinding!!.progressBar.visibility = View.VISIBLE
    }

    private fun fadeOutTextAndSetProgressDialog() {
        mBinding!!.signInTxt.animate().alpha(0f).setDuration(250).setListener(object : AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation : Animator?) {
                super.onAnimationEnd(animation)
                showProgressDialog()
            }
        }).start()
    }

    private fun getFinalWidth() :Int {
        return resources.getDimension(R.dimen.get_width).toInt()
    }
}
