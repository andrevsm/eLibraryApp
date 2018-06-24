package up.edu.br.elibrary;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.util.List;

public class LivroAdapter extends BaseAdapter {
    private List<Livro> livros;
    Activity act;

    public LivroAdapter(List<Livro> livros, Activity act) {
        this.livros = livros;
        this.act = act;
    }

    @Override
    public int getCount() {
        return this.livros.size();
    }

    @Override
    public Object getItem(int position) {
        return this.livros.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.livro_adapter, parent, false);

        ImageView imageCapa = view.findViewById(R.id.capaLivro);
        TextView txtTitulo = view.findViewById(R.id.txtTitulo);
        TextView txtAutor = view.findViewById(R.id.txtAutor);
        TextView txtStatus = view.findViewById(R.id.txtStatus);
        Livro l = livros.get(position);

        ByteArrayInputStream imageStream = new ByteArrayInputStream(l.getCapa());
        Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
        imageCapa.setImageBitmap(bitmap);

        txtTitulo.setText(l.getTitulo());
        txtAutor.setText(l.getAutor());

        if(Boolean.toString(l.isStatus()).equals("false")){
            txtStatus.setText("Reservado");
        } else {
            txtStatus.setText("Disponível");
        }

        return view;
    }


    public void remove(Livro livro) {
        this.livros.remove(livro);
    }
}
