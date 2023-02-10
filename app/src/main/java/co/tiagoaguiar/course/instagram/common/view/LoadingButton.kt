package co.tiagoaguiar.course.instagram.common.view

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import co.tiagoaguiar.course.instagram.R

//o button será um frameLayout
class LoadingButton : FrameLayout {

    private lateinit var button : Button
    private lateinit var progress: ProgressBar
    private var text: String? = null // variavel pode ser nula ou não

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {

        setup(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        setup(context, attrs)
    }

    //funcao - iremos passar o contexto e os atributos
    private  fun setup(context: Context, attrs: AttributeSet?) {

        //iremos inflar o Layout do button
        val inflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) // getSystemService - manipula qualquer serviço do android
            as LayoutInflater
        inflater.inflate(R.layout.button_loading, this)

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.LoadingButton, 0 ,0)
       text = typedArray.getString(R.styleable.LoadingButton_text)


        button = getChildAt(0) as Button // converter para button
        progress = getChildAt(1) as ProgressBar // converter para progressBar

        //aguardo a informacao do style criado dentro do BUTTON
        button.text = text
        button.isEnabled = false

        //array
        typedArray.recycle()
    }

    //sobescrever a funcao isEnabled
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        button.isEnabled = enabled
    }

    override fun setOnClickListener(l: OnClickListener?) {
        button.setOnClickListener(l)
    }


    //funcao para ativar o progressBar
    public fun showProgress(enabled: Boolean) {
        if(enabled) {
            button.text = "" // botão ficar sem informação de texto
            button.isEnabled = false // o button ficará desativado
            progress.visibility = View.VISIBLE // ira exibir a progressBar

        } else {
            button.text = text // retornara para o texto inicial do button
            button.isEnabled = true // ira ativar o button novamente
            progress.visibility = View.GONE // ira esconder a progressBar
        }
    }
}