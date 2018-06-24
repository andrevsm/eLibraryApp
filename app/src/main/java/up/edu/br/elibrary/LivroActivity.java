package up.edu.br.elibrary;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class LivroActivity extends AppCompatActivity {
    Livro livro = new Livro();
    int CAMERA_REQUEST = 1888;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_livro);

        //Pegando os objetos da View
        final TextView txtTitulo = (TextView) findViewById(R.id.txtTitulo);
        final TextView txtAutor = (TextView) findViewById(R.id.txtAutor);
        final TextView txtIsbn = (TextView) findViewById(R.id.txtISBN);
        final TextView txtCampus = (TextView) findViewById(R.id.txtCampus);
        final TextView txtAno = (TextView) findViewById(R.id.txtAno);
        final TextView txtEdicao = (TextView) findViewById(R.id.txtEdicao);
        final ImageView imageCapa = (ImageView) findViewById(R.id.capaLivro);
        final TextView txtStatus = (TextView) findViewById(R.id.txtStatus);
        Button btnSave = (Button) findViewById(R.id.btnSave);
        Button btnChange = (Button) findViewById(R.id.btnChangeStatus);
        Button btnDelete = (Button) findViewById(R.id.btnDelete);
        txtStatus.setText("Disponível");

        //Intent para filtrar se for Editar
        Intent it = getIntent();
        String action = it.getAction();
        String type = it.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                Uri imageUri = (Uri) it.getParcelableExtra(Intent.EXTRA_STREAM);
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageCapa.setImageBitmap(bitmap);
            }
        }

        if (it != null && it.hasExtra("livro")) {
                livro = (Livro) it.getSerializableExtra("livro");

                //Preenchendo o livro
                txtTitulo.setText(livro.getTitulo());
                txtAutor.setText(livro.getAutor());
                txtIsbn.setText(livro.getIsbn());
                txtEdicao.setText(livro.getEdicao());
                txtCampus.setText(livro.getCampus());
                txtAno.setText(Integer.toString(livro.getAno()));
                ByteArrayInputStream imageStream = new ByteArrayInputStream(livro.getCapa());
                Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
                imageCapa.setImageBitmap(bitmap);
                if (Boolean.toString(livro.isStatus()).equals("true")) {
                    txtStatus.setText("Disponível");
                    livro.setStatus(true);
                } else {
                    txtStatus.setText("Reservado");
                    livro.setStatus(false);
                }
            } else {
                btnDelete.setVisibility(View.GONE);
            }


            //Abrindo a Câmera
            imageCapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);

                }
            });

            btnChange.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView txtStatus = (TextView) findViewById(R.id.txtStatus);

                    if (txtStatus.getText().toString().equals("Disponível")) {
                        txtStatus.setText("Reservado");
                        livro.setStatus(false);
                    } else {
                        txtStatus.setText("Disponível");
                        livro.setStatus(true);
                    }
                }
            });

            //BOTÃO SALVAR
            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (livro == null) {
                        livro = new Livro();
                    }

                    livro.setTitulo(txtTitulo.getText().toString());
                    livro.setAutor(txtAutor.getText().toString());
                    livro.setIsbn(txtIsbn.getText().toString());
                    livro.setEdicao(txtEdicao.getText().toString());
                    livro.setCampus(txtCampus.getText().toString());
                    livro.setAno(Integer.parseInt(txtAno.getText().toString()));

                    //TRATANDO O BITMAP PARA UM ARRAY DE BYTES
                    Bitmap bitmap = ((BitmapDrawable) imageCapa.getDrawable()).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageInBytes = baos.toByteArray();
                    livro.setCapa(imageInBytes);

                    //Tratando o Status
                    if (txtStatus.getText().toString().equals("Reservado")) {
                        livro.setStatus(false);
                    } else {
                        livro.setStatus(true);
                    }

                    //Instânciando o banco e salvando
                    new LivroDao().salvar(livro);
                    livro = null;

                    //Toast message
                    Toast.makeText(getApplicationContext(), "Salvo com sucesso!",
                            Toast.LENGTH_SHORT).show();

                    //Mudando a Intent
                    Intent it = new Intent(LivroActivity.this, MainActivity.class);
                    startActivity(it);
                }
            });


            //Deletar
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LivroActivity.this);
                    builder.setTitle("Excluir");
                    builder.setMessage("Tem certeza que deseja excluir?");
                    builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            new LivroDao().excluir(livro);
                            dialog.dismiss();
                            Intent it = new Intent(LivroActivity.this, MainActivity.class);
                            startActivity(it);
                        }

                    });

                    builder.setNegativeButton("Não", null);
                    builder.show();

                }
            });
        }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            if (resultCode == Activity.RESULT_OK) {
                if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
                    Bitmap photo = (Bitmap) data.getExtras().get("data");
                    ImageView image = (ImageView) findViewById(R.id.capaLivro);
                    image.setImageBitmap(photo);
                }
            }
        }
    }