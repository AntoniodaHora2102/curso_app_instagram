package co.tiagoaguiar.course.instagram.common.view

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import android.widget.TextView
import co.tiagoaguiar.course.instagram.R
import co.tiagoaguiar.course.instagram.databinding.DialogCustomBinding

class CustomDialog(context: Context) : Dialog(context) {

    //como o DIALOG funciona como uma atividade vamos inicia-lo
    private lateinit var binding: DialogCustomBinding

    private lateinit var txtButton: Array<TextView>

    private var titleId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DialogCustomBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }

    override fun setTitle(titleId: Int) {
        this.titleId = titleId
    }

    // funcao que irá ouvir o evento de click do button
    fun addButton(vararg texts: Int, listener: View.OnClickListener) { // vararg nos permite passar 1 ou N argumentos

        //ARRAY de textView
        txtButton = Array(texts.size) {

            //iremos  instanciar um TEXTVIEW de forma programatica
            TextView(context)
        }

        //loops do TEXTVIEW
        texts.forEachIndexed { index, txtId ->
            //id fixo para os txtId
            txtButton[index].id = txtId

            //armazenar dentro do indice do array
            txtButton[index].setText(txtId) // IRA ADICIONAR O TEXTO

            //ouvindo o evento de click
            txtButton[index].setOnClickListener {
                listener.onClick(it)
                dismiss() // ira esconder a DIALOG
            }
        }
    }

    //vamos subescrever o metodo show
    override fun show() {
        requestWindowFeature(Window.FEATURE_NO_TITLE) // irá retirar o title das dialog nas versões antigas
        super.show()

        titleId?.let {
            binding.dialogTitle.setText(it)
        }

        //loop dentro do array para adicionar dentro do DIALOG
        for (textView in txtButton) {

        //IREMOS ADICONAR AS MARGENS DINAMICAMENTE
        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(30, 50, 30,50)

            binding.dialogContainer.addView(textView, layoutParams) // adicionar o BUTTON + LAYOUTPARAMS
        }
    }
}