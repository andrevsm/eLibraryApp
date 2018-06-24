package up.edu.br.elibrary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Conexao extends SQLiteOpenHelper {

    private static Conexao conexao;

    public Conexao(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        conexao = this;
    }

    public static Conexao getInstance() {
        return conexao;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String statement = " create table livro (" +
                " id integer primary key AUTOINCREMENT," +
                " titulo varchar(255)," +
                " autor varchar(50), " +
                " isbn varchar(50), " +
                " capa blob," +
                " edicao varchar(50), " +
                " campus varchar(50), " +
                " ano integer, " +
                " status integer" +
                ")";

        db.execSQL(statement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
