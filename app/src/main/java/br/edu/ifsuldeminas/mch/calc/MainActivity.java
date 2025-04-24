package br.edu.ifsuldeminas.mch.calc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ifsuldeminas.mch.calc";

    // armazena a expressao digitada
    private String expressao = "";

    private TextView textViewResultado;
    private TextView textViewUltimaExpressao;

    //declaração de todos os botões usados
    private Button buttonIgual, buttonSubtracao, numeroTres, numeroDois,
            numeroCinco, numeroSeis, buttonMultiplicacao, numeroSete, numeroOito,
            numeroNove, buttonDivisao, buttonReset, buttonDelete, buttonPorcento, numeroQuatro, numeroUm,
            buttonSoma, buttonVirgula, numeroZero;

    // mapea botoes da interface com as variáveis do código
    public void mapearBotoes() {
        numeroZero = findViewById(R.id.buttonZeroID);
        numeroUm = findViewById(R.id.buttonUmID);
        numeroDois = findViewById(R.id.buttonDoisID);
        numeroTres = findViewById(R.id.buttonTresID);
        numeroQuatro = findViewById(R.id.buttonQuatroID);
        numeroCinco = findViewById(R.id.buttonCincoID);
        numeroSeis = findViewById(R.id.buttonSeisID);
        numeroSete = findViewById(R.id.buttonSeteID);
        numeroOito = findViewById(R.id.buttonOitoID);
        numeroNove = findViewById(R.id.buttonNoveID);

        buttonSoma = findViewById(R.id.buttonSomaID);
        buttonSubtracao = findViewById(R.id.buttonSubtracaoID);
        buttonMultiplicacao = findViewById(R.id.buttonMultiplicacaoID);
        buttonDivisao = findViewById(R.id.buttonDivisaoID);
        buttonPorcento = findViewById(R.id.buttonPorcentoID);
        buttonVirgula = findViewById(R.id.buttonVirgulaID);
        buttonIgual = findViewById(R.id.buttonIgualID);
        buttonReset = findViewById(R.id.buttonResetID);
        buttonDelete = findViewById(R.id.buttonDeleteID);
        textViewResultado = findViewById(R.id.textViewResultadoID);
        textViewUltimaExpressao = findViewById(R.id.textViewUltimaExpressaoID);
    }

    // metodo chamado ao clicar em botões (números e operadores simples)
    // botoes e operacoes complexas (fora das bibliotecas) trato por metodo individual

    @Override
    public void onClick(View v) {
        Button botaoClicado = (Button) v;
        String valor = botaoClicado.getText().toString();

        expressao += valor;
        textViewUltimaExpressao.setText(expressao);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapearBotoes();

        numeroZero.setOnClickListener(this);
        numeroUm.setOnClickListener(this);
        numeroDois.setOnClickListener(this);
        numeroTres.setOnClickListener(this);
        numeroQuatro.setOnClickListener(this);
        numeroCinco.setOnClickListener(this);
        numeroSeis.setOnClickListener(this);
        numeroSete.setOnClickListener(this);
        numeroOito.setOnClickListener(this);
        numeroNove.setOnClickListener(this);
        buttonVirgula.setOnClickListener(this);
        buttonSoma.setOnClickListener(this);
        buttonSubtracao.setOnClickListener(this);
        buttonMultiplicacao.setOnClickListener(this);
        buttonDivisao.setOnClickListener(this);

        // configura o botao de porcentagem (mostra %, mas trata depois)
        buttonPorcento.setOnClickListener(v -> {
            expressao += "%";
            textViewUltimaExpressao.setText(expressao);
        });

        // botao de igual: trata %, avalia e mostra o resultado
        buttonIgual.setOnClickListener(v -> {
            try {
                String expressaoTratada = tratarPorcentagem(expressao);
                Calculable avaliadorExpressao = new ExpressionBuilder(expressaoTratada).build();
                Double resultado = avaliadorExpressao.calculate();

                textViewUltimaExpressao.setText(expressao);
                textViewResultado.setText(resultado.toString());

                expressao = resultado.toString();
            } catch (Exception e) {
                Log.e(TAG, "Erro ao calcular expressão: " + e.getMessage());
                textViewResultado.setText("Erro");
            }
        });

        // botao C limpar
        buttonReset.setOnClickListener(v -> {
            expressao = "";
            textViewUltimaExpressao.setText("");
            textViewResultado.setText("");
        });

        // pega o ultimo da expressao e apaga
        buttonDelete.setOnClickListener(v -> {
            if (!expressao.isEmpty()) {
                expressao = expressao.substring(0, expressao.length() - 1);
                textViewUltimaExpressao.setText(expressao);
            }
        });
    }

    //  trata expressões com porcentagem, transformando 80+50% em 80+(80*50/100)
    private String tratarPorcentagem(String expr) {
        Pattern padrao = Pattern.compile("(\\d+(\\.\\d+)?)([\\+\\-\\*/])(\\d+(\\.\\d+)?)%");
        Matcher matcher = padrao.matcher(expr);
        StringBuffer novaExpressao = new StringBuffer();

        while (matcher.find()) {
            String numeroAnterior = matcher.group(1);
            String operador = matcher.group(3);
            String porcentagem = matcher.group(4);

            String substituto = numeroAnterior + operador + "(" + numeroAnterior + "*" + porcentagem + "/100)";
            matcher.appendReplacement(novaExpressao, substituto);
        }

        matcher.appendTail(novaExpressao);
        return novaExpressao.toString();
    }
}
