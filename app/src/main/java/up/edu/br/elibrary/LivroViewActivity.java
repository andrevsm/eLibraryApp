package up.edu.br.elibrary;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class LivroViewActivity extends AppCompatActivity {
    Livro livro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livro);

        //Pegando os objetos da View
        final TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        final TextView txtAutor = (TextView) findViewById(R.id.txtAutor);
        final TextView txtIsbn = (TextView) findViewById(R.id.txtISBN);
        final TextView txtCampus = (TextView) findViewById(R.id.txtCampus);
        final TextView txtAno = (TextView) findViewById(R.id.txtAno);
        final TextView txtEdicao = (TextView) findViewById(R.id.txtEdicao);
        final ImageView imageCapa = (ImageView) findViewById(R.id.capaLivro);
        final TextView txtStatus = (TextView) findViewById(R.id.txtStatus);
        Button btnReservar = (Button)findViewById(R.id.btnReservar);


        //Intent para filtrar se for Editar
        Intent it = getIntent();

        if (it != null && it.hasExtra("livro")) {
            livro = (Livro) it.getSerializableExtra("livro");

            //Preenchendo o livro
            txtTitulo.setText(livro.getTitulo());
            txtAutor.setText(livro.getAutor());
            txtIsbn.setText("ISBN: " + livro.getIsbn());
            txtEdicao.setText("Edição: " + livro.getEdicao());
            txtCampus.setText("Campus: " + livro.getCampus());
            txtAno.setText("Ano: " + Integer.toString(livro.getAno()));
            ByteArrayInputStream imageStream = new ByteArrayInputStream(livro.getCapa());
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            imageCapa.setImageBitmap(bitmap);
            if(Boolean.toString(livro.isStatus()).equals("true")){
                txtStatus.setText("Disponível");
            } else {
                txtStatus.setText("Reservado");
            }

        }

        btnReservar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Boolean.toString(livro.isStatus()).equals("false")){
                    Toast.makeText(getApplicationContext(),
                            "Livro indisponível.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    livro.setStatus(false);

                    new LivroDao().salvar(livro);

                    //Notificação
                    Calendar calendar = Calendar.getInstance();
                    Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            NotificationCompat.Builder mBuilder =
                                    new NotificationCompat.Builder(LivroViewActivity.this)
                                            .setSmallIcon(R.drawable.notificationo1)
                                            .setContentTitle("Livro")
                                            .setContentText("Disponível para retirada em até 1 dia útil");
                            // Cria o intent que irá chamar a atividade a ser aberta quando clicar na notifição
                            Intent resultIntent = new Intent(LivroViewActivity.this, MainActivity.class);

                            //PendingIntent é "vinculada" a uma notification para abrir a intent
                            PendingIntent resultPendingIntent = PendingIntent.
                                    getActivity(LivroViewActivity.this, 0, resultIntent, 0);

                            //associa o intent na notificação
                            mBuilder.setContentIntent(resultPendingIntent);
                            NotificationManager mNotificationManager =
                                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            //gera a notificação
                            mNotificationManager.notify(99, mBuilder.build());
                        }
                    }, calendar.getTime());

                    livro = null;

                    //Mudando a Intent
                    Intent it = new Intent(LivroViewActivity.this, MainActivity.class);
                    startActivity(it);
                }
            }
        });

        Button btnEmail = (Button) findViewById(R.id.btnEmail);

        btnEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse("mailto:" + "suporte@elibrary.com.br"));

                startActivity(Intent.createChooser(emailIntent, "Email do contato"));
            }
        });
    }





}
