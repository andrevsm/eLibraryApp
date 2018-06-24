package up.edu.br.elibrary;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LivroDao {
    public void salvar(Livro l) {
        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("titulo", l.getTitulo());
        values.put("autor", l.getAutor());
        values.put("isbn", l.getIsbn());
        values.put("edicao", l.getEdicao());
        values.put("campus", l.getCampus());
        values.put("ano", l.getAno());
        values.put("capa", l.getCapa());
        values.put("status", Boolean.toString(l.isStatus()));

        if(l.getId() == null) {
            conn.insert("livro", null, values);
        } else {
            conn.update("livro", values, "id = ?", new String [] {l.getId().toString()});
        }
    }

    public List<Livro> listar() {
        SQLiteDatabase conn = Conexao.getInstance().getReadableDatabase();

        Cursor c;
        c = conn.query("livro", new String[] {
                        "id", "titulo", "autor", "isbn", "edicao", "campus", "ano",
                        "capa", "status"},
                null, null, null, null, "titulo");

        ArrayList<Livro> livros = new ArrayList<Livro>();

        if(c.moveToFirst()) {
            do {
                Livro livro = new Livro();

                livro.setId(c.getInt(c.getColumnIndex("id")));
                livro.setTitulo(c.getString(c.getColumnIndex("titulo")));
                livro.setAutor(c.getString(c.getColumnIndex("autor")));
                livro.setIsbn(c.getString(c.getColumnIndex("isbn")));
                livro.setEdicao(c.getString(c.getColumnIndex("edicao")));
                livro.setCampus(c.getString(c.getColumnIndex("campus")));
                livro.setAno(c.getInt(c.getColumnIndex("ano")));
                livro.setCapa(c.getBlob(c.getColumnIndex("capa")));
                livro.setStatus(Boolean.parseBoolean
                        (c.getString(c.getColumnIndex("status"))));
                livros.add(livro);
            } while (c.moveToNext());
            c.close();
        }
        return livros;
    }

    public void excluir(Livro l) {
        SQLiteDatabase conn = Conexao.getInstance().getWritableDatabase();

        conn.delete("livro", "id = ?", new String[] {l.getId().toString()});
    }
}


