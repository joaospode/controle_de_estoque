package com.example.controledeestoque.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.controledeestoque.model.Funcionario;
import com.example.controledeestoque.model.Uniforme;
import com.example.controledeestoque.model.HistoricoEntrega;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "uniformes.db";
    private static final int    DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Tabela Funcionarios
        db.execSQL(
                "CREATE TABLE funcionarios (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "  nome TEXT NOT NULL, " +
                        "  data_nascimento TEXT, " +
                        "  cpf TEXT UNIQUE, " +
                        "  setor TEXT, " +
                        "  funcao TEXT, " +
                        "  data_admissao TEXT" +
                        ");"
        );

        // Tabela Uniformes
        db.execSQL(
                "CREATE TABLE uniformes (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "  tipo TEXT NOT NULL, " +
                        "  ca TEXT" +
                        ");"
        );

        // Tabela Estoque
        db.execSQL(
                "CREATE TABLE estoque (" +
                        "  uniforme_id INTEGER PRIMARY KEY, " +
                        "  quantidade INTEGER, " +
                        "  FOREIGN KEY(uniforme_id) REFERENCES uniformes(id)" +
                        ");"
        );

        // Tabela Entregas
        db.execSQL(
                "CREATE TABLE entregas (" +
                        "  id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        "  funcionario_id INTEGER, " +
                        "  uniforme_id INTEGER, " +
                        "  quantidade INTEGER, " +
                        "  data_entrega TEXT, " +
                        "  FOREIGN KEY(funcionario_id) REFERENCES funcionarios(id), " +
                        "  FOREIGN KEY(uniforme_id)    REFERENCES uniformes(id)" +
                        ");"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No upgrade, recria tudo
        db.execSQL("DROP TABLE IF EXISTS entregas;");
        db.execSQL("DROP TABLE IF EXISTS estoque;");
        db.execSQL("DROP TABLE IF EXISTS uniformes;");
        db.execSQL("DROP TABLE IF EXISTS funcionarios;");
        onCreate(db);
    }

    // --- Métodos Funcionario ---

    public long insertFuncionario(Funcionario f) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("nome",             f.getNome());
        cv.put("data_nascimento",  f.getDataNascimento());
        cv.put("cpf",              f.getCpf());
        cv.put("setor",            f.getSetor());
        cv.put("funcao",           f.getFuncao());
        cv.put("data_admissao",    f.getDataAdmissao());
        return db.insert("funcionarios", null, cv);
    }

    public int deleteFuncionarioByCpf(String cpf) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("funcionarios", "cpf = ?", new String[]{ cpf });
    }

    public List<Funcionario> getAllFuncionarios() {
        List<Funcionario> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, nome FROM funcionarios ORDER BY nome", null);
        while (c.moveToNext()) {
            Funcionario f = new Funcionario();
            f.setId   (c.getInt   (0));
            f.setNome (c.getString(1));
            list.add(f);
        }
        c.close();
        return list;
    }

    // --- Métodos Uniforme ---

    public long insertUniforme(Uniforme u) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("tipo", u.getTipo());
        cv.put("ca",   u.getCa());
        return db.insert("uniformes", null, cv);
    }

    public int deleteUniformeByTipo(String tipo) {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete("uniformes", "tipo = ?", new String[]{ tipo });
    }

    public List<Uniforme> getAllUniformes() {
        List<Uniforme> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT id, tipo FROM uniformes ORDER BY tipo", null);
        while (c.moveToNext()) {
            Uniforme u = new Uniforme();
            u.setId  (c.getInt   (0));
            u.setTipo(c.getString(1));
            list.add(u);
        }
        c.close();
        return list;
    }

    // --- Métodos Estoque ---

    public boolean addStock(int uniformeId, int quantidade) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(
                "SELECT quantidade FROM estoque WHERE uniforme_id = ?",
                new String[]{ String.valueOf(uniformeId) }
        );
        int current = 0;
        if (c.moveToFirst()) {
            current = c.getInt(0);
            c.close();
            ContentValues cv = new ContentValues();
            cv.put("quantidade", current + quantidade);
            return db.update("estoque", cv, "uniforme_id = ?", new String[]{ String.valueOf(uniformeId) }) > 0;
        } else {
            c.close();
            ContentValues cv = new ContentValues();
            cv.put("uniforme_id", uniformeId);
            cv.put("quantidade",  quantidade);
            long id = db.insert("estoque", null, cv);
            return id != -1;
        }
    }

    public boolean removeStock(int uniformeId, int quantidade) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(
                "SELECT quantidade FROM estoque WHERE uniforme_id = ?",
                new String[]{ String.valueOf(uniformeId) }
        );
        if (!c.moveToFirst()) {
            c.close();
            return false;
        }
        int current = c.getInt(0);
        c.close();
        if (current < quantidade) return false;
        ContentValues cv = new ContentValues();
        cv.put("quantidade", current - quantidade);
        return db.update("estoque", cv, "uniforme_id = ?", new String[]{ String.valueOf(uniformeId) }) > 0;
    }

    // --- Métodos Entrega / Histórico ---

    /**
     * Registra uma entrega e já desconta do estoque.
     * @return ID da entrega ou -1 em caso de erro.
     */
    public long insertEntrega(int funcionarioId,
                              int uniformeId,
                              int quantidade,
                              String dataEntrega) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put("funcionario_id", funcionarioId);
            cv.put("uniforme_id",    uniformeId);
            cv.put("quantidade",     quantidade);
            cv.put("data_entrega",   dataEntrega);
            long entregaId = db.insert("entregas", null, cv);
            if (entregaId == -1) throw new RuntimeException("Erro ao inserir entrega");

            // agora atualiza estoque
            Cursor c = db.rawQuery(
                    "SELECT quantidade FROM estoque WHERE uniforme_id = ?",
                    new String[]{ String.valueOf(uniformeId) }
            );
            int current = 0;
            if (c.moveToFirst()) current = c.getInt(0);
            c.close();
            if (current < quantidade) throw new RuntimeException("Estoque insuficiente");

            ContentValues stock = new ContentValues();
            stock.put("quantidade", current - quantidade);
            int rows = db.update("estoque", stock, "uniforme_id = ?", new String[]{ String.valueOf(uniformeId) });
            if (rows == 0) throw new RuntimeException("Falha ao atualizar estoque");

            db.setTransactionSuccessful();
            return entregaId;
        } catch (Exception ex) {
            return -1;
        } finally {
            db.endTransaction();
        }
    }

    /** Retorna todas as entregas ordenadas por data (desc). */
    public List<HistoricoEntrega> getAllDeliveries() {
        return getDeliveriesByFilter(null, null);
    }

    /**
     * Busca entregas filtrando por nome de funcionário e tipo de uniforme.
     * Se o filtro for nulo ou vazio, traz todos (%).
     */
    public List<HistoricoEntrega> getDeliveriesByFilter(String funcionarioFilter,
                                                        String uniformeFilter) {
        List<HistoricoEntrega> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String funcLike = (funcionarioFilter == null || funcionarioFilter.isEmpty())
                ? "%" : "%" + funcionarioFilter + "%";
        String uniLike  = (uniformeFilter    == null || uniformeFilter.isEmpty())
                ? "%" : "%" + uniformeFilter + "%";

        String sql =
                "SELECT e.id, f.nome AS funcionario, u.tipo AS uniforme, " +
                        "       e.quantidade, e.data_entrega " +
                        "  FROM entregas e " +
                        "  JOIN funcionarios f ON e.funcionario_id = f.id " +
                        "  JOIN uniformes   u ON e.uniforme_id    = u.id " +
                        " WHERE f.nome LIKE ? " +
                        "   AND u.tipo LIKE ? " +
                        " ORDER BY e.data_entrega DESC;";

        Cursor c = db.rawQuery(sql, new String[]{ funcLike, uniLike });
        if (c.moveToFirst()) {
            do {
                int    id          = c.getInt   (c.getColumnIndexOrThrow("id"));
                String func        = c.getString(c.getColumnIndexOrThrow("funcionario"));
                String uni         = c.getString(c.getColumnIndexOrThrow("uniforme"));
                int    qtde        = c.getInt   (c.getColumnIndexOrThrow("quantidade"));
                String dataEntrega = c.getString(c.getColumnIndexOrThrow("data_entrega"));
                list.add(new HistoricoEntrega(id, func, uni, qtde, dataEntrega));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }
}