package up.edu.br.elibrary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent it = new Intent(MainActivity.this, LivroActivity.class);
                startActivity(it);
            }
        });
        //CRIANDO O BANCO
        new Conexao(getApplicationContext(), "livroContext.db", null, 1);

        //SETTANDO ADAPTER
        ListView listLivros = (ListView)findViewById(R.id._listLivros);
        LivroAdapter livroAdapter = new LivroAdapter(new LivroDao().listar(), this);
        listLivros.setAdapter(livroAdapter);

        //CLICK DO ADAPTER
        listLivros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                //Pegando posição no adapter
                Livro livro = (Livro)parent.getItemAtPosition(position);

                //Criando a Intent
                Intent editLivro = new Intent(MainActivity.this, LivroActivity.class);
                editLivro.putExtra("livro", livro);
                startActivity(editLivro);

                return true;
            }
        });

        //Click para visualizar
        listLivros.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Livro livro = (Livro) parent.getItemAtPosition(position);

                Intent it = new Intent(MainActivity.this, LivroViewActivity.class);
                it.putExtra("livro", livro);
                startActivity(it);

            }
        });

        //Long Click para Editar
        listLivros.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, int position, long id) {

                Livro livro = (Livro) parent.getItemAtPosition(position);

                Intent it = new Intent(MainActivity.this, LivroActivity.class);
                it.putExtra("livro", livro);
                startActivity(it);

                return true;
            }
        });

    }

}
